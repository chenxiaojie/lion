package com.chenxiaojie.lion.dao;

import com.chenxiaojie.lion.entity.LionMap;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by xiaojie.chen on 2015-03-04 15:39:38.
 */
public interface LionMapDao {

    //@Select("select id,mapKey,mapValue,lazy,projectName from lion_map where mapKey=#{mapKey} and projectName=#{projectName}")
    //public LionMap getByKeyAndProjectName(Map<String, String> params);


    //@SelectKey(before = false, keyProperty = "id", resultType = int.class, statementType = StatementType.STATEMENT, statement = "SELECT LAST_INSERT_ID() AS id")
    @Insert("insert into lion_map(mapKey,mapValue,lazy,projectName,env) values(#{mapKey},#{mapValue},#{lazy},#{projectName},#{env})")
    @SelectKey(before = false, resultType = int.class, keyProperty = "id", statement = "select @@IDENTITY as id")
    public int insert(LionMap lionMap);

    @Insert("<script>" +
            "insert into lion_map(mapKey,mapValue,lazy,projectName,env) values" +
            "<foreach collection='lionMapList' item='lionMap' separator=','>" +
            "(#{lionMap.mapKey},#{lionMap.mapValue},#{lionMap.lazy},#{lionMap.projectName},#{lionMap.env})" +
            "</foreach>" +
            "</script>")
    public int inserts(@Param("lionMapList") List<LionMap> lionMapList);

    @Delete("delete from lion_map where id=#{id}")
    public int deleteByID(@Param("id") int id);

    @Delete("delete from lion_map where mapKey=#{mapKey} and projectName=#{projectName} and env=#{env}")
    public int deleteByKeyProjectAndEnv(@Param("mapKey") String mapKey, @Param("projectName") String projectName, @Param("env") int env);

    @Delete("<script>" +
            "delete from lion_map where mapKey=#{mapKey} and projectName=#{projectName} and env in " +
            "<foreach collection='envs' item='env' open='(' separator=',' close=')'>" +
            "#{env}" +
            "</foreach>" +
            "</script>")
    public int deleteByKeyProjectAndEnvs(@Param("mapKey") String mapKey, @Param("projectName") String projectName, @Param("envs") int[] envs);

    @Update("update lion_map set mapKey=#{mapKey},mapValue=#{mapValue},lazy=#{lazy},projectName=#{projectName},env=#{env} where id=#{id}")
    public int update(LionMap lionMap);

    @Update("<script>" +
            "update lion_map set mapValue=#{lionMap.mapValue},lazy=#{lionMap.lazy} where mapKey=#{lionMap.mapKey} and projectName=#{lionMap.projectName} and env in " +
            "<foreach collection='envs' item='env' open='(' separator=',' close=')'>" +
            "#{env}" +
            "</foreach>" +
            "</script>")
    public int updates(@Param("lionMap") LionMap lionMap, @Param("envs") int[] envs);

    @Update("update lion_map set mapValue=#{mapValue} where mapKey=#{mapKey} and projectName=#{projectName} and env=#{env}")
    public int updateValueByKeyProjectAndEnv(@Param("mapValue") String mapValue, @Param("mapKey") String mapKey, @Param("projectName") String projectName, @Param("env") int env);

    @Update("<script>" +
            "update lion_map set mapValue=#{mapValue} where mapKey=#{mapKey} and projectName=#{projectName} and env in " +
            "<foreach collection='envs' item='env' open='(' separator=',' close=')'>" +
            "#{env}" +
            "</foreach>" +
            "</script>")
    public int updateValueByKeyProjectAndEnvs(@Param("mapValue") String mapValue, @Param("mapKey") String mapKey, @Param("projectName") String projectName, @Param("envs") int[] envs);

    @Update("replace into lion_map(mapKey,mapValue,lazy,projectName,env) values(#{mapKey},#{mapValue},#{lazy},#{projectName},#{env})")
    public int replace(LionMap lionMap);

    @Select("select id,mapKey,mapValue,lazy,projectName,env from lion_map where id=#{id}")
    public LionMap getByID(@Param("id") int id);

    @Select("select id,mapKey,mapValue,lazy,projectName,env from lion_map where mapKey=#{mapKey} and projectName=#{projectName} and env=#{env}")
    public LionMap getByKeyProjectAndEnv(@Param("mapKey") String mapKey, @Param("projectName") String projectName, @Param("env") int env);

    @Select("<script>" +
            "select id,mapKey,mapValue,lazy,projectName,env from lion_map where mapKey=#{mapKey} and projectName=#{projectName} and env in " +
            "<foreach collection='envs' item='env' open='(' separator=',' close=')'>" +
            "#{env}" +
            "</foreach>" +
            "</script>")
    public List<LionMap> getByKeyProjectAndEnvs(@Param("mapKey") String mapKey, @Param("projectName") String projectName, @Param("envs") int[] envs);

    @Select("select id,mapKey,mapValue,lazy,projectName,env from lion_map where projectName=#{projectName} and env=#{env}")
    public List<LionMap> getByProjectAndEnv(@Param("projectName") String projectName, @Param("env") int env);

    @Select("select id,mapKey,mapValue,lazy,projectName,env from lion_map where projectName=#{projectName} and env=#{env} limit #{start}, #{size}")
    public List<LionMap> getPageByProjectAndEnv(@Param("projectName") String projectName, @Param("env") int env, @Param("start") int start, @Param("size") int size);

    @Select("select count(*) from lion_map where projectName=#{projectName} and env=#{env}")
    public int getCountByProjectAndEnv(@Param("projectName") String projectName, @Param("env") int env);

    @Select("select projectName from lion_map group by projectName")
    public List<String> getAllProjectNames();

    @Select("select id,mapKey,mapValue,lazy,projectName,env from lion_map")
    public List<LionMap> getAll();
}
