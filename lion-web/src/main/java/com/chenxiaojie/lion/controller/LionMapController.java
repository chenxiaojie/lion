package com.chenxiaojie.lion.controller;

import com.chenxiaojie.lion.biz.LionMapBiz;
import com.chenxiaojie.lion.entity.LionMap;
import com.chenxiaojie.lion.type.LionEnv;
import com.chenxiaojie.lion.utils.LionEnvUtils;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class LionMapController {

    @Autowired
    private LionMapBiz lionMapBiz;

    @RequestMapping("index")
    public String index(HttpServletRequest request) {
        List<String> projectNames = lionMapBiz.getAllProjectNames();
        request.setAttribute("projectNames", projectNames);
        return "page/lionmap/index";
    }

    @RequestMapping("add-project")
    public
    @ResponseBody
    boolean addProject(LionMap lionMap) {
        List<LionMap> lionMapList = lionMapBiz.getByKeyProjectAndEnvs(lionMap.getMapKey(), lionMap.getProjectName(), LionEnvUtils.All_Lion_Env_Values);
        if (lionMapList == null || lionMapList.size() == 0) {
            lionMapList = LionEnvUtils.createAllEnvLionMaps(lionMap);
            if (lionMapBiz.inserts(lionMapList) == LionEnvUtils.All_Lion_Env_Values.length) {
                return true;
            }
        }
        return false;
    }

    @RequestMapping("{projectName}/list/{env}")
    public String list(HttpServletRequest request, @PathVariable String projectName, @PathVariable Integer env, Integer pageSize, Integer curIndex) {
        if (env == null) {
            env = LionEnv.DEVELOP.value;
        }
        if (pageSize == null) {
            pageSize = 10;
        } else if (pageSize > 20) {
            pageSize = 20;
        }
        if (curIndex == null) {
            curIndex = 1;
        }
        List<LionMap> lionMaps = lionMapBiz.getPageByProjectAndEnv(projectName, env, curIndex, pageSize);
        request.setAttribute("totalCount", lionMapBiz.getCountByProjectAndEnv(projectName, env));
        request.setAttribute("lionMaps", lionMaps);
        return "page/lionmap/list";
    }

    @RequestMapping("lion/env-check")
    public
    @ResponseBody
    String envCheck(LionMap lionMap) {
        List<LionMap> lionMapList = lionMapBiz.getByKeyProjectAndEnvs(lionMap.getMapKey(), lionMap.getProjectName(), LionEnvUtils.All_Lion_Env_Values);
        if (lionMapList != null && lionMapList.size() > 0) {
            Integer[] envs = new Integer[lionMapList.size()];
            for (int i = 0; i < lionMapList.size(); i++) {
                envs[i] = lionMapList.get(i).getEnv();
            }
            return Joiner.on(',').join(envs);
        }
        return "";
    }


    @RequestMapping("lion/add")
    public
    @ResponseBody
    LionMap add(int[] envs, LionMap lionMap, int curEnv) {
        if (envs == null) {
            envs = LionEnvUtils.All_Lion_Env_Values;
        }
        try {
            if (lionMapBiz.inserts(LionEnvUtils.createEnvLionMaps(lionMap, envs)) == envs.length) {
                return lionMapBiz.getByKeyProjectAndEnv(lionMap.getMapKey(), lionMap.getProjectName(), curEnv);
            }
        } catch (Exception e) {
        }
        return null;
    }


    @RequestMapping("lion/update")
    public
    @ResponseBody
    LionMap update(int[] envs, LionMap lionMap) {
        if (envs == null) {
            envs = LionEnvUtils.All_Lion_Env_Values;
        }
        try {
            if (lionMapBiz.updates(lionMap, envs) == envs.length) {
                return lionMap;
            }
        } catch (Exception e) {
        }
        return null;
    }

    @RequestMapping("lion/delete")
    public
    @ResponseBody
    boolean delete(int[] envs, LionMap lionMap) {
        return lionMapBiz.deleteByKeyProjectAndEnvs(lionMap.getMapKey(), lionMap.getProjectName(), envs) == envs.length;
    }
}