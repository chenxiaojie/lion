package com.chenxiaojie.lion.biz.impl;

import com.chenxiaojie.lion.biz.LionMapBiz;
import com.chenxiaojie.lion.dao.LionMapDao;
import com.chenxiaojie.lion.entity.LionMap;
import com.chenxiaojie.lion.notify.observer.Observer;
import com.chenxiaojie.lion.transformer.LionMapDTOTransformer;
import com.chenxiaojie.lion.type.NotifyType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by xiaojie.chen on 2015-03-05 13:27:36.
 */
@Service("lionMapBiz")
@Slf4j
public class LionMapBizImpl implements LionMapBiz {

    @Autowired
    private LionMapDao lionMapDao;

    @Autowired
    @Qualifier("observer")
    private Observer observer;

//    @Override
//    public int insert(LionMap lionMap) {
//        int result = lionMapDao.insert(lionMap);
//        if (result > 0) {
//            observer.notifyAll(LionMapDTOTransformer.INSTANCE.apply(lionMap), NotifyType.INSERT);
//        }
//        log.info("insert,lionMap:" + lionMap + ">>>result:" + result);
//        return result;
//    }

    @Override
    public int inserts(List<LionMap> lionMapList) {
        int result = lionMapDao.inserts(lionMapList);
        if (result == lionMapList.size()) {
            for (LionMap lionMap : lionMapList) {
                observer.notifyAll(LionMapDTOTransformer.INSTANCE.apply(lionMap), NotifyType.INSERT);
            }
        }
        StringBuilder sb = new StringBuilder("inserts,");
        for (LionMap lionMap : lionMapList) {
            sb.append("lionMap:");
            sb.append(lionMap);
        }
        sb.append(">>>result:");
        sb.append(result == lionMapList.size());
        log.info(sb.toString());
        return result;
    }

//    @Override
//    public int deleteByID(int id) {
//        LionMap lionMap = lionMapDao.getByID(id);
//        int result = 0;
//        if (lionMap != null) {
//            result = lionMapDao.deleteByID(id);
//            if (result > 0) {
//                observer.notifyAll(LionMapDTOTransformer.INSTANCE.apply(lionMap), NotifyType.DELETE);
//            }
//        }
//        log.info("deleteByID,id:" + id + ">>>result:" + result);
//        return result;
//    }

//    @Override
//    public int deleteByKeyProjectAndEnv(String mapKey, String projectName, int env) {
//        LionMap lionMap = lionMapDao.getByKeyProjectAndEnv(mapKey, projectName, env);
//        int result = 0;
//        if (lionMap != null) {
//            result = lionMapDao.deleteByKeyProjectAndEnv(mapKey, projectName, env);
//            if (result > 0) {
//                observer.notifyAll(LionMapDTOTransformer.INSTANCE.apply(lionMap), NotifyType.DELETE);
//            }
//        }
//        log.info("deleteByKeyProjectAndEnv,mapKey:" + mapKey + ",projectName:" + projectName +
//                ",env:" + env + ">>>result:" + result);
//        return result;
//    }

    @Override
    public int deleteByKeyProjectAndEnvs(String mapKey, String projectName, int[] envs) {
        List<LionMap> lionMapList = lionMapDao.getByKeyProjectAndEnvs(mapKey, projectName, envs);
        int result = lionMapDao.deleteByKeyProjectAndEnvs(mapKey, projectName, envs);
        if (result == envs.length) {
            if (lionMapList != null) {
                for (LionMap lion : lionMapList) {
                    observer.notifyAll(LionMapDTOTransformer.INSTANCE.apply(lion), NotifyType.DELETE);
                }
            }
        }
        log.info("deleteByKeyProjectAndEnvs,mapKey:" + mapKey + ",projectName:" +
                projectName + ",envs:" + Arrays.toString(envs) + ">>>result:" + (result == envs.length));
        return result;
    }

    @Override
    public int updates(LionMap lionMap, int[] envs) {
        int result = lionMapDao.updates(lionMap, envs);
        if (result == envs.length) {
            List<LionMap> lionMapList = lionMapDao.getByKeyProjectAndEnvs(lionMap.getMapKey(), lionMap.getProjectName(), envs);
            if (lionMapList != null) {
                for (LionMap lion : lionMapList) {
                    observer.notifyAll(LionMapDTOTransformer.INSTANCE.apply(lion), NotifyType.UPDATE);
                }
            }
        }
        log.info("updates,lionMap:" + lionMap + ",envs:" +
                Arrays.toString(envs) + ">>>result:" + (result == envs.length));
        return result;
    }

//    @Override
//    public int updateValueByKeyProjectAndEnv(String mapValue, String mapKey, String projectName, int env) {
//        int result = lionMapDao.updateValueByKeyProjectAndEnv(mapValue, mapKey, projectName, env);
//        if (result > 0) {
//            LionMap lionMap = lionMapDao.getByKeyProjectAndEnv(mapKey, projectName, env);
//            if (lionMap != null) {
//                observer.notifyAll(LionMapDTOTransformer.INSTANCE.apply(lionMap), NotifyType.UPDATE);
//            }
//        }
//        log.info("updateValueByKeyProjectAndEnv,mapValue:" + mapValue + ",mapKey:" +
//                mapKey + ",projectName:" + projectName + ",env:" + env + ">>>result:" + result);
//        return result;
//    }
//
//    @Override
//    public int updateValueByKeyProjectAndEnvs(String mapValue, String mapKey, String projectName, int[] envs) {
//        int result = lionMapDao.updateValueByKeyProjectAndEnvs(mapValue, mapKey, projectName, envs);
//        if (result == envs.length) {
//            List<LionMap> lionMapList = lionMapDao.getByKeyProjectAndEnvs(mapKey, projectName, envs);
//            if (lionMapList != null) {
//                for (LionMap lionMap : lionMapList) {
//                    observer.notifyAll(LionMapDTOTransformer.INSTANCE.apply(lionMap), NotifyType.UPDATE);
//                }
//            }
//        }
//        log.info("updateValueByKeyProjectAndEnvs,mapValue:" + mapValue +
//                ",mapKey" + mapKey + ",projectName:" + projectName + ",envs:" +
//                Arrays.toString(envs) + ">>>result:" + (result == envs.length));
//        return result;
//    }
//
//    @Override
//    public int replace(LionMap lionMap) {
//        int result = lionMapDao.replace(lionMap);
//        if (result > 0) {
//            observer.notifyAll(LionMapDTOTransformer.INSTANCE.apply(lionMap), NotifyType.UPDATE);
//        }
//        log.info("replace,lionMap:" + lionMap + ">>>result:" + result);
//        return result;
//    }

    @Override
    public LionMap getByKeyProjectAndEnv(String mapKey, String projectName, int env) {
        return lionMapDao.getByKeyProjectAndEnv(mapKey, projectName, env);
    }

    @Override
    public List<LionMap> getByKeyProjectAndEnvs(String mapKey, String projectName, int[] envs) {
        return lionMapDao.getByKeyProjectAndEnvs(mapKey, projectName, envs);
    }

    @Override
    public List<LionMap> getByProjectAndEnv(String projectName, int env) {
        return lionMapDao.getByProjectAndEnv(projectName, env);
    }

    @Override
    public List<LionMap> getPageByProjectAndEnv(String projectName, int env, int start, int size) {
        return lionMapDao.getPageByProjectAndEnv(projectName, env, (start - 1) * size, size);
    }

    @Override
    public int getCountByProjectAndEnv(String projectName, int env) {
        return lionMapDao.getCountByProjectAndEnv(projectName, env);
    }

    @Override
    public List<String> getAllProjectNames() {
        return lionMapDao.getAllProjectNames();
    }
}
