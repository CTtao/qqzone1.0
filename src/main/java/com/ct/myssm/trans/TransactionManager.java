package com.ct.myssm.trans;

import com.ct.myssm.basedao.ConnUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
* @Description: 事务管理，实现事务的开启、提交、回滚
* @Param:
* @return:
* @Author: CT
* @Date: 2022/2/6 11:07
*/
public class TransactionManager {


    //开启事务
    public static void beginTrans() throws SQLException {
        ConnUtil.getConn().setAutoCommit(false);
    }

    //提交
    public static void commit() throws SQLException {
        Connection conn = ConnUtil.getConn();
        conn.commit();
        ConnUtil.closeConn();
    }

    //回滚
    public static void rollback() throws SQLException {
        Connection conn = ConnUtil.getConn();
        conn.rollback();
        ConnUtil.closeConn();
    }
}
