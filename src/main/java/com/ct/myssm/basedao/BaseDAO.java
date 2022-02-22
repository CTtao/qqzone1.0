package com.ct.myssm.basedao;

import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
* @Description: 对Mysql的连接、一系列CRUD基础操作、以及特殊操作
* @Param:
* @return:
* @Author: CT
* @Date: 2022/2/6 11:04
*/
public abstract class BaseDAO<T> {

    //连接参数
    public final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public final String URL = "jdbc:mysql://localhost:3306/fruit_db?useUnicode=true&characterEncoding=utf-8";
    public final String USER ="root";
    public final String PASSWORD ="";

    //三大资源
    protected Connection conn;
    protected PreparedStatement ps;
    protected ResultSet rs;

    private Class entityClass;

    public BaseDAO(){
        Type genericType = getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();

        Type actualType = actualTypeArguments[0];
        try {
            entityClass = Class.forName(actualType.getTypeName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new DAOException("BaseDAO 构造方法出错了");
        }
    }
    //获取连接
    protected Connection getConn() {
        return ConnUtil.getConn();
    }
    //资源关闭
    protected void close(ResultSet rs,PreparedStatement ps,Connection conn){

    }
    //给预处理命令设置参数
    private void setParams(PreparedStatement ps,Object...args) throws SQLException {
        if(args!=null&&args.length>0){
            for(int i =0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
        }
    }
    //执行更新，返回影响行数
    protected int executeUpdate(String sql,Object...args){
        boolean insertFlag = false;
        insertFlag=sql.trim().toUpperCase().startsWith("INSERT");
        conn=getConn();
        int count = 0;
        try {
            if(insertFlag){
                ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            }else {
                ps = conn.prepareStatement(sql);
            }
            setParams(ps,args);
            count = ps.executeUpdate();

            if (insertFlag){
                rs = ps.getGeneratedKeys();
                if(rs.next()){
                    return ((Long)rs.getLong(1)).intValue();
                }
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("BaseDAO executeUpdate出错了");
        }
    }
    //通过反射给obj对象的property属性赋值propertyValue
    private void setValue(Object obj,String property,Object propertyValue) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Class clazz = obj.getClass();
            Field field = clazz.getDeclaredField(property);
            if(field!=null){
                //获取字段类型
                String typeName = field.getType().getName();
                //判断如果是自定义类型，则需要调用这个自定义类的带一个参的构造方法
                if(isMyType(typeName)){
                    Class typeNameClass = Class.forName(typeName);
                    Constructor constructor = typeNameClass.getDeclaredConstructor(Integer.class);
                    propertyValue = constructor.newInstance(propertyValue);
                }
                field.setAccessible(true);
                field.set(obj,propertyValue);
            }
    }

    private static boolean isNotMyType(String typeName){
        return "java.lang.Integer".equals(typeName)||"java.lang.String".equals(typeName)||"java.util.Date".equals(typeName)||"java.sql.Date".equals(typeName)||"java.lang.Double".equals(typeName);
    }
    private static boolean isMyType(String typeName){
        return !isNotMyType(typeName);
    }
    //执行复杂查询，返回结果
    protected  Object[] executeComplexQuery(String sql,Object...args){
        try{
            conn =getConn();
            ps=conn.prepareStatement(sql);
            setParams(ps,args);
            rs =ps.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            Object[] columnValueArr = new Object[columnCount];
            if(rs.next()){
                for(int i = 0;i<columnCount;i++){
                    Object columnValue = rs.getObject(i + 1);
                    columnValueArr[i] = columnValue;
                }
                return columnValueArr;
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new DAOException("BaseDAO executeComplexQuery出错了");
        }
        return null;
    }
    //执行查询，返回单个对象
    protected T load(String sql,Object...args){
        conn =getConn();
        try {
            ps=conn.prepareStatement(sql);
            setParams(ps,args);
            rs =ps.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if(rs.next()){
                T entity = (T)entityClass.newInstance();

                for(int i =0;i<columnCount;i++){
                    String columnName = rsmd.getColumnLabel(i + 1);
                    Object columnValue = rs.getObject(i+1);
                    setValue(entity,columnName,columnValue);
                }
                return entity;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new DAOException("BaseDAO load出错了");
        }
        return null;
    }
    //执行查询，返回list
    protected List<T> executeQuery(String sql,Object...args){
        List<T> list = new ArrayList<>();
        conn=getConn();
        try {
            ps = conn.prepareStatement(sql);
            setParams(ps,args);
            rs = ps.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (rs.next()){
                T entity  =(T)entityClass.newInstance();

                for(int i =0;i<columnCount;i++){
                    String columnName = rsmd.getColumnLabel(i + 1);
                    Object columnValue = rs.getObject(i+1);
                    setValue(entity,columnName,columnValue);
                }
                list.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("BaseDAO executeQuery出错了");
        }
        return list;
    }
}
