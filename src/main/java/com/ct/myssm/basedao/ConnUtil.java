package com.ct.myssm.basedao;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
* @Description: 获取连接工具类
* @Param:
* @return:
* @Author: CT
* @Date: 2022/1/6 11:04
*/
public class ConnUtil {

    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();
    static Properties pros = new Properties();
    static {
        InputStream is = ConnUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
        try {
            pros.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Connection createConn() {
        try {
            DataSource dataSource = DruidDataSourceFactory.createDataSource(pros);
            return dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Connection getConn(){
        Connection conn = threadLocal.get();
        if(conn==null){
            conn = createConn();
            threadLocal.set(conn);
        }
        return threadLocal.get();
    }
    public static void closeConn() throws SQLException {
        Connection conn = threadLocal.get();
        if(conn ==null){
            return;
        }
        if(!conn.isClosed()){
            conn.close();
            threadLocal.set(null);
        }
    }
}
