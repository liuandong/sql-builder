package com.jfinal.model;

/**
 * 关联接口(自动绑定相关)，给model继承，
 * 可以在别的对象关联它的时候给出默认的MergeConfig合并配置
 * Created by liuandong on 2017/7/16.
 */
public interface iAssociation {
    /**
     * 根据一个外键给出一个默认合并配置
     * @param fk 外键名称
     * @return 合并配置
     */
    public MergeConfig association(String fk);
}
