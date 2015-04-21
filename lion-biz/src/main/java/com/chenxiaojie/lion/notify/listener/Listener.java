package com.chenxiaojie.lion.notify.listener;

import com.chenxiaojie.lion.dto.LionMapDTO;
import com.chenxiaojie.lion.type.NotifyType;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

/**
 * Created by xiaojie.chen on 2015-03-22 17:18:39.
 */
public class Listener {

    private LionListener lionListener;

    private String url;

    public Listener(String url) {
        this.url = url;
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceUrl("rmi://" + this.url + "/notify");
        rmiProxyFactoryBean.setServiceInterface(LionListener.class);
        rmiProxyFactoryBean.afterPropertiesSet();
        this.lionListener = (LionListener) rmiProxyFactoryBean.getObject();
    }

    public boolean notify(LionMapDTO lionMapDTO, NotifyType notifyType) {
        if (notifyType == NotifyType.INSERT) {
            return lionListener.insert(lionMapDTO);
        } else if (notifyType == NotifyType.DELETE) {
            return lionListener.delete(lionMapDTO);
        } else if (notifyType == NotifyType.UPDATE) {
            return lionListener.update(lionMapDTO);
        }
        return false;
    }

    public String getUrl() {
        return url;
    }

}