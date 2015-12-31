package com.chenxiaojie.lion.spring;

import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Map;
import java.util.Properties;

/**
 * Created by xiaojie.chen on 2015-03-04 15:35:16.
 */
public class LionPlaceholder extends PropertyPlaceholderConfigurer {

    private Map<String, String> getRemoteProperties() {
        Map<String, String> remoteProperties = Maps.newHashMap();
        remoteProperties.put("driverClassName", "com.mysql.jdbc.Driver");
        remoteProperties.put("url", "jdbc:mysql://localhost:3306/lion?useUnicode=true&amp;characterEncoding=UTF-8");
        remoteProperties.put("username", "root");
        remoteProperties.put("password", "qwe888888");
        return remoteProperties;
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        props.putAll(getRemoteProperties());
        super.processProperties(beanFactoryToProcess, props);
    }

}
