package com.chenxiaojie.lion.type;

/**
 * Created by xiaojie.chen on 2015-03-22 16:45:30.
 */
public enum NotifyType {

    INSERT(1),
    DELETE(2),
    UPDATE(3);

    public final int value;

    private NotifyType(int value) {
        this.value = value;
    }
}