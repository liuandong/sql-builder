package com.jfinal.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by liuandong on 2017/11/9.
 */
public class FormFilterTest {
    @Test
    public void getParaExcludeAll() throws Exception {
        FormFilter f = new FormFilter("aa/111");
        assertEquals(f.getParaExcludeAll("aa","111"),null);
        assertEquals(f.getParaExcludeAll("aa","123"),"111");
    }

}