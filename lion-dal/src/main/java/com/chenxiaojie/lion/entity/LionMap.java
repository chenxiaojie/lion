package com.chenxiaojie.lion.entity;

import lombok.Data;

/**
 * Created by xiaojie.chen on 2015-03-04 15:35:16.
 */
@Data
public class LionMap {
    private int id;
    private String mapKey;
    private String mapValue;
    private boolean lazy;
    private String projectName;
    private int env;
}
