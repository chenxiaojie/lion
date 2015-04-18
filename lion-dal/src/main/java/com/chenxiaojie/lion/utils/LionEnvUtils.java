package com.chenxiaojie.lion.utils;

import com.chenxiaojie.lion.entity.LionMap;
import com.chenxiaojie.lion.type.LionEnv;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by xiaojie.chen on 2015-03-24 20:15:27.
 */
public class LionEnvUtils {

    public final static int[] All_Lion_Env_Values = getAllLionEnvValues();

    private static int[] getAllLionEnvValues() {
        int[] values = new int[LionEnv.values().length];
        for (LionEnv lionEnv : LionEnv.values()) {
            values[lionEnv.ordinal()] = lionEnv.value;
        }
        return values;
    }

    public static List<LionMap> createAllEnvLionMaps(LionMap lionMap) {
        List<LionMap> lionMapList = Lists.newArrayList();
        for (int value : All_Lion_Env_Values) {
            LionMap lion = new LionMap();
            lion.setProjectName(lionMap.getProjectName());
            lion.setMapValue(lionMap.getMapValue());
            lion.setMapKey(lionMap.getMapKey());
            lion.setLazy(lionMap.isLazy());
            lion.setEnv(value);
            lionMapList.add(lion);
        }
        return lionMapList;
    }

    public static List<LionMap> createEnvLionMaps(LionMap lionMap, int[] envs) {
        List<LionMap> lionMapList = Lists.newArrayList();
        for (int value : envs) {
            LionMap lion = new LionMap();
            lion.setProjectName(lionMap.getProjectName());
            lion.setMapValue(lionMap.getMapValue());
            lion.setMapKey(lionMap.getMapKey());
            lion.setLazy(lionMap.isLazy());
            lion.setEnv(value);
            lionMapList.add(lion);
        }
        return lionMapList;
    }
}
