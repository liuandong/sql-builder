package com.jfinal;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import org.junit.Before;
import tpson.model._MappingKit;

/**
 * Created by liuandong on 2017/7/7.
 */
public class jfinalTest {

    @Before
    public void setUp() throws Exception {
        String url = "jdbc:mysql://127.0.0.1/tpson_standalone?serverTimezone=GMT%2b8&autoReconnect=true&failOverReadOnly=false&useUnicode=true&characterEncoding=UTF-8";
        DruidPlugin druidPlugin = new DruidPlugin(url, "root", "Liuandong123.");
        druidPlugin.setInitialSize(1);
        druidPlugin.start();

        ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(druidPlugin);
        activeRecordPlugin.setShowSql(true);
        _MappingKit.mapping(activeRecordPlugin);
        activeRecordPlugin.start();
    }
}
