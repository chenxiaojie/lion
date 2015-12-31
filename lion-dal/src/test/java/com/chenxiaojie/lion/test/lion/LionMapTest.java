package com.chenxiaojie.lion.test.lion;

import com.chenxiaojie.lion.dao.LionMapDao;
import com.chenxiaojie.lion.entity.LionMap;
import com.chenxiaojie.lion.type.LionEnv;
import com.chenxiaojie.lion.utils.LionEnvUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * Created by xiaojie.chen on 2015-03-04 15:44:31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath*:spring/common/appcontext-*.xml"})
public class LionMapTest {

    @Autowired
    private LionMapDao lionMapDao;

    @Test
    public void testInsert() {
//        LionMap lionMap = LionMap.builder().mapKey("male").mapValue("陈孝杰").lazy(false).projectName("lion").build();
//        LionMap lionMap2 = LionMap.builder().mapKey("female").mapValue("冬冬").lazy(true).projectName("lion").build();
        LionMap lionMap = new LionMap();
        lionMap.setMapKey("male");
        lionMap.setMapValue("陈孝杰");
        lionMap.setLazy(false);
        lionMap.setProjectName("lion");
        lionMap.setEnv(LionEnv.DEVELOP.value);
        LionMap lionMap3 = new LionMap();
        lionMap3.setMapKey("male");
        lionMap3.setMapValue("陈孝杰3");
        lionMap3.setLazy(true);
        lionMap3.setProjectName("lion");
        lionMap3.setEnv(LionEnv.BETA.value);

        LionMap lionMap2 = new LionMap();
        lionMap2.setMapKey("female");
        lionMap2.setMapValue("冬冬");
        lionMap2.setLazy(true);
        lionMap2.setProjectName("lion");
        lionMap2.setEnv(LionEnv.DEVELOP.value);


        int result = lionMapDao.insert(lionMap);
        int result2 = lionMapDao.insert(lionMap2);
        int result3 = lionMapDao.insert(lionMap3);
        System.out.println(result);
        System.out.println(result2);
        System.out.println(result3);
        System.out.println(lionMap.getId());
        System.out.println(lionMap2.getId());
        System.out.println(lionMap3.getId());
    }

    @Test
    public void testInserts() {
//        LionMap lionMap = LionMap.builder().mapKey("male").mapValue("陈孝杰").lazy(false).projectName("lion").build();
//        LionMap lionMap2 = LionMap.builder().mapKey("female").mapValue("冬冬").lazy(true).projectName("lion").build();
        LionMap lionMap = new LionMap();
        lionMap.setMapKey("male");
        lionMap.setMapValue("陈孝杰");
        lionMap.setLazy(false);
        lionMap.setProjectName("lion");
        lionMap.setEnv(LionEnv.DEVELOP.value);
        LionMap lionMap3 = new LionMap();
        lionMap3.setMapKey("male");
        lionMap3.setMapValue("陈孝杰3");
        lionMap3.setLazy(true);
        lionMap3.setProjectName("lion");
        lionMap3.setEnv(LionEnv.BETA.value);

        LionMap lionMap2 = new LionMap();
        lionMap2.setMapKey("female");
        lionMap2.setMapValue("冬冬");
        lionMap2.setLazy(true);
        lionMap2.setProjectName("lion");
        lionMap2.setEnv(LionEnv.DEVELOP.value);

        List<LionMap> lionMapList = Lists.newArrayList();
        lionMapList.add(lionMap);
        lionMapList.add(lionMap2);
        lionMapList.add(lionMap3);


        int result = lionMapDao.inserts(lionMapList);
        System.out.println(result);
        for (LionMap l : lionMapList) {
            System.out.println(l.getId());
        }
    }


    @Test
    public void testDeleteById() {
        int result = lionMapDao.deleteByID(4);
        System.out.println(result);
    }

    @Test
    public void testDeleteByKeyProjectAndEnv() {
        int result = lionMapDao.deleteByKeyProjectAndEnv("xxx", "lion-client", LionEnv.BETA.value);
        System.out.println(result);
    }

    @Test
    public void testDeleteByKeyProjectAndEnvs() {
        int result = lionMapDao.deleteByKeyProjectAndEnvs("xxx", "lion-client", LionEnvUtils.All_Lion_Env_Values);
        System.out.println(result);
    }

    @Test
    public void testUpdate() {
        LionMap lionMap = new LionMap();
        lionMap.setId(134);
        lionMap.setMapKey("male");
        lionMap.setMapValue("陈孝杰");
        lionMap.setLazy(false);
        lionMap.setProjectName("lion");
        lionMap.setEnv(LionEnv.ONLINE.value);
        System.out.println(lionMapDao.update(lionMap));
    }

    @Test
    public void testUpdates() {
        LionMap lionMap = new LionMap();
        lionMap.setMapKey("陈孝杰");
        lionMap.setMapValue("haah");
        lionMap.setLazy(false);
        lionMap.setProjectName("lion-client");
        System.out.println(lionMapDao.updates(lionMap, LionEnvUtils.All_Lion_Env_Values));
    }

    @Test
    public void testUpdateValueByKeyProjectAndEnv() {
        System.out.println(lionMapDao.updateValueByKeyProjectAndEnv("娃哈哈", "xxxxxxxxxxx", "lion", LionEnv.DEVELOP.value));
    }

    @Test
    public void testUpdateValueByKeyProjectAndEnvs() {
        System.out.println(lionMapDao.updateValueByKeyProjectAndEnvs("娃哈哈", "male", "lion",
                new int[]{1, 3}));
    }


    @Test
    public void testReplace() {
        LionMap lionMap = new LionMap();
        lionMap.setMapKey("邹冬冬");
        lionMap.setMapValue("陈孝杰");
        lionMap.setLazy(false);
        lionMap.setProjectName("pigeon");
        lionMap.setEnv(LionEnv.ONLINE.value);
        System.out.println(lionMapDao.replace(lionMap));
    }

    @Test
    public void testGetById() {
        LionMap lionMap = lionMapDao.getByID(105);
        System.out.println(lionMap);
    }

    @Test
    public void testGetByKeyProjectAndEnv() {
//        Map<String, String> params = Maps.newHashMap();
//        params.put("mapKey", "male");
//        params.put("projectName", "lion");
//        LionMap lionMap = lionMapDao.getByKeyAndProjectName(params);


        Map<String, String> params = Maps.newHashMap();
        LionMap lionMap = lionMapDao.getByKeyProjectAndEnv("male", "lion", LionEnv.DEVELOP.value);
        System.out.println(lionMap);
    }

    @Test
    public void testGetByKeyProjectAndEnvs() {
        List<LionMap> lionMaps = lionMapDao.getByKeyProjectAndEnvs("male", "lion", new int[]{1, 3, 5});
        for (LionMap lionMap : lionMaps) {
            System.out.println(lionMap);
        }
    }

    @Test
    public void testGetByProjectAndEnv() {
        List<LionMap> lionMaps = lionMapDao.getByProjectAndEnv("lion", LionEnv.DEVELOP.value);
        for (LionMap lionMap : lionMaps) {
            System.out.println(lionMap);
        }
    }

    @Test
    public void testGetPageByProjectAndEnv() {
        List<LionMap> lionMaps = lionMapDao.getPageByProjectAndEnv("lion", LionEnv.DEVELOP.value, 0, 10);
        for (LionMap lionMap : lionMaps) {
            System.out.println(lionMap);
        }
    }

    @Test
    public void testGetCountByProjectAndEnv() {
        int count = lionMapDao.getCountByProjectAndEnv("lion", LionEnv.DEVELOP.value);
        System.out.println(count);
    }

    @Test
    public void testGetAllProjectNames() {
        List<String> names = lionMapDao.getAllProjectNames();
        System.out.println(names);
    }

    @Test
    public void testGetAll() {
        List<LionMap> lionMaps = lionMapDao.getAll();
        for (LionMap lionMap : lionMaps) {
            System.out.println(lionMap);
        }
    }

}
