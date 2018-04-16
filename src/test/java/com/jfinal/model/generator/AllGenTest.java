package com.jfinal.model.generator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by liuandong on 2017/12/28.
 */
public class AllGenTest {
    @Test
    public void gen() throws Exception {
        String url = "jdbc:mysql://127.0.0.1/tpson_standalone?serverTimezone=GMT%2b8&autoReconnect=true&failOverReadOnly=false&useUnicode=true&characterEncoding=UTF-8";
        String db = "tpson_standalone";
        String user = "root";
        String pass = "Liuandong123.";

        AllGen.gen(url,db,user,pass,"tp_");
    }

}