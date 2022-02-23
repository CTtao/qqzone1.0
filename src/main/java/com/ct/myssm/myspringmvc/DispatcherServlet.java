package com.ct.myssm.myspringmvc;

import com.ct.myssm.ioc.BeanFactory;
import com.ct.myssm.utils.StringUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
* @Description: 中央控制器
* @Param:
* @return:
* @Author: CT
* @Date: 2021/12/30 15:28
*/
@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet{

    private BeanFactory beanFactory;
    @Override
    public void init() throws ServletException {
        super.init();
        //优化为从作用域中获取beanFactory
        //beanFactory = new ClassPathXmlApplicationContext();
        ServletContext application = getServletContext();
        Object beanFactoryObj = application.getAttribute("beanFactory");
        if(beanFactoryObj!=null){
            beanFactory = (BeanFactory)beanFactoryObj;
        }else {
            throw new RuntimeException("IOC容器获取失败");
        }
    }

    public DispatcherServlet(){

    }
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置编码放到了Filter中
        //获取访问的路径：/***.do
        String servletPath = req.getServletPath();

        //排除掉/和.do，留下***
        servletPath = servletPath.substring(1);
        int lastDotIndex = servletPath.lastIndexOf(".do");
        servletPath = servletPath.substring(0,lastDotIndex);

        Object controllerBeanObj = beanFactory.getBean(servletPath);

        String operate = req.getParameter("operate");

        if(StringUtil.isEmpty(operate)){
            operate ="index";
        }
        try {
            Method[] methods = controllerBeanObj.getClass().getDeclaredMethods();
            for(Method method : methods){
                if(operate.equals(method.getName())){
                    //1.统一获取请求参数
                    Parameter[] parameters = method.getParameters();
                    //存放参数的值
                    Object[] parameterValues = new Object[parameters.length];
                    for(int i = 0;i<parameters.length;i++){
                        Parameter parameter = parameters[i];
                        String parameterName = parameter.getName();
                        if("req".equals(parameterName)){
                            parameterValues[i] = req;
                        }else if("resp".equals(parameterName)){
                            parameterValues[i] = resp;
                        }else if("session".equals(parameterName)){
                            parameterValues[i] =req.getSession();
                        }else {
                            String parameterValue = req.getParameter(parameterName);
                            String typeName = parameter.getType().getName();
                            Object parameterObj = parameterValue;

                            if(parameterObj!=null){
                                if("java.lang.Integer".equals(typeName)){
                                    parameterObj = Integer.parseInt(parameterValue);
                                }
                            }
                            parameterValues[i] = parameterObj;
                        }
                    }

                    //2.controller组件方法调用
                    method.setAccessible(true);
                    Object returnObj = method.invoke(controllerBeanObj,parameterValues);

                    //3.视图处理
                    String methodStringStr = (String) returnObj;
                    if(methodStringStr.startsWith("redirect:")){
                        String redirectStr = methodStringStr.substring("redirect:".length());
                        resp.sendRedirect(redirectStr);
                    }else {
                        super.processTemplate(methodStringStr,req,resp);
                    }
                }
            }
//            }else {
//                throw new RuntimeException("operate值非法！");
//            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DispatcherServletException("DispatcherServlet出错了");
        }
    }
}
