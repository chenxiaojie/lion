package com.chenxiaojie.lion.type;

/**
 * Created by xiaojie.chen on 2015-03-24 09:50:41.
 */
public enum LionEnv {
    DEVELOP(1),             //开发环境
    ALPHA(2),               //Alpha环境
    BETA(3),                //beta环境
    PPE(4),                 //预发环境
    ONLINE(5),              //线上环境
    PERFORMANCE(6);         //性能环境

    public final int value;

    private LionEnv(int value) {
        this.value = value;
    }
}
