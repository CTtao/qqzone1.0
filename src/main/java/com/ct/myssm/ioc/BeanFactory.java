package com.ct.myssm.ioc;

/**
* @Description: 定义ioc容器操作规范
* @Param:
* @return:
* @Author: CT
* @Date: 2022/1/6 11:06
*/
public interface BeanFactory {
    Object getBean(String id);
}
