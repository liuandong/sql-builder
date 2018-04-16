package com.jfinal.model;


import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 可以对数据集进行链式操作。
 * Created by liuandong on 2017/6/14.
 */
public class MergeUtil {
    private List<?> data;

    private MergeUtil(List<?> data){
        this.data = data;
    }

    /**
     * 选定数据源
     * @param data 数据源
     * @return MergeUtil
     */
    public static MergeUtil list(List<?> data){
        return new MergeUtil(data);
    }


    /**
     * 通过dao绑定数据
     * @param data 数据源
     * @param dao dao对象
     * @param key 键
     * @return MergeUtil
     */
    public static MergeUtil merge(List<?> data, Model dao, String key){
        return new MergeUtil(data).merge(dao,key);
    }

    /**
     * 通过dao绑定数据
     * @param data 数据源
     * @param dao dao对象
     * @param config 合并配置
     * @return MergeUtil
     */
    public static MergeUtil merge(List<?> data, Model dao, MergeConfig config){
        return new MergeUtil(data).merge(dao,config);
    }

    /**
     * 通过dao绑定数据
     * @param data 数据源
     * @param dao dao对象
     * @return MergeUtil
     */
    public static MergeUtil merge(List<?> data,Model dao){
        return new MergeUtil(data).merge(dao,(String) null);
    }

    /**
     * 通过dao绑定数据
     * @param dao dao对象
     * @param key 外键
     * @return MergeUtil
     */
    public MergeUtil merge(Model dao, String key){
        if(dao instanceof iAssociation){
            MergeConfig config = ((iAssociation)dao).association(key);
            merge(this.data ,dao.getClass(), config);
        }else {
            System.out.println("请对" + dao.getClass() + "继承iAssociation接口！");
        }
        return this;
    }

    /**
     * 通过dao绑定数据（从dao中取关联配置）
     * @param dao dao对象
     * @return MergeUtil
     */
    public MergeUtil merge(Model dao){
        if(dao instanceof iAssociation){
            MergeConfig config = ((iAssociation)dao).association(null);
            merge(this.data ,dao.getClass(), config);
        }else {
            System.out.println("请对" + dao.getClass() + "继承iAssociation接口！");
        }
        return this;
    }

    /**
     * 通过dao绑定数据
     * @param dao dao对象
     * @param config 绑定配置
     * @return MergeUtil
     */
    public MergeUtil merge(Model dao, MergeConfig config){
        merge(this.data ,dao.getClass(), config);
        return this;
    }

    /**
     * 绑定一组时间间距
     * @param key 时间戳的键名
     * @param timeKey 时间间距的键名
     * @param endStr 时间间距的后缀
     * @return MergeUtil
     */
    public MergeUtil mergeDistanceTime(String key, String timeKey, String endStr){
        AddDistanceTime(this.data,key, timeKey, endStr);
        return this;
    }

    /**
     * 删除一些列
     * @param columns 列的键名
     * @return MergeUtil
     */
    public MergeUtil removeColumn(String... columns){
        removeColumn(data,columns);
        return this;
    }

    /**
     * 合并一个map对象
     * @param key 主表的外键
     * @param newKey map绑定进的新键名
     * @param map 需要绑定的map
     * @return MergeUtil
     */
    public MergeUtil mergeMap(String key, String newKey,Map<Object,Object> map){
        mergeMap(this.data,map,key,newKey);
        return this;
    }

    /**
     * 自动绑定
     * @return MergeUtil
     */
    public MergeUtil autoBind(){
        autoBind(data);
        return this;
    }

    /**
     * 替换一个列名
     * @param valueKey 旧的键名
     * @param replaceKey 新的键名
     * @return MergeUtil
     */
    public MergeUtil replace(String valueKey, String replaceKey){
        replace(data,valueKey,replaceKey);
        return this;
    }


    //############### 合并数据具体方法 ##################

    /**
     * 对一个model，record对象通过外键进行数据合并
     * @param data 合并的数据
     * @param modelClass 要合并model的class
     * @param config 合并配置
     */
    public static void merge(List<?> data, Class<? extends Model> modelClass, MergeConfig config){
        Set<Object> ids = _getIds(data,config.getFk());
        String tableName = TableMapping.me().getTable(modelClass).getName();
        if (ids == null || ids.size()==0) return;
        Map<String,Record> map = _getTabMap(tableName,config, ids);

        _mergeModelOrRecordAndRecordMap(data,config.getFk(),map,config.getColumns(),config.getAliasName());
    }

    /**
     * 合并一个MAP
     * @param data 数据源
     * @param map map对象
     * @param pidKey 数据源的外键名
     * @param mergeKey 合并后的新键名
     */
    public static void mergeMap(List<?> data, Map<Object,Object> map, String pidKey, String mergeKey){
        for(int i = 0; i<data.size(); i++){
            _setRename(data.get(i), mergeKey, map.get(_get(data.get(i), pidKey)));
        }
    }

    /**
     * 替换一个键名
     * @param data 数据
     * @param valueKey 需要替换的键名
     * @param replaceKey 被替换的键名
     */
    public static void replace(List<?> data,String valueKey, String replaceKey){
        data.forEach(d -> {
            _set(d,replaceKey,_get(d,valueKey));
            _remove(d,valueKey);
        });
    }

    /**
     * 删除掉列
     * @param data 数据
     * @param column 需要删除的列名
     */
    public static void removeColumn(List<?> data, String... column){
        for(int i = 0; i<data.size(); i++){
            for(String col : column){
                _remove(data.get(i),col);
            }
        }
    }

    private static Set<Object> _getIds(List<?> data, String key){
        Set<Object> ids = new HashSet<>();
        int columnCount = 0;
        for (Object obj: data) {
            if (obj instanceof Record){
                Record record = (Record) obj;
                if (record.get(key) != null && StrKit.notBlank(record.get(key).toString())){
                    ids.add(record.get(key));
                    columnCount++;
                }
            }else if(obj instanceof Model){
                Model model = (Model) obj;
                if (model.get(key) != null && StrKit.notBlank(model.get(key).toString())){
                    ids.add(model.get(key));
                    columnCount++;
                }
            }
        }
        return columnCount > 0 ? ids : null;
    }

    private static Long _getLongId(Object obj, String key){
        if (obj instanceof Record && ((Record) obj).get(key) != null){
            Record record = (Record) obj;
            if (record.get(key) != null && StrKit.notBlank(record.get(key).toString())){
                return new Long(record.get(key).toString());
            }
        }else if(obj instanceof Model && ((Model) obj).get(key) != null){
            Model model = (Model) obj;
            if (model.get(key) != null && StrKit.notBlank(model.get(key).toString())){
                return new Long(model.get(key).toString());
            }
        }
        return null;
    }

    private static Object _get(Object obj, String column){
        if (obj instanceof Record){
            Record record = (Record) obj;
            return record.get(column);
        }else if(obj instanceof Model){
            Model model = (Model) obj;
            return model.get(column);
        }
        return null;
    }

    private static void  _remove(Object obj, String column){
        if (obj instanceof Record){
            Record record = (Record) obj;
            record.remove(column);
        }else if(obj instanceof Model){
            Model model = (Model) obj;
            model.remove(column);
        }
    }

    private static void _set(Object obj, String column, Object value){
        if (obj instanceof Record){
            Record record = (Record) obj;
            record.set(column,value);
        }else if(obj instanceof Model){
            Model model = (Model) obj;
            model.put(column,value);
        }
    }

    private static void _setRename(Object obj, String column, Object value){
        if (obj instanceof Record){
            Record record = (Record) obj;
            int index = 2;
            String columnName = column;
            while (record.get(columnName)!=null){
                columnName = column + index++;
            }
            record.set(columnName, value);
        }else if(obj instanceof Model){
            Model model = (Model) obj;
            int index = 2;
            String columnName = column;
            while (model.get(columnName)!=null){
                columnName = column + index++;
            }
            model.put(columnName, value);
        }
    }

    private static Map<String,Record> _getTabMap(String tableName, MergeConfig config, Set<Object> ids){
        StringJoiner sj = new StringJoiner(",");
        sj.add("`" + config.getPk() + "`");
        for (String c : config.getColumns()) sj.add("`" + c + "`");

        String sql = "SELECT "+ sj +" FROM " + tableName +
                " WHERE " + config.getPk() + " in(" +
                ArrayStrUtil.join(ids)
                +") ";

        return Db.find(sql).stream()
                .collect(Collectors.toMap(
                        record -> record.get(config.getPk()).toString(),
                        record -> record
                ));
    }

    /**
     * 对两个model数据合并
     * @param data 主数据
     * @param data2 要合并的数据
     * @param config 配置
     */
    public static void mergeData(List<?> data, List<?> data2, MergeConfig config){
        Map<String,Record> map = new HashMap<>();

        data2.forEach(model ->{
            Object keyObj = _get(model,config.getPk());
            if(keyObj!=null)
                map.put(keyObj.toString(), model instanceof Model ?((Model)model).toRecord():(Record) model);
        });

        _mergeModelOrRecordAndRecordMap(data,config.getFk(),map,config.getColumns(),config.getAliasName());
    }

    private static void _mergeModelOrRecordAndRecordMap(List<?> data, String pidKey, Map<String,Record> map, String[] columns, Map<String,String> aliasName){

        if (columns == null || columns.length==0){
            System.err.println("!!!" + "请设置要绑定的列 "+ pidKey);
            return;
        }

        for(int i = 0; i<data.size(); i++){

            String tempValue = Optional.ofNullable(_get(data.get(i), pidKey))
                    .map(o -> o.toString())
                    .orElse(null);

            if (tempValue == null) {
                for (String col: columns) {
                    _setRename(
                            data.get(i),
                            Optional.ofNullable(aliasName).map(a->a.get(col)).orElse(col),
                            null
                    );
                }
                continue;
            }

            Optional<Record> recordOp = Optional.ofNullable(map.get(tempValue));
            for (String col: columns) {
                if(aliasName!=null && aliasName.containsKey(col)){
                    _set(
                            data.get(i),
                            aliasName.get(col),
                            recordOp.map(r -> r.get(col)).orElse(null)
                    );
                }else{
                    _setRename(
                            data.get(i),
                            col,
                            recordOp.map(r -> r.get(col)).orElse(null));
                }
            }
        }
    }

    /**
     * 增加一个时间间距
     * @param list 数据
     * @param key 时间戳的键
     * @param timeKey 新的时间键名
     * @param endStr 尾部字符串
     */
    public static void AddDistanceTime(List<?> list, String key, String timeKey, String endStr){
        long now = TimeUtil.getNow();
        for (int i = 0; i<list.size(); i++){
            Object obj = list.get(i);
            Long time = _getLongId(obj, key);
            if(time != null){
                String distance = TimeUtil.getDistanceTimeStr(time,now);
                _set(obj, timeKey, distance+endStr);
            }else {
                _set(obj, timeKey, "");
            }
        }
    }

    /**
     * 自动绑定
     * @param data model的数组
     */
    public static void autoBind(List<?> data){
        if(data.size() > 0)
            if(data.get(0) instanceof iAutoBind && data.get(0) instanceof Model){
                iAutoBind bind = (iAutoBind) data.get(0);
                Model model = (Model) data.get(0);
                for (String name : model._getAttrNames()){
                    bind.bind(data,name);
                }
            }else{
                System.out.println(data.get(0).getClass() + "需要绑定iAutoBind接口！");
            }
    }

    /**
     * 自动绑定model
     * @param model 单个模型对象
     */
    public static void autoBind(Model model){
        if (model!=null){
            List<Model> resultArray = new ArrayList<>();
            resultArray.add(model);
            autoBind(resultArray);
        }
    }
}
