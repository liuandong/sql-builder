package com.jfinal.model;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 合并配置
 * 主表和外表需要绑定时用到的配置
 * Created by liuandong on 2017/6/9.
 */
public class MergeConfig implements Cloneable{
    private String fk; // 主表的外键
    private String pk; // 附表的主键
    private String[] columns; // 附表要列出的列名
    Map<String,String> aliasName = new Hashtable<>(); //附表合并后的别名

    public MergeConfig(String fk, String pk){
        this.fk = fk;
        this.pk = pk;
    }

    public MergeConfig(String fk){
        this.fk = fk;
        this.pk = "id";
    }

    public void setFk(String fk) {
        this.fk = fk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getFk() {
        return fk;
    }

    public String getPk() {
        return pk;
    }

    /**
     * 拿出所有的列名，如果没有设置column会从alias里面拿
     * 不管怎样主键都会放进去
     * @return 列数组
     */
    public String[] getColumns() {
        Set<String> columnsSet = new HashSet<>();
        //columnsSet.add(pk);
        if(columns == null){
            columnsSet.addAll(aliasName.keySet());
        }else {
            for(String c : columns) columnsSet.add(c);
        }

        return columnsSet.stream()
                .collect(Collectors.toList())
                .toArray(new String[columnsSet.size()]);
    }

    public MergeConfig setColumns(String... columns) {
        this.columns = columns;
        return this;
    }

    public Map<String, String> getAliasName() {
        return aliasName;
    }

    public MergeConfig setAliasName(Map<String, String> aliasName) {
        this.aliasName = aliasName;
        return this;
    }

    public MergeConfig setAlias(String originalName, String newName) {
        aliasName.put(originalName, newName);
        return this;
    }

    /**
     * 克隆一个配置
     * @return 配置
     */
    public MergeConfig clone(){
        return new MergeConfig(this.getFk(),this.getPk())
                .setColumns(this.columns)
                .setAliasName(this.aliasName);
    }


}
