package com.chenxiaojie.lion.notify.listener;

import com.chenxiaojie.lion.dto.LionMapDTO;
import com.chenxiaojie.lion.type.NotifyType;
import com.chenxiaojie.lion.utils.CommonListener;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

/**
 * Created by xiaojie.chen on 2015-03-22 17:18:39.
 */
public class Listener {

    private CommonListener commonListener;

    private String url;

    public Listener(String url) {
        this.url = url;
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceUrl("rmi://" + this.url + "/notify");
        rmiProxyFactoryBean.setServiceInterface(CommonListener.class);
        rmiProxyFactoryBean.afterPropertiesSet();
        this.commonListener = (CommonListener) rmiProxyFactoryBean.getObject();
    }

    public void notify(LionMapDTO lionMapDTO, NotifyType notifyType) {
        switch (notifyType) {
            case UPDATE:
                commonListener.update(lionMapDTO);
                break;
            case INSERT:
                commonListener.insert(lionMapDTO);
                break;
            case DELETE:
                commonListener.delete(lionMapDTO);
                break;
        }
    }

    public String getUrl() {
        return url;
    }

}