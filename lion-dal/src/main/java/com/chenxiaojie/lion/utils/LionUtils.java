package com.chenxiaojie.lion.utils;

import com.chenxiaojie.lion.dao.LionMapDao;
import com.chenxiaojie.lion.entity.LionMap;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaojie.chen on 2015-03-05 11:19:15.
 */
@Component
@Slf4j
public class LionUtils implements InitializingBean {

    @Autowired
    private LionMapDao lionMapDao;

    private static Map<String, Map<String, String>> projectMaps = new HashMap<String, Map<String, String>>();
    private static LionMapDao static_lionMapDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        static_lionMapDao = lionMapDao;
        log.info("##########lion initData##########");
        long curTime = System.currentTimeMillis();
        //在lion的服务器上不做缓存,让应用服务器进行缓存数据,达到应用服务器解除对lion强依赖
        //initData();
        log.info("##########lion initData use " + (System.currentTimeMillis() - curTime) + " millis##########");
    }

//    private void initData() {
//        List<String> projectNames = static_lionMapDao.getAllProjectNames();
//        Map<String, String> projectMap = null;
//        List<LionMap> lionMaps = null;
//        for (String projectName : projectNames) {
//            projectMap = new HashMap<String, String>();
//            lionMaps = static_lionMapDao.getLazyByProjectName(projectName);
//            for (LionMap lionMap : lionMaps) {
//                projectMap.put(lionMap.getMapKey(), lionMap.getMapValue());
//            }
//            projectMaps.put(projectName, projectMap);
//        }
//    }

    /**
     * 获取lion的value
     *
     * @param key
     * @param defaultValue 如果value为null则返回默认值defaultValue
     * @param env          环境
     * @return
     */
    public static String getProperty(String key, String defaultValue, int env) {
        if (!Strings.isNullOrEmpty(key)) {
            String[] projectName_key = key.split("\\.");
            if (projectName_key.length == 2) {
                String value = getValue(projectName_key[1], projectName_key[0], env);
                if (!Strings.isNullOrEmpty(value)) {
                    return value;
                }
            }
        }
        return defaultValue;
    }

    /**
     * 获取lion的value
     *
     * @param key
     * @param projectName
     * @param env
     * @return
     */
    private static String getValue(String key, String projectName, int env) {
        Map<String, String> projectMap = projectMaps.get(projectName);
        if (projectMap != null) {
            String value = projectMap.get(key);
            if (value != null) {
                return value;
            } else {
                LionMap lionMap = static_lionMapDao.getByKeyProjectAndEnv(key, projectName, env);
                if (lionMap != null) {
                    projectMap.put(lionMap.getMapKey(), lionMap.getMapValue());
                    return lionMap.getMapValue();
                }
            }
        }
        return null;
    }
}
