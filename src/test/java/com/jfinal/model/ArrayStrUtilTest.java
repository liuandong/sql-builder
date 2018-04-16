package com.jfinal.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by liuandong on 2017/11/9.
 */
public class ArrayStrUtilTest {
    @Test
    public void joinStr() throws Exception {
        String[] testArr = new String[]{"1","2","3","1and'#and"};
        System.out.println(ArrayStrUtil.join(testArr));

        List<String> testArr2 = Arrays.asList("aaa","bbb",null,"aa*'aaa");
        System.out.println(ArrayStrUtil.join(testArr2));

        List<Integer> testArr3 = Arrays.asList(1,2,3,4,5);
        System.out.println(ArrayStrUtil.join(testArr3));
    }

    @Test
    public void joinStr1() throws Exception {

    }

}