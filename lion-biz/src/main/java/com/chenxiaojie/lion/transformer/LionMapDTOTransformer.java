package com.chenxiaojie.lion.transformer;

import com.chenxiaojie.lion.dto.LionMapDTO;
import com.chenxiaojie.lion.entity.LionMap;
import com.google.common.base.Function;

/**
 * Created by xiaojie.chen on 2015-03-11 10:01:03.
 */
public class LionMapDTOTransformer implements Function<LionMap, LionMapDTO> {

    public static final LionMapDTOTransformer INSTANCE = new LionMapDTOTransformer();

    @Override
    public LionMapDTO apply(LionMap src) {
        LionMapDTO des = new LionMapDTO();
        des.setId(src.getId());
        des.setMapKey(src.getMapKey());
        des.setMapValue(src.getMapValue());
        des.setProjectName(src.getProjectName());
        des.setLazy(src.isLazy());
        des.setEnv(src.getEnv());
        return des;
    }
}
