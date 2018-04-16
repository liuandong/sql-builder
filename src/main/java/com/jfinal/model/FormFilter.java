package com.jfinal.model;

import com.jfinal.kit.StrKit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 表单参数封装类
 * 方便在多参数进行列表查询时，controller向service传递参数时不需要写过多参数
 * 注意：避免在非列表业务中传递该对象。
 * Created by liuandong on 2017/5/23.
 */
public class FormFilter extends HashMap<String,String> {
    private int pageSize = 10; //分页大小
    private int pageNumber = 1; //当前页面
    private String orderName;
    private String orderType = "ASC";

    public FormFilter(){}

    public FormFilter(Map<String,String[]> requestParameterMap){
        requestParameterMap.entrySet().forEach( entry -> {
            put(entry.getKey(), entry.getValue()[0]);
        });
        paraInit();
    }

    //处理 a/1/b/2的情况
    public FormFilter(String requestUrl){
        String[] list = requestUrl.split("\\/");
        if (list.length > 1){
            for(int i=0; i<list.length; i+=2){
                put(list[i],list[i+1]);
            }
        }
        paraInit();
    }

    private void paraInit(){
        //如果有页码
        if(containsKey("page_number") && StrKit.notBlank(get("page_number"))){
            pageNumber = getInt("page_number");
        }

        //如果有分页大小
        if(containsKey("page_size") && StrKit.notBlank(get("page_size"))){
            pageSize = getInt("page_size");
        }

        //如果有排序
        if(containsKey("order_name") && StrKit.notBlank(get("order_name"))){
            orderName = get("order_name");
        }

        //如果有排序类型
        if(containsKey("order_type") && StrKit.notBlank(get("order_type"))){
            orderType = get("order_type");
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getOrderName() {
        return orderName;
    }

    public FormFilter setOrderName(String orderName) {
        this.orderName = orderName;
        return this;
    }

    public String getOrderType() {
        return orderType;
    }

    public FormFilter setOrderType(String orderType) {
        this.orderType = orderType;
        return this;
    }


    public String getPara(String name) {
        return get(name);
    }

    public String getPara(String name, String defaultValue) {
        Optional<String> result = Optional.ofNullable(get(name));
        return result.orElse(defaultValue);
    }

    public String getParaExcludeAll(String name, String excludeValue) {
        Optional<String> result = Optional.ofNullable(get(name))
                .filter(str->!excludeValue.equals(str));
        return result.orElse(null);
    }


    private Integer toInt(String value, Integer defaultValue) {
        if(StrKit.isBlank(value)) {
            return defaultValue;
        } else {
            value = value.trim();
            return !value.startsWith("N") &&
                        !value.startsWith("n")?
                        Integer.valueOf(Integer.parseInt(value)):
                        Integer.valueOf(-Integer.parseInt(value.substring(1)));
        }
    }

    public Integer getInt(String name) {
        return this.toInt(get(name), (Integer)null);
    }

    public Integer getInt(String name, Integer defaultValue) {
        return this.toInt(get(name), defaultValue);
    }

    public Integer getIntExcludeAll(String name, int excludeValue) {
        Optional<Integer> result = Optional.ofNullable(getInt(name))
                .filter(i -> i != excludeValue);
        return result.orElse(null);
    }

    private Long toLong(String value, Long defaultValue) {
            if(StrKit.isBlank(value)) {
                return defaultValue;
            } else {
                value = value.trim();
                return !value.startsWith("N") &&
                        !value.startsWith("n")?
                        Long.valueOf(Long.parseLong(value)):
                        Long.valueOf(-Long.parseLong(value.substring(1)));
            }
    }

    public Long getLong(String name) {
        return this.toLong(get(name), (Long)null);
    }

    public Long getLong(String name, Long defaultValue) {
        return this.toLong(get(name), defaultValue);
    }

    public Long getLongExcludeAll(String name, long excludeValue) {
        Optional<Long> result = Optional.ofNullable(getLong(name))
                .filter(i -> i != excludeValue);
        return result.orElse(null);
    }

    public static int[] getIntIds(String ids){
        return ArrayStrUtil.str2IntArray(ids);
    }

    public static long[] getLongIds(String ids){
        return ArrayStrUtil.str2LongArray(ids);
    }
}
