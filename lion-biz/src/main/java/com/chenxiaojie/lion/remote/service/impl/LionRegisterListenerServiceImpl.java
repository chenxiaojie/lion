package com.chenxiaojie.lion.remote.service.impl;

import com.chenxiaojie.lion.dao.LionListenerDao;
import com.chenxiaojie.lion.entity.LionListener;
import com.chenxiaojie.lion.remote.service.LionRegisterListenerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xiaojie.chen on 2015-03-22 10:06:48.
 */
@Service("lionRegisterListenerService")
@Slf4j
public class LionRegisterListenerServiceImpl implements LionRegisterListenerService {

    @Autowired
    private LionListenerDao lionListenerDao;

    @Override
    public boolean registerListener(String projectName, String listenerURL, int env) {
        LionListener lionListener = lionListenerDao.getByProjectAndEnvAndURL(projectName, env, listenerURL);
        boolean result = true;
        if (lionListener != null) {
            if (!lionListener.isActive()) {
                result = lionListenerDao.updateActiveByID(true, lionListener.getId()) > 0;
            }
        } else {
            lionListener = new LionListener();
            lionListener.setActive(true);
            lionListener.setListenerURL(listenerURL);
            lionListener.setEnv(env);
            lionListener.setProjectName(projectName);
            result = lionListenerDao.insert(lionListener) > 0;
        }
        log.info("registerListener,projectName:" + projectName + ",listenerURL:" + listenerURL + ",env" + env + ",result:" + result);
        return result;
    }

}
