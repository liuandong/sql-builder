package com.jfinal.model;

import java.util.List;

/**
 * 自动绑定接口(自动绑定相关)，给model继承
 * 可以在该对象需要自动绑定的时候去依次绑定相关的数据编辑
 * Created by liuandong on 2017/7/16.
 */
public interface iAutoBind {
    /**
     * 绑定数据
     * @param data 数据源
     * @param columnName 列名
     */
    public void bind(List<?> data, String columnName);
}
