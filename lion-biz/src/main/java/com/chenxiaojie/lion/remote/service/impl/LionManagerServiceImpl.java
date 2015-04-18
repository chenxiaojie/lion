package com.chenxiaojie.lion.remote.service.impl;

import com.chenxiaojie.lion.dao.LionMapDao;
import com.chenxiaojie.lion.entity.LionMap;
import com.chenxiaojie.lion.remote.service.LionManagerService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by xiaojie.chen on 2015-03-15 13:24:26.
 */
@Service("lionManagerService")
@Slf4j
public class LionManagerServiceImpl implements LionManagerService {

    @Autowired
    private LionMapDao lionMapDao;

    @Override
    public String getValue(String key, String projectName, int env) {
        log.info("remote getValue key:" + key + ",projectName:" + projectName + ",env:" + env);
        LionMap lionMap = lionMapDao.getByKeyProjectAndEnv(key, projectName, env);
        if (lionMap != null) {
            return lionMap.getMapValue();
        }
        return null;
    }

    @Override
    public Map<String, String> getValues(String projectName, int env) {
        log.info("remote getValues projectName:" + projectName + ",env:" + env);
        Map<String, String> projectMap = Maps.newHashMap();
        List<LionMap> lionMaps = lionMapDao.getByProjectAndEnv(projectName, env);
        for (LionMap lionMap : lionMaps) {
            projectMap.put(projectName + "." + lionMap.getMapKey(), lionMap.getMapValue());
        }
        return projectMap;
    }

}