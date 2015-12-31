package com.chenxiaojie.lion.entity;

import lombok.Data;

/**
 * Created by xiaojie.chen on 2015-04-21 16:54:46.
 */
@Data
public class LionListener {
    private int id;
    private String projectName;
    private String listenerURL;
    private int env;
    private boolean active;
}
