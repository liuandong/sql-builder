package tpson.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by SqlBuilder, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSensorsType<M extends BaseSensorsType<M>> extends Model<M> implements IBean,ISensorsType {

	public M setId(java.lang.Long id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Long getId() {
		return getLong("id");
	}

	public M setName(java.lang.String name) {
		set("name", name);
		return (M)this;
	}

	public java.lang.String getName() {
		return getStr("name");
	}

	public M setAlarmTypeId(java.lang.Long alarmTypeId) {
		set("alarm_type_id", alarmTypeId);
		return (M)this;
	}

	public java.lang.Long getAlarmTypeId() {
		return getLong("alarm_type_id");
	}

	public M setParentId(java.lang.Long parentId) {
		set("parent_id", parentId);
		return (M)this;
	}

	public java.lang.Long getParentId() {
		return getLong("parent_id");
	}

	public M setLevel(java.lang.Integer level) {
		set("level", level);
		return (M)this;
	}

	public java.lang.Integer getLevel() {
		return getInt("level");
	}

	public M setIsDelete(java.lang.Boolean isDelete) {
		set("is_delete", isDelete);
		return (M)this;
	}

	public java.lang.Boolean getIsDelete() {
		return get("is_delete");
	}

	public M setHasThresholdValue(java.lang.Boolean hasThresholdValue) {
		set("has_threshold_value", hasThresholdValue);
		return (M)this;
	}

	public java.lang.Boolean getHasThresholdValue() {
		return get("has_threshold_value");
	}

	public M setUnit(java.lang.String unit) {
		set("unit", unit);
		return (M)this;
	}

	public java.lang.String getUnit() {
		return getStr("unit");
	}

	public M setHasValue(java.lang.Boolean hasValue) {
		set("has_value", hasValue);
		return (M)this;
	}

	public java.lang.Boolean getHasValue() {
		return get("has_value");
	}

}