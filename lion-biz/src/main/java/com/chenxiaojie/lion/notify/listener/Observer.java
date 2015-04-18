package com.chenxiaojie.lion.notify.listener;

import com.chenxiaojie.lion.dto.LionMapDTO;
import com.chenxiaojie.lion.remote.service.LionRegisterListenerService;
import com.chenxiaojie.lion.type.NotifyType;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
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
    @Qualifier("lionRegisterListenerService")
    private LionRegisterListenerService lionRegisterListenerService;

    private Thread thread;
    private LinkedList<NotifyMessage> msgList = Lists.newLinkedList();
    private LinkedList<Listener> errorListenerList = Lists.newLinkedList();
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void addListener(String projectName, String url, int env) {
        try {
            Listener listener = new Listener(url, env);
            List<Listener> listenerList = ListenerPool.getListenerMap(env).get(projectName);
            if (listenerList == null) {
                listenerList = Lists.newArrayList();
            }
            listenerList.add(listener);
            ListenerPool.getListenerMap(env).put(projectName, listenerList);
        } catch (Exception e) {
            log.info("新建Listener异常,移除改URL");
            lionRegisterListenerService.removeListener(projectName, url, env);
        }
    }

    public void notifyAll(final LionMapDTO lionMapDTO, final NotifyType notifyType) {
        msgList.add(new NotifyMessage(lionMapDTO, notifyType));
        lock.lock();
        condition.signal();
        lock.unlock();
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
        List<Listener> listenerList = ListenerPool.getListenerMap(lionMapDTO.getEnv()).get(lionMapDTO.getProjectName());
        if (listenerList != null) {
            for (Listener listener : listenerList) {
                try {
                    listener.notify(lionMapDTO, notifyMessage.getNotifyType());
                } catch (Exception e) {
                    //通知异常删除这个监听器,添加到异常List,避免循环时删除异常
                    errorListenerList.add(listener);
                    lionRegisterListenerService.removeListener(lionMapDTO.getProjectName(), listener.getUrl(), listener.getEnv());
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
