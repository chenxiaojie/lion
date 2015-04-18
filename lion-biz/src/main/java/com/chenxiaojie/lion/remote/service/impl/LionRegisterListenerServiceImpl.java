package com.chenxiaojie.lion.remote.service.impl;

import com.chenxiaojie.lion.dao.LionMapDao;
import com.chenxiaojie.lion.entity.LionMap;
import com.chenxiaojie.lion.notify.listener.ListenerUrlPool;
import com.chenxiaojie.lion.notify.listener.Observer;
import com.chenxiaojie.lion.remote.service.LionRegisterListenerService;
import com.chenxiaojie.lion.utils.LionEnvUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xiaojie.chen on 2015-03-22 10:06:48.
 */
@Service("lionRegisterListenerService")
@Slf4j
public class LionRegisterListenerServiceImpl implements LionRegisterListenerService, InitializingBean, DisposableBean {

    @Autowired
    private LionMapDao lionMapDao;

    @Autowired
    @Qualifier("observer")
    private Observer observer;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    private Lock lock = new ReentrantLock();

    @Override
    public boolean registerListener(String projectName, String listenerUrl, final int env) {
        log.info("registerListener,projectName:" + projectName + ",listenerUrl:" + listenerUrl + ",env" + env);
        Multimap<String, String> listenerMap = ListenerUrlPool.getListenerMap(env);
        listenerMap.put(projectName, listenerUrl);
        observer.addListener(projectName, listenerUrl, env);
        replaceListenerMap(env);
        return true;
    }

    @Override
    public boolean removeListener(String projectName, String listenerUrl, final int env) {
        log.info("removeListener,projectName:" + projectName + ",listenerUrl:" + listenerUrl + ",env" + env);
        Multimap<String, String> listenerMap = ListenerUrlPool.getListenerMap(env);
        listenerMap.remove(projectName, listenerUrl);
        replaceListenerMap(env);
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化listenerMap的数据
        log.info("init listenerMap start");
        List<LionMap> lionMapList = lionMapDao.getByKeyProjectAndEnvs("listenerUrls", "lion", LionEnvUtils.All_Lion_Env_Values);
        if (lionMapList != null && lionMapList.size() > 0) {
            for (LionMap lionMap : lionMapList) {
                Multimap<String, String> listenerMap = ListenerUrlPool.getListenerMap(lionMap.getEnv());
                for (String projectUrl : Splitter.on('|').omitEmptyStrings().split(lionMap.getMapValue())) {
                    Iterator<String> projectUrls = Splitter.on(',').omitEmptyStrings().split(projectUrl).iterator();
                    String projectName = "";
                    if (projectUrls.hasNext()) {
                        projectName = projectUrls.next();
                    }
                    while (projectUrls.hasNext()) {
                        String listenerUrl = projectUrls.next();
                        listenerMap.put(projectName, listenerUrl);
                        observer.addListener(projectName, listenerUrl, lionMap.getEnv());
                    }
                }
            }
        }
        log.info("init listenerMap finish");
    }

    private void replaceListenerMap(final int env) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    lock.lock();
                    Multimap<String, String> listenerMap = ListenerUrlPool.getListenerMap(env);
                    StringBuilder sb = new StringBuilder();
                    for (String projectName : listenerMap.keySet()) {
                        sb.append(projectName);
                        for (String url : listenerMap.get(projectName)) {
                            sb.append(',');
                            sb.append(url);
                        }
                        if (sb.length() > 1 && sb.charAt(sb.length() - 1) == ',') {
                            sb.deleteCharAt(sb.length() - 1);
                        }
                        sb.append('|');
                    }
                    if (sb.length() > 1 && sb.charAt(sb.length() - 1) == '|') {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    LionMap lionMap = new LionMap();
                    lionMap.setLazy(false);
                    lionMap.setProjectName("lion");
                    lionMap.setMapKey("listenerUrls");
                    lionMap.setMapValue(sb.toString());
                    lionMap.setEnv(env);
                    lionMapDao.replace(lionMap);
                } finally {
                    lock.unlock();
                }
            }
        });
    }

    @Override
    public void destroy() throws Exception {
        executorService.shutdownNow();
    }
}
