package com.chenxiaojie.lion.test.lion;

import com.chenxiaojie.lion.dao.LionListenerDao;
import com.chenxiaojie.lion.entity.LionListener;
import com.chenxiaojie.lion.type.LionEnv;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by xiaojie.chen on 2015-03-04 15:44:31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath*:spring/common/appcontext-*.xml"})
public class LionListenerTest {

    @Autowired
    private LionListenerDao lionListenerDao;

    @Test
    public void testInsert() {
        LionListener lionListener = new LionListener();
        lionListener.setActive(true);
        lionListener.setEnv(LionEnv.DEVELOP.value);
        lionListener.setListenerURL("http://dasd");
        lionListener.setProjectName("lion");
        int result = lionListenerDao.insert(lionListener);
        System.out.println(result);
    }

    @Test
    public void testUpdateActiveByID() {
        System.out.println(lionListenerDao.updateActiveByID(false, 6));
    }


    @Test
    public void testUpdateValueByKeyProjectAndEnv() {
        System.out.println(lionListenerDao.updateActiveByProjectAndEnvAndURL(false, "lion", LionEnv.DEVELOP.value, "http://dasd"));
    }


    @Test
    public void testReplace() {
        LionListener lionListener = new LionListener();
        lionListener.setActive(true);
        lionListener.setEnv(LionEnv.DEVELOP.value);
        lionListener.setListenerURL("http://dasd");
        lionListener.setProjectName("lion");
        int result = lionListenerDao.replace(lionListener);
        System.out.println(result);
    }

    @Test
    public void testGetByProjectAndEnvAndURL() {
        LionListener lionListener = lionListenerDao.getByProjectAndEnvAndURL("lion", LionEnv.DEVELOP.value, "http://dasd");
        System.out.println(lionListener);
    }

    @Test
    public void testGetActiveByProjectAndEnv() {
        List<LionListener> lionListenerList = lionListenerDao.getActiveByProjectAndEnv("lion", LionEnv.DEVELOP.value);
        for (LionListener lionListener : lionListenerList)
            System.out.println(lionListener);
    }


}
