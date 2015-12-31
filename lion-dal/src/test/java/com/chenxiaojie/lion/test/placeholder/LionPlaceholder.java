package com.chenxiaojie.lion.test.placeholder;

import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Map;
import java.util.Properties;

/**
 * Created by Administrator on 2015/3/3.
 */
public class LionPlaceholder extends PropertyPlaceholderConfigurer {

    private Properties lionLocalProperties;

    private Map<String, String> getRemoteProperties() {
        Map<String, String> remoteProperties = Maps.newHashMap();
        remoteProperties.put("age", "22");
        remoteProperties.put("name", "陈孝杰");
        remoteProperties.put("lion", "自己开发lion");
        remoteProperties.put("phoneNum", "13476147026");
        return remoteProperties;
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        props.putAll(getRemoteProperties());
        lionLocalProperties = props;
        super.processProperties(beanFactoryToProcess, props);
    }


    /**
     * 获取值
     *
     * @param key          key
     * @param defaultValue 如果value为null则返回默认值defaultValue
     * @return
     */
    public Object getProperty(String key, String defaultValue) {
        Object value = lionLocalProperties.get(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

}
