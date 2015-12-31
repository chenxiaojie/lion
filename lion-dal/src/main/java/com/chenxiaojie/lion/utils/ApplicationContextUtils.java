package com.chenxiaojie.lion.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * WebApplicationContext springContext = WebApplicationContextUtils
 * .getRequiredWebApplicationContext(request.getServletContext());
 * ContextLoader.getCurrentWebApplicationContext()
 * 获得spring的上下文
 * Root WebApplicationContext: startup date [Tue May 13 20:50:46 CST 2014];
 * root of context hierarchy
 * ***********************************************************
 * WebApplicationContext springMVCContext = RequestContextUtils
 * .getWebApplicationContext(request);
 * 获取springMvc的上下文
 * WebApplicationContext for namespace 'springMVC-servlet': startup date
 * [Tue May 13 20:51:10 CST 2014]; parent: Root WebApplicationContext
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext appcontext;

    /**
     * @param applicationContext Component注解是谁配置文件解析的,ac则是谁的上下文
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.appcontext = applicationContext;
    }

    public static Object getBean(String beanName) {
        return appcontext.getBean(beanName);
    }
}
