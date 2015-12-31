package com.chenxiaojie.lion.test.lion;

import com.chenxiaojie.lion.biz.LionMapBiz;
import com.chenxiaojie.lion.entity.LionMap;
import com.chenxiaojie.lion.type.LionEnv;
import com.chenxiaojie.lion.utils.LionManager;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by xiaojie.chen on 2015-03-05 15:44:31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath*:spring/common/appcontext-*.xml"})
@Slf4j
public class LionMapTest {

    @Autowired
    private LionMapBiz lionMapBiz;

    @Test
    public void testGetAll() {
        List<LionMap> lionMaps = lionMapBiz.getByProjectAndEnv("lion", LionEnv.DEVELOP.value);
        for (LionMap lionMap : lionMaps) {
            System.out.println(lionMap);
        }
//        log.info("#########娃哈哈陈孝杰");
//        System.out.println(LionUtils.getProperty("lion.driverClassName", "default"));
//        System.out.println(LionUtils.getProperty("wawa.sss", "default"));
//        System.out.println(LionUtils.getProperty("wawa.ss", "default"));
    }

    @Test
    public void testInserts() {
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


        int result = lionMapBiz.inserts(lionMapList);
        System.out.println(result);
        for (LionMap l : lionMapList) {
            System.out.println(l.getId());
        }
    }

    @Test
    public void testDeleteById() {
        //        int result = lionMapBiz.deleteByID(128);
        //        System.out.println(result);
        System.out.println(LionManager.CUR_ENV);
    }

//    @Test
//    public void testGetAll2() {
//        List<LionMapDTO> lionMaps = lionService.getAll();
//        for (LionMapDTO lionMap : lionMaps) {
//            System.out.println(lionMap);
//        }
//    }

}
