package com.ct.myssm.basedao;

/**
* @Description: DAO操作相关异常
* @Param:
* @return:
* @Author: CT
* @Date: 2022/2/6 11:05
*/
public class DAOException extends RuntimeException{
    public DAOException(String msg){
        super(msg);
    }
}
