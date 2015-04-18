package com.chenxiaojie.lion.test.transformer;

import com.chenxiaojie.lion.biz.LionMapBiz;
import com.chenxiaojie.lion.dto.LionMapDTO;
import com.chenxiaojie.lion.entity.LionMap;
import com.chenxiaojie.lion.transformer.LionMapDTOTransformer;
import com.chenxiaojie.lion.type.LionEnv;
import com.google.common.collect.Lists;
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
public class LionMapTest {

    @Autowired
    private LionMapBiz lionMapBiz;

    @Test
    public void testGetAll() {
        List<LionMap> lionMaps = lionMapBiz.getByProjectAndEnv("lion", LionEnv.DEVELOP.value);
        List<LionMapDTO> lionMapDTOs = Lists.transform(lionMaps, LionMapDTOTransformer.INSTANCE);
        for (LionMapDTO lionMapDTO : lionMapDTOs) {
            System.out.println(lionMapDTO);
        }
//        for (LionMap lionMap : lionMaps) {
//            System.out.println(LionMapDTOTransformer.INSTANCE.apply(lionMap));
//        }
    }

}
