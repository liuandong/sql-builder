sqlbuild是一个数据集工具集合，它可以快速调用sql语句，简化Controller参数，拼表，自动拼表等功能 其中包括了5个子集

- ArrayStrUtil 把逗号分隔字符串和数组互转
- FormFilter 对表单的值进行封装
- iAssociation 关联的接口
- iAutoBind 自动绑定的接口
- MergeConfig 合并数据集的配置
- MergeData 合并数据
- MergeUtil 优化合并数据的使用方式的类
- SqlBuilder 快速调用sql

1.ArrayStrUtil
---------------------
普通列表项目字符串转数组
如1,2,3 转成 [1,2,3] 数组类型支持常用的int[] long[] Set<Long> ArrayList<String>

数组转字符串
以上支持的数组类型重新变成1,2,3 这样的字符串

2.FormFilter
---------------------
封装表单的参数，继承了HashMap<String,String> 方便在多参数进行列表查询时，controller向service传递参数时不需要写过多参数 注意：不要在非列表业务中传递该对象。

编辑

3.iAssociation
---------------------
(自动绑定相关)关联接口，给model继承，可以在别的对象关联它的时候给出默认的MergeConfig合并配置

4.iAutoBind
---------------------
(自动绑定相关)自动绑定接口，给model继承，可以在该对象需要自动绑定的时候去依次绑定相关的数据

5.MergeConfig
---------------------
主表和外表需要绑定时用到的配置 共有以下参数

fk 主表的外键（初始化参数）
pk 附表的主键
columns 附表要列出的列名
aliasName 附表合并后的别名

6.MergeData
---------------------
对一个list<model|record>进行数据合并 支持合并的类型

另一个先有的list<model|record>，需要通过一个MergeConfig配置
另一个数据库表中数据，需要通过一个MergeConfig配置
另一个MAP数据
对源数据自动绑定
注：如果原来的数据中有name，绑定数据中的字段也有name，那么绑定后的name会被重定义成name2 依次类推，继续绑定会有name3 name4。该特性为了避免绑定相同的字段，数据被覆盖。 开发劲量不要用到该特性 劲量使用MergeConfig的aliasname对字段设置别名。

该类提供了删除字段，修改字段名等操作。

7.MergeUtil
---------------------
对以上类进行封装，可以对数据集进行链式操作。

8.SqlBuilder
---------------------
快速调出sql数据集的主类 如下：

```
      return SqlBuilder.dao(Alarm.dao) //选择对应的dao
              .where(Alarm._organization_fk, "in", Service.getChildrenIdsSet(orgId)) //where条件
              .where(Alarm._add_time, ">", filter.getInt("start_time"))
              .where(Alarm._add_time, "<", filter.getInt("end_time"))
              .where(Alarm._status, filter.getInt("status"))
              .where(Alarm._type, filter.getInt("type"))
              .where(Alarm._level, filter.getInt("level"))
              .where(Alarm._resident_nature_type, filter.getInt("resident_nature_type"))
              .orderBy(Alarm._add_time, "desc")//order条件
              .allPage(filter);
```
              
注意： 条件为null的时候会自动忽略该条件语句
