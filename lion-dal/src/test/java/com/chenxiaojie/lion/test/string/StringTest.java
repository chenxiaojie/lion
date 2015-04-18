package com.chenxiaojie.lion.test.string;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by xiaojie.chen on 2015-03-05 10:28:56.
 */
public class StringTest {

    private final static Set<String> offlineCodeSet = new HashSet(
            Arrays.asList("0005,0010,0012,0013,0014,0016,0018".split(",")
            ));

    public static void main(String[] args) {
        for (String str : offlineCodeSet) {
            System.out.println(str);
        }
        System.out.println(Arrays.toString(new int[]{1, 2, 3}));
    }
}
