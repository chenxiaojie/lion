package com.chenxiaojie.lion.notify.listener;

import com.chenxiaojie.lion.utils.LionEnvUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.util.List;

/**
 * Created by xiaojie.chen on 2015-03-26 16:54:56.
 */
public class ListenerUrlPool {
    private static List<Multimap<String, String>> listenerMapList = Lists.newArrayList();

    static {
        Multimap<String, String> listenerMap = null;
        for (int i = 0; i < LionEnvUtils.All_Lion_Env_Values.length; i++) {
            listenerMap = HashMultimap.create();
            listenerMapList.add(listenerMap);
        }
    }

    public static Multimap<String, String> getListenerMap(int env) {
        return listenerMapList.get(env - 1);
    }
}
