package com.chenxiaojie.lion.biz;

import com.chenxiaojie.lion.entity.LionMap;

import java.util.List;

/**
 * Created by xiaojie.chen on 2015-03-05 13:27:18.
 */
public interface LionMapBiz {
//    public int insert(LionMap lionMap);

    public int inserts(List<LionMap> lionMapList);

//    public int deleteByID(int id);

//    public int deleteByKeyProjectAndEnv(String mapKey, String projectName, int env);

    public int deleteByKeyProjectAndEnvs(String mapKey, String projectName, int[] envs);

    public int updates(LionMap lionMap, int[] envs);

//    public int updateValueByKeyProjectAndEnv(String mapValue, String mapKey, String projectName, int env);

//    public int updateValueByKeyProjectAndEnvs(String mapValue, String mapKey, String projectName, int[] envs);

//    public int replace(LionMap lionMap);

    public LionMap getByKeyProjectAndEnv(String mapKey, String projectName, int env);

    public List<LionMap> getByKeyProjectAndEnvs(String mapKey, String projectName, int[] envs);

    public List<LionMap> getByProjectAndEnv(String projectName, int env);

    public List<LionMap> getPageByProjectAndEnv(String projectName, int env, int start, int size);

    public int getCountByProjectAndEnv(String projectName, int env);

    public List<String> getAllProjectNames();
}
