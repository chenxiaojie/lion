package com.chenxiaojie.lion.dao;

import com.chenxiaojie.lion.entity.LionListener;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by xiaojie.chen on 2015-04-21 16:52:23.
 */
public interface LionListenerDao {

    @Insert("insert into lion_listener(projectName,listenerURL,env,active) values(#{projectName},#{listenerURL},#{env},#{active})")
    @SelectKey(before = false, resultType = int.class, keyProperty = "id", statement = "select @@IDENTITY as id")
    public int insert(LionListener lionListener);

    @Update("update lion_listener set active=#{active} where id=#{id}")
    public int updateActiveByID(@Param("active") boolean active, @Param("id") int id);

    @Update("update lion_listener set active=#{active} where projectName=#{projectName} and env=#{env} and listenerURL=#{listenerURL}")
    public int updateActiveByProjectAndEnvAndURL(@Param("active") boolean active, @Param("projectName") String projectName, @Param("env") int env, @Param("listenerURL") String listenerURL);

    @Update("replace into lion_listener(projectName,listenerURL,env,active) values(#{projectName},#{listenerURL},#{env},#{active})")
    public int replace(LionListener lionListener);

    @Select("select id,projectName,listenerURL,env,active from lion_listener where projectName=#{projectName} and env=#{env} and listenerURL=#{listenerURL}")
    public LionListener getByProjectAndEnvAndURL(@Param("projectName") String projectName, @Param("env") int env, @Param("listenerURL") String listenerURL);

    @Select("select id,projectName,listenerURL,env,active from lion_listener where active=1 and projectName=#{projectName} and env=#{env}")
    public List<LionListener> getActiveByProjectAndEnv(@Param("projectName") String projectName, @Param("env") int env);

}