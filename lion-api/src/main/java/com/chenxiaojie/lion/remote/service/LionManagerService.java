package com.chenxiaojie.lion.remote.service;

import java.util.Map;

/**
 * Created by xiaojie.chen on 2015-03-15 13:23:37.
 */
public interface LionManagerService {

    /**
     * 从lion上面取值
     *
     * @param key
     * @param projectName 项目名
     * @return
     */
    public String getValue(String key, String projectName, int env);

    /**
     * 从lion上面取一整个项目的值
     *
     * @param projectName 项目名
     * @return
     */
    public Map<String, String> getValues(String projectName, int env);
}
