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
    private Map<String, Listener> listenerMap = Maps.newConcurrentMap();

    /**
     * 任务队列
     */
    private LinkedList<NotifyMessage> msgQueue = Lists.newLinkedList();

    /**
     * 处理任务队列的线程
     */
    private ExecutorService msgQueueMonitorExecutorService = Executors.newSingleThreadExecutor();

    /**
     * 发送通知线程
     */
    private ExecutorService sendMsgExecutorService = Executors.newFixedThreadPool(100);

    /**
     * 线程锁,避免多线程并发产生数据不一致的问题
     */
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void notifyAll(LionMapDTO lionMapDTO, NotifyType notifyType) {
        msgQueue.add(new NotifyMessage(lionMapDTO, notifyType));
        lock.lock();
        condition.signal();
        lock.unlock();
    }

    private void processMsgQueue() {
        try {
            lock.lock();
            while (msgQueue.isEmpty()) {
                condition.await();
            }
            processMsg(msgQueue.removeFirst());
        } catch (Exception e) {
            log.error("processMsgQueue异常", e);
        } finally {
            lock.unlock();
        }
    }

    private void processMsg(final NotifyMessage notifyMessage) {
        final LionMapDTO lionMapDTO = notifyMessage.getLionMapDTO();
        List<LionListener> lionListenerList = lionListenerDao.getActiveByProjectAndEnv(lionMapDTO.getProjectName(), lionMapDTO.getEnv());

        if (lionListenerList != null && lionListenerList.size() > 0) {
            for (final LionListener lionListener : lionListenerList) {
                sendMsgExecutorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Listener listener = listenerMap.get(lionListener.getListenerURL());
                            if (listener == null) {
                                listener = new Listener(lionListener.getListenerURL());
                                listenerMap.put(lionListener.getListenerURL(), listener);
                            }
                            listener.notify(lionMapDTO, notifyMessage.getNotifyType());
                            log.info("notify success,listenerURL:" + listener.getUrl() + ";lionMapDTO:" + lionMapDTO + ";notifyType:" + notifyMessage.getNotifyType());
                        } catch (Exception e) {
                            //通知异常删除这个监听器,添加到异常List,避免循环时删除异常
                            log.warn("processMsg异常,移除错误的Listener");
                            listenerMap.remove(lionListener.getListenerURL());
                            lionListenerDao.updateActiveByProjectAndEnvAndURL(false, lionListener.getProjectName(), lionListener.getEnv(), lionListener.getListenerURL());
                            log.info("removeListener,projectName:" + lionListener.getProjectName() + ",listenerURL:" + lionListener.getListenerURL() + ",env" + lionListener.getEnv());
                        }
                    }
                });
            }
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("afterPropertiesSet");
        msgQueueMonitorExecutorService.submit(new Runnable() {
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
        try {
            //msgQueueMonitorExecutorService.shutdownNow();
            sendMsgExecutorService.shutdown();
            msgQueueMonitorExecutorService.shutdown();
        } catch (Exception e) {
            log.error("destroy error", e);
        }
    }

//    @PostConstruct
//    public void postConstruct() throws Exception {
//        log.info("postConstruct 注解会先执行");
//    }
//
//    @PreDestroy
//    public void preDestroy() throws Exception {
//        log.info("preDestroy 注解会先执行");
//    }

}
