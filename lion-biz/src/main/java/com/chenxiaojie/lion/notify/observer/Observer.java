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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xiaojie.chen on 2015-03-22 17:12:59.
 */
@Slf4j
@Component("observer")
public class Observer implements InitializingBean, DisposableBean {

    @Autowired
    private LionListenerDao lionListenerDao;
    /**
     * 用于缓存所有Listener,key:URL,value:Listener
     */
    private Map<String, Listener> listenerMap = Maps.newHashMap();
    /**
     * 任务队列
     */
    private LinkedList<NotifyMessage> msgList = Lists.newLinkedList();
    /**
     * 处理任务队列的线程
     */
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    /**
     * 线程锁,避免多线程并发产生数据不一致的问题
     */
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void notifyAll(final LionMapDTO lionMapDTO, final NotifyType notifyType) {
        msgList.add(new NotifyMessage(lionMapDTO, notifyType));
        lock.lock();
        condition.signal();
        lock.unlock();
    }

    private void processMsgQueue() {
        try {
            lock.lock();
            while (msgList.isEmpty()) {
                condition.await();
            }
            processMsg(msgList.removeFirst());
        } catch (InterruptedException e) {
            log.error("processMsgQueue异常", e);
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
                    log.info("processMsg异常,移除错误的Listener");
                    removeListener(lionMapDTO.getProjectName(), listener.getUrl(), lionMapDTO.getEnv());
                }
            }
        }
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
                    log.info("getListenerList异常,移除错误的Listener");
                    removeListener(lionListener.getProjectName(), lionListener.getListenerURL(), lionListener.getEnv());
                }
            }
            return listenerList;
        }
        return null;
    }

    private void removeListener(String projectName, String listenerURL, int env) {
        listenerMap.remove(listenerURL);
        lionListenerDao.updateActiveByProjectAndEnvAndURL(false, projectName, env, listenerURL);
        log.info("removeListener,projectName:" + projectName + ",listenerURL:" + listenerURL + ",env" + env);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("afterPropertiesSet");
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    processMsgQueue();
                }
            }
        });
    }

    @Override
    public void destroy() throws Exception {
        log.info("destroy");
    }

    @PostConstruct
    public void postConstruct() throws Exception {
        log.info("postConstruct");
    }

    @PreDestroy
    public void preDestroy() throws Exception {
        log.info("preDestroy");
    }

}
