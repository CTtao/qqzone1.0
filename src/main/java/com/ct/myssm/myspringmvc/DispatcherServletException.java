package com.ct.myssm.myspringmvc;

/**
* @Description: 中央控制器异常
* @Param:
* @return:
* @Author: CT
* @Date: 2022/2/6 10:59
*/
public class DispatcherServletException extends RuntimeException{
    public DispatcherServletException(String msg){
        super(msg);
    }
}
