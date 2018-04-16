package com.jfinal.model;

import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 普通列表项目字符串\数组 互转
 * Created by liuandong on 2017/6/14.
 */
public class ArrayStrUtil {
    public static Set<Long> str2LongSet(String ids){
        if (StrKit.isBlank(ids)) return Collections.emptySet();
        return Arrays.stream(ids.split(","))
                .mapToLong(Long::valueOf)
                .boxed()
                .collect(Collectors.toSet());
    }

    public static Set<Integer> str2IntegerSet(String ids){
        if (StrKit.isBlank(ids)) return Collections.emptySet();
        return Arrays.stream(ids.split(","))
                .mapToInt(Integer::valueOf)
                .boxed()
                .collect(Collectors.toSet());
    }

    public static int[] str2IntArray(String ids){
        if (StrKit.isBlank(ids)) return null;
        return Arrays.stream(ids.split(","))
                .mapToInt(Integer::valueOf)
                .toArray();
    }

    public static long[] str2LongArray(String ids){
        if (StrKit.isBlank(ids)) return null;
        return Arrays.stream(ids.split(","))
                .mapToLong(Long::valueOf)
                .toArray();
    }

    /**
     * Collection 或者 array 转成 1，2，3 "a","b","c"
     * @param obj
     * @return
     */
    public static String join(Object obj){
        if (obj instanceof Collection || obj.getClass().isArray()){
            String result = JsonKit.toJson(obj);
            if (StrKit.notBlank(result) && result.length()>=3){
                return result.substring(1,result.length()-1);
            }
        }
        return null;
    }

    public static String join(String[] pair, String bothSideStr){
        return Arrays.stream(pair)
                .map(str -> bothSideStr + str + bothSideStr)
                .collect(Collectors.joining(","));
    }

    public static List<String> array2ArrayList(Object pair){
        List<String> l = null;
        if ( pair instanceof int[]){
            l = Arrays.stream( (int[]) pair)
                    .boxed()
                    .map(i->i.toString())
                    .collect(Collectors.toList());
        }else if ( pair instanceof long[]){
            long [] arr = ( long[] ) pair;
            l = Arrays.stream( (long[]) pair)
                    .boxed()
                    .map(i->i.toString())
                    .collect(Collectors.toList());
        }else if ( pair instanceof Set){
            Set<?> set = ( Set<?> ) pair;
            l = set.stream()
                    .map(str->str.toString())
                    .collect(Collectors.toList());
        }
        return l;
    }

}
