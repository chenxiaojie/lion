package com.chenxiaojie.lion.test.spring;

import com.chenxiaojie.lion.test.entity.Person;
import com.chenxiaojie.lion.test.placeholder.LionPlaceholder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Administrator on 2015/3/3.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath*:test/spring/common/appcontext-test.xml"})
public class PlaceholderTest {

    @Autowired
    private Person person;

    @Autowired
    private LionPlaceholder lionPlaceholder;

    @Test
    public void testPlaceholder() {
        System.out.println(person.getName() + ":" + person.getAge() + ":" + person.getPhoneNum());
        System.out.println(lionPlaceholder.getProperty("name", "chenxiaojie") + ":"
                + lionPlaceholder.getProperty("age", "25") + ":"
                + lionPlaceholder.getProperty("lion", "娃哈哈"));
        System.out.println(lionPlaceholder.getProperty("name2", "chenxiaojie") + ":"
                + lionPlaceholder.getProperty("age2", "25") + ":"
                + lionPlaceholder.getProperty("lion2", "娃哈哈"));
    }
}
