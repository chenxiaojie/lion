package com.chenxiaojie.lion.notify.observer;

import com.chenxiaojie.lion.dao.LionListenerDao;
import com.chenxiaojie.lion.dto.LionMapDTO;
import com.chenxiaojie.lion.entity.LionListener;
import com.chenxiaojie.lion.notify.listener.Listener;
import com.chenxiaojie.lion.notify.message.NotifyMessage;
import com.chenxiaojie.lion.type.NotifyType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xiaojie.chen on 2015-03-22 17:12:59.
 */
@Component("observer")
@Slf4j
public class Observer implements DisposableBean {

    @Autowired
    private LionListenerDao lionListenerDao;

    private Thread thread;
    private Map<String, Listener> listenerMap = Maps.newHashMap();
    private LinkedList<NotifyMessage> msgList = Lists.newLinkedList();
    private LinkedList<Listener> errorListenerList = Lists.newLinkedList();
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void notifyAll(final LionMapDTO lionMapDTO, final NotifyType notifyType) {
        msgList.add(new NotifyMessage(lionMapDTO, notifyType));
        lock.lock();
        condition.signal();
        lock.unlock();
    }

    private List<Listener> getListenerList(String projectName, int env) {
        List<LionListener> lionListenerList = lionListenerDao.getActiveByProjectAndEnv(projectName, env);
        if (lionListenerList != null && lionListenerList.size() > 0) {
            List<Listener> listenerList = Lists.newArrayList();
            for (LionListener lionListener : lionListenerList) {
                try {
                    Listener listener = listenerMap.get(lionListener.getListenerURL());
                    if (listener == null) {
                        listener = new Listener(lionListener.getListenerURL());
                        listenerMap.put(lionListener.getListenerURL(), listener);
                    }
                    listenerList.add(listener);
                } catch (Exception e) {
                    log.info("getListenerList异常,移除错误的Listener,URL:" + lionListener.getListenerURL());
                    removeListener(lionListener.getProjectName(), lionListener.getListenerURL(), lionListener.getEnv());
                }
            }
            return listenerList;
        }
        return null;
    }

    private void removeListener(String projectName, String listenerURL, int env) {
        boolean result = lionListenerDao.updateActiveByProjectAndEnvAndURL(false, projectName, env, listenerURL) > 0;
        log.info("removeListener,projectName:" + projectName + ",listenerURL:" + listenerURL + ",env" + env + "result:" + result);
    }

    public Observer() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    processMsgQueue();
                }
            }
        });
        thread.start();
    }

    private void processMsgQueue() {
        try {
            lock.lock();
            while (msgList.isEmpty()) {
                condition.await();
            }
            processMsg(msgList.removeFirst());
        } catch (InterruptedException e) {
            log.error("Listener等待异常", e);
        } finally {
            lock.unlock();
        }
    }

    private void processMsg(NotifyMessage notifyMessage) {
        LionMapDTO lionMapDTO = notifyMessage.getLionMapDTO();
        List<Listener> listenerList = getListenerList(lionMapDTO.getProjectName(), lionMapDTO.getEnv());
        if (listenerList != null && listenerList.size() > 0) {
            for (Listener listener : listenerList) {
                try {
                    listener.notify(lionMapDTO, notifyMessage.getNotifyType());
                } catch (Exception e) {
                    //通知异常删除这个监听器,添加到异常List,避免循环时删除异常
                    errorListenerList.add(listener);
                    removeListener(lionMapDTO.getProjectName(), listener.getUrl(), lionMapDTO.getEnv());
                }
            }
            if (!errorListenerList.isEmpty()) {
                for (Listener listener : errorListenerList) {
                    listenerList.remove(listener);
                }
                errorListenerList.clear();
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        thread.interrupt();
    }
}
