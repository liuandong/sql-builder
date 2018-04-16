package com.jfinal.model;

import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 快速调出sql数据集的主类
 * 写法：SqlBuilder.dao(OneModel.dao).where('id',1).findFirst();
 * @param <M>
 */
public class SqlBuilder<M extends Model<?>> {

	private M _model;
	private Table _thisTable;
	private Map<String,String> _sqlStrMap;
	private Map<String,StringJoiner> _sqlListMap;
	//存储字符串拼接别名
	private static final Map<String,String> _listMapSplitStr = new HashMap<String,String>(){{
		put(WHERE,"\n\tAND ");
		put(HAVING,"\n\tAND ");
		put(SET,",");
		put(ORDER,",");
	}};

	private Map<String,List<Object>> _values;

	private static final String TABLE = "TABLE";
	private static final String COLUMN = "COLUMN";
	private static final String WHERE = "WHERE";
	private static final String SET = "SET";
	private static final String GROUP = "GROUP";
	private static final String ORDER = "ORDER BY";
	private static final String HAVING = "HAVING";
	private static final String LIMIT = "LIMIT";

	private boolean isAutoBind = false;

	public static <T extends Model<T>> SqlBuilder<T> dao(T model){
		return new SqlBuilder<T>(model);
	}
	
	public SqlBuilder(M model){
		_thisTable = TableMapping.me().getTable(model.getClass());
		_sqlStrMap = new HashMap<>();
		_sqlListMap = new HashMap<>();
		_values = new HashMap<>();
		_model = model;
		_setSqlStr(TABLE,_thisTable.getName());
	}

	protected void _setSqlStr(String key,String value) {
		_sqlStrMap.put(key, value);
	}

	protected void _setSqlList(String key,String value) {
		if (!_sqlListMap.containsKey(key)){
			_sqlListMap.put(key,new StringJoiner(_listMapSplitStr.get(key)));
		}
		_sqlListMap.get(key).add(value);
	}

	protected void _setValue(String key,Object value) {
		if (!_values.containsKey(key)){
			_values.put(key,new ArrayList<>());
		}
		_values.get(key).add(value);
	}
	
	public SqlBuilder<M> columns(String... pair){
		return paraStr(COLUMN,"`", pair);
	}

	/**
	 * 所有的列 除了exceptColumn
	 * @param exceptColumn 排除的列
	 * @return SqlBuilder
	 */
	public SqlBuilder<M> columnsExcept(String ...exceptColumn){
		Set<String> exceptSet = Stream.of(exceptColumn).collect(Collectors.toSet());

		List<String> attrArr = _thisTable.getColumnTypeMap().keySet().stream()
				.filter(attr -> !exceptSet.contains(attr))
				.collect(Collectors.toList());

		return paraStr(COLUMN,"`", attrArr.toArray(new String[attrArr.size()]));
	}

	/**
	 * a,b,c 变成 `a`,`b`,`c` a asc,b desc变成 a asc,b desc
	 * 如果hasSecondAttr
	 * @param keyName
	 * @param pair
	 * @return SqlBuilder
	 */
	protected SqlBuilder<M> paraStr(String keyName,String bothSideStr,String ...pair){
		if (pair.length == 1 && (
				pair[0].contains(" ") || pair[0].contains(",")
		)) {
			_setSqlStr(keyName,pair[0]);
		}else{
			_setSqlStr(keyName,ArrayStrUtil.join(pair, bothSideStr));
		}
		return this;
	}
	
	public SqlBuilder<M> limit(int ...limitNumber){
		if (limitNumber.length == 1){
			_setSqlStr(LIMIT, String.valueOf(limitNumber[0]));
		}else if(limitNumber.length == 2) {
			_setSqlStr(LIMIT, limitNumber[0]+","+limitNumber[1]);
		}

		return this;
	}
	
	public SqlBuilder<M> where(Object ...pair){
		return condition(WHERE,pair);
	}

    public SqlBuilder<M> where(Model model){
        for(String attr : model._getAttrNames()){
            where(attr,model.get(attr));
        }
        return this;
    }


    public SqlBuilder<M> where(Consumer<M> action){
        try {
            M object = (M) _model.getClass().newInstance();
            action.accept(object);
            return where(object);
        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }


    protected boolean checkNotNullValue(Object value){
		return value!=null && StrKit.notBlank(value.toString());
	}

	protected SqlBuilder<M> condition(String keyName,Object ...pair){
		String whereStr = _getCondition(keyName,pair);
		if(StrKit.notBlank(whereStr))
			_setSqlList(keyName, whereStr);
		return this;
	}

	protected String _getCondition(String keyName,Object ...pair){
		StringBuilder sb = new StringBuilder();
		if (pair.length == 1 && StrKit.notBlank((String) pair[0])){
			sb.append(pair[0]);
		}else if (pair.length == 2){
			if (checkNotNullValue(pair[1])){
				if(pair[1].getClass().isArray() || pair[1] instanceof Collection){
					sb.append("`" + pair[0] + "` in(" + ArrayStrUtil.join(pair[1]) +") ");
				}else{
					sb.append("`" + pair[0] + "` = ? ");
					_setValue(keyName,pair[1]);
				}
			}
		}else if (pair.length == 3){
			String centerStr = pair[1].toString().toUpperCase();
			if (checkNotNullValue(pair[2])){
				if(pair[2].getClass().isArray() || pair[2] instanceof Collection){
					sb.append("`" + pair[0] + "` "+ pair[1] + " (" + ArrayStrUtil.join(pair[2]) +") ");
				}else if(centerStr.equals("LIKE")){
					sb.append("`" +pair[0] + "` LIKE ? ");
					_setValue(keyName,"%" + pair[2] + "%");
				}else{
					sb.append("`" + pair[0] + "` "+ pair[1] +" ? ");
					_setValue(keyName,pair[2]);
				}
			}
		}
		return sb.toString() ;
	}

	/**
	 * group by语句
	 * @param pair "id","aa"
	 * @return SqlBuilder
	 */
	public SqlBuilder<M> groupBy(String... pair){
		return paraStr(GROUP,"`", pair);
	}

	/**
	 * having语句
	 * @param pair 和sql一样
	 * @return SqlBuilder
	 */
	public SqlBuilder<M> having(Object... pair){
		return condition(HAVING, pair);
	}

	public SqlBuilder<M> autoBind(){
	    isAutoBind = true;
	    return this;
    }

	/**
	 * 排序字段
	 * @param pair "id asc","name","phone desc",fromFilter
	 * @return SqlBuilder
	 */
	public SqlBuilder<M> orderBy(Object ...pair){
		for(int i=0; i<pair.length; i++){
			Object currentPara = pair[i];
			//如果当前这个和下一个都是字符串并且下一个是asc或desc
			if (
					i < pair.length-1 &&
					currentPara instanceof String &&
					pair[i+1] instanceof String &&
					(
							pair[i+1].toString().toUpperCase().equals("ASC") ||
							pair[i+1].toString().toUpperCase().equals("DESC")
					)

			){
				_setSqlList(ORDER, "`" + pair[i] + "` " + pair[i+1].toString().toUpperCase());
				++i;
			}
			//如果当前是字符串
			else if (currentPara instanceof String && StrKit.notBlank(currentPara.toString())){
				String orderStr = (String) currentPara;
				//如果字符串带了空格
				if (orderStr.contains(" ")){
					_setSqlList(ORDER, orderStr );
				}else{
					_setSqlList(ORDER,"`" + orderStr + "`");
				}
			}
			//如果是表单参数
			else if (currentPara instanceof FormFilter){
				FormFilter formFilter = (FormFilter) currentPara;
                if (StrKit.notBlank(formFilter.getOrderName())){
					_setSqlList(ORDER, "`" + formFilter.getOrderName() + "` " + formFilter.getOrderType().toUpperCase());
				}
			}
		}

		return this;
	}

	public String buildBefore(){
		return "SELECT " + (_sqlStrMap.containsKey(COLUMN) ? _sqlStrMap.get(COLUMN) : "*");
	}

	protected void _buildCondition(StringBuilder sb,String keyName){
		if (_sqlListMap.containsKey(keyName))
			sb.append("\n"+keyName+"\n\t" + _sqlListMap.get(keyName));
	}
	
	public String buildAfter(){
		StringBuilder sb = new StringBuilder();
		_addCondition(sb,"\nFROM ", TABLE);
		_buildCondition(sb,WHERE);
		_addCondition(sb,"\nGROUP BY ", GROUP);
		_buildCondition(sb, ORDER);
		_buildCondition(sb,HAVING);
		_addCondition(sb,"\nLIMIT ", LIMIT);
		return sb.toString();
	}

	protected void _addCondition(StringBuilder sb,String after,String key){
		if(_sqlStrMap.containsKey(key))
			sb.append(after + _sqlStrMap.get(key));
	}

	public Object[] getValues(String... key){
		ArrayList<Object> objects = new ArrayList<>();
		for(String k : key){
			if (_values.containsKey(k)) objects.addAll(_values.get(k));
		}
		return  objects.toArray(new Object[objects.size()]);
	}

	@SuppressWarnings("unchecked")
	/**
	 * 进行分页
	 */
	public Page<M> page(int page, int size){
        Page<M> result = (Page<M>) _model.paginate(
                page,size,
                _sqlStrMap.containsKey(GROUP),
                buildBefore(),
                buildAfter(),
                getValues(WHERE,HAVING));

        if (isAutoBind){
            MergeUtil.autoBind(result.getList());
        }

		return result;
	}

	/**
	 * 进行分页
	 * @param filter 参数
	 * @return 分页对象
	 */
	public Page<M> page(FormFilter filter){
	    //如果没有显式调用orderBy语句，并且表单中已经带了排序字段，并且该字段包含在主表中，则自动排序
	    if (
	            StrKit.notBlank(filter.getOrderName()) &&
                _thisTable.getColumnTypeMap().keySet().contains(filter.getOrderName()) &&
                !_sqlListMap.containsKey(ORDER)){
	        orderBy(filter);
        }
		return page(filter.getPageNumber(), filter.getPageSize());
	}

	/**
	 * 进行分页
	 * @param filter 参数
	 * @return Page
	 */
	public Page<M> page(FormFilter filter,Consumer<List<M>> consumer){
		Page<M> result = page(filter.getPageNumber(),filter.getPageSize());
		consumer.accept(result.getList());
		return result;
	}

	/**
	 * 进行分页，补全需要关联的字段
	 * @param page 页码
	 * @param size 页数
	 * @return Page
	 */
	public Page<M> allPage(int page, int size){
	    autoBind();
		return page(page, size);
	}

	/**
	 * 进行分页，补全需要关联的字段
	 * @param filter 参数
	 * @return Page
	 */
	public Page<M> allPage(FormFilter filter){
		autoBind();
		return page(filter);
	}

	/**
	 * 进行分页，补全需要关联的字段
	 * @param filter 参数
	 * @return Page
	 */
	public Page<M> allPage(FormFilter filter,Consumer<List<M>> consumer){
		Page<M> result = allPage(filter);
		consumer.accept(result.getList());
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<M> select(){
        List<M> result =  (List<M>) _model.find(buildBefore() + buildAfter(), getValues(WHERE,HAVING));
        if (isAutoBind){
            MergeUtil.autoBind(result);
        }
        return result;
	}

    /**
	 * 返回一个流
	 * @return Stream对象
	 */
	public Stream<M> stream(){
		return select().stream();
	}

	public List<M> allSelect(){
		autoBind();
		return select();
	}

	/**
	 * 自动绑定后的流
	 * @return Stream对象
	 */
	public Stream<M> allStream(){
		return allSelect().stream();
	}

	/**
	 * 按分页遍历数据源，一百条一次
	 * @param action 回调函数
	 */
	public void forEach(Consumer<? super M> action){
		forEach(100,action);
	}

	/**
	 * 按分页遍历数据源
	 * @param pageSize 分页条数
	 * @param action 回调函数
	 */
	public void forEach(int pageSize,Consumer<? super M> action){
		if(!_sqlListMap.containsKey("ORDER")){
			System.err.println("!!!遍历时最好加入order语句确保遍历的次序");
		}
		Page<M> _page = page(1,pageSize);
		if (_page != null && _page.getList().size() > 0){
			_page.getList().forEach(action);
			if(_page.getTotalPage() >1){
				for(int i=2; i<=_page.getTotalPage(); i++){
					Page<M> _page2 = page(i,pageSize);
					_page2.getList().forEach(action);
				}
			}
		}
	}

	//按分页遍历数据源，一百条一次
	public void allForEach(Consumer<? super M> action){
		allForEach(100, action);
	}

	public void allForEach(int pageSize,Consumer<? super M> action){
		if(!_sqlListMap.containsKey("ORDER")){
			System.err.println("!!!遍历时最好加入order语句确保遍历的次序");
		}
		Page<M> _page = allPage(1,pageSize);
		if (_page != null && _page.getList().size() > 0){
			_page.getList().forEach(action);
			if(_page.getTotalPage() >1){
				for(int i=2; i<= _page.getTotalPage(); i++){
					Page<M> _page2 = allPage(i,pageSize);
					_page2.getList().forEach(action);
				}
			}
		}
	}

	/**
	 * 获取数据列中的id的Set数组
	 * @return 数据列中的id的Set数组
	 */
	public Set<Long> selectIds(){
		return selectSet("id");
	}

	/**
	 * 获取数据列中的id_key的Set数组
	 * @return 数据列中的id_key的Set数组
	 */
	public Set<Long> selectIds(String idKey){
		return selectSet(idKey);
	}

	/**
	 * 获取数据列中的id_key的Set数组
	 * @return 数据列中的id_key的Set数组
	 */
	public Set selectSet(String recordKey){
		return stream().map(m -> m.get(recordKey))
				.collect(Collectors.toSet());
	}

	/**
	 * 获取一个一对一的map
	 * @param keyName map中key的name
	 * @param valueName map中value的name
	 * @return 数据列转成的Map
	 */
	public <K,V> Map<K,V> selectMap(String keyName,String valueName){
		return selectRecord().stream()
				.collect(
						Collectors.toMap(r->(K)r.get(keyName),r->(V)r.get(valueName))
				);
	}

	/**
	 * 获取一个一对多的map
	 * @param keyName map中key的name
	 * @return 数据列转成的Map
	 */
	public <T, T2 extends List<M>> Map<T,List<M>> selectMap(String keyName){
		Map<T ,List<M>> result = new HashMap();
		select().stream().forEach(m -> {
			T key = (T) m.get(keyName);
			if (!result.containsKey(key)){
				result.put(key,new ArrayList<M>());
			}
			result.get(key).add(m);
		});
		return result;
	}

	public List<Record> selectRecord(){
		return Db.find(buildBefore()+ buildAfter(), getValues(WHERE,HAVING));
	}
	
	@SuppressWarnings("unchecked")
	public M findFirst(){
		return (M) _model.findFirst(buildBefore() + buildAfter(), getValues(WHERE,HAVING));
	}

	@SuppressWarnings("unchecked")
	public M allFindFirst(){
		M result = (M) _model.findFirst(buildBefore() + buildAfter(), getValues(WHERE,HAVING));
		if (result!=null){
			List<M> resultArray = new ArrayList<>();
			resultArray.add(result);
			MergeUtil.autoBind(resultArray);
			return resultArray.get(0);
		}else{
			return null;
		}
	}
	
	public int delete(){
		if(_sqlListMap.get(WHERE) != null){
			return Db.update("DELETE " + buildAfter(), getValues(WHERE));
		}else {
			System.err.println("!!!没有where条件，不执行delete语句！");
			return 0;
		}
	}
	
	public <T> T queryFirst(){
		return Db.queryFirst(buildBefore() + buildAfter(), getValues(WHERE,HAVING));
	}

	public <T> T queryFirst(String column){
		_setSqlStr(COLUMN, column);
		return Db.queryFirst(buildBefore() + buildAfter(), getValues(WHERE,HAVING));
	}
	
	public long count(){
		return queryFirst("count(1)");
	}
	
	public <T> T max(String column){
		return queryFirst("max(`"+ column +"`)");
	}
	
	public <T> T min(String column){
		return queryFirst( "min(`"+ column +"`)");
	}
	
	public <T> T avg(String column){
		return queryFirst("avg(`"+ column +"`)");
	}

	public long sum(String column){
		BigDecimal sum = queryFirst("sum(`"+ column +"`)");
		return sum == null ? 0 : sum.longValue();
	}
	
	public String toString(){
		return buildBefore()+ buildAfter();
	}

	// 输出sql语句 和 参数列表
	public SqlBuilder<M> dump(){
		System.out.println("######## SQL #######");
		System.out.println(this.toString());
		System.out.println("######## PARA #######");
		System.out.println(JsonKit.toJson(getValues(SET,WHERE,HAVING)));
		System.out.println("######## END #######");
		return this;
	}

	/**
	 * set语句
	 * @param pair
	 * @return SqlBuilder
	 */
	public SqlBuilder<M> set(String pair){
		_setSqlList(SET, pair);
		return this;
	}

    /**
     * set语句
     * @param model
     * @return SqlBuilder
     */
    public SqlBuilder<M> set(Model model){
        for(String attr : model._getAttrNames()){
            set(attr,model.get(attr));
        }
        return this;
    }

	/**
	 * set语句
	 * @param action
	 * @return SqlBuilder
	 */
	public SqlBuilder<M> set(Consumer<M> action){
	    try {
            M object = (M) _model.getClass().newInstance();
            action.accept(object);
            return set(object);
        }catch (Exception e){
	        e.printStackTrace();
        }

		return this;
	}

	/**
	 * set语句
	 * @param pair
	 * @param value
	 * @return SqlBuilder
	 */
	public SqlBuilder<M> set(String pair, Object value){
		_setSqlList(SET, "`" + pair + "`=?");
		_setValue(SET, value);
		return this;
	}

	public int update(){
		if(_sqlListMap.get(WHERE) != null || _sqlListMap.get(SET) == null){

			StringBuilder whereSb = new StringBuilder();
			_buildCondition(whereSb,WHERE);
			return Db.update(
					"UPDATE `"+ _sqlStrMap.get(TABLE) +"` SET " +
							_sqlListMap.get(SET)  + whereSb,
					getValues(SET,WHERE));
		}else {
			System.err.println("!!!没有where条件或没有值，不执行update语句！");
			return 0;
		}
	}


    public boolean fakeDelete(String idsStr){
        long[] ids = ArrayStrUtil.str2LongArray(idsStr);
        return fakeDelete(ids);
    }

    public boolean fakeDelete(long[] ids){
        if(ids == null || ids.length == 0){
            return false;
        }else{
            String idsStr = ArrayStrUtil.join(ids);
            int result;
            if(_thisTable.getColumnTypeMap().containsKey("delete_time")){
                result = Db.update(
                		"UPDATE " + _sqlStrMap.get(TABLE) +
								" SET is_delete=1,delete_time=?" +
								" where id in ("+idsStr+")",
						TimeUtil.getNow());
            }else{
                result = Db.update("UPDATE "+ _sqlStrMap.get(TABLE) +
								" SET is_delete=1 where id in ("+idsStr+")");
            }
            return result > 0;
        }
    }

    public boolean disable(String idsStr){
        long[] ids = ArrayStrUtil.str2LongArray(idsStr);
        return disable(ids);
    }

    public boolean disable(long[] ids){
        if(ids == null || ids.length == 0){
            return false;
        }else{
            String idsStr = ArrayStrUtil.join(ids);
            int result;
            if(_thisTable.getColumnTypeMap().containsKey("update_time")){
                result = Db.update("update "+ _sqlStrMap.get(TABLE) +
						" set is_disable=1,update_time=?" +
						" where id in ("+idsStr+")",
						TimeUtil.getIntNow());
            }else{
                result = Db.update("update "+ _sqlStrMap.get(TABLE) +" set is_disable=1 where id in ("+idsStr+")");
            }
            return result > 0;
        }
    }

    public boolean disable(long[] ids, int status){
        if(ids == null || ids.length == 0){
            return false;
        }else{
            String idsStr = ArrayStrUtil.join(ids);
            int result;
            if(_thisTable.getColumnTypeMap().containsKey("update_time")){
                result = Db.update("update "+ _sqlStrMap.get(TABLE) +
						" set is_disable=?,update_time=?" +
						" where id in ("+idsStr+")",
						status,
						TimeUtil.getIntNow());
            }else{
                result = Db.update("update "+ _sqlStrMap.get(TABLE) +
						" set is_disable=? where id in ("+idsStr+")", status);
            }
            return result > 0;
        }
    }

}