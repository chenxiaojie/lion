package com.chenxiaojie.lion.notify.listener;

import com.chenxiaojie.lion.utils.LionEnvUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created by xiaojie.chen on 2015-03-26 16:54:56.
 */
public class ListenerPool {

    private static List<Map<String, List<Listener>>> listenerMapList = Lists.newArrayList();

    static {
        Map<String, List<Listener>> listenerMap = null;
        for (int i = 0; i < LionEnvUtils.All_Lion_Env_Values.length; i++) {
            listenerMap = Maps.newHashMap();
            listenerMapList.add(listenerMap);
        }
    }

    public static Map<String, List<Listener>> getListenerMap(int env) {
        return listenerMapList.get(env - 1);
    }

}
