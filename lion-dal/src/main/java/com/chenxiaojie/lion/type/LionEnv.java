package com.chenxiaojie.lion.type;

/**
 * Created by xiaojie.chen on 2015-03-24 09:50:41.
 */
public enum LionEnv {
    DEVELOP(1),
    ALPHA(2),
    BETA(3),
    PPE(4),
    ONLINE(5),
    PERFORMANCE(6);

    public final int value;

    private LionEnv(int value) {
        this.value = value;
    }
}
