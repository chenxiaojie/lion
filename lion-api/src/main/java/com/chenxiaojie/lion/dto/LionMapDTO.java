package com.chenxiaojie.lion.dto;

/**
 * Created by xiaojie.chen on 2015-03-04 15:35:16.
 */
public class LionMapDTO implements java.io.Serializable {
    private int id;
    private String mapKey;
    private String mapValue;
    private boolean lazy;
    private String projectName;
    private int env;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMapKey() {
        return mapKey;
    }

    public void setMapKey(String mapKey) {
        this.mapKey = mapKey;
    }

    public String getMapValue() {
        return mapValue;
    }

    public void setMapValue(String mapValue) {
        this.mapValue = mapValue;
    }

    public boolean isLazy() {
        return lazy;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getEnv() {
        return env;
    }

    public void setEnv(int env) {
        this.env = env;
    }

    @Override
    public String toString() {
        return "LionMapDTO{" +
                "id=" + id +
                ", mapKey='" + mapKey + '\'' +
                ", mapValue='" + mapValue + '\'' +
                ", lazy=" + lazy +
                ", projectName='" + projectName + '\'' +
                '}';
    }
}
