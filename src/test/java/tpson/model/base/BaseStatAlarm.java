package tpson.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by SqlBuilder, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseStatAlarm<M extends BaseStatAlarm<M>> extends Model<M> implements IBean,IStatAlarm {

	public M setId(java.lang.Long id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Long getId() {
		return getLong("id");
	}

	public M setAlarmLevel(java.lang.Integer alarmLevel) {
		set("alarm_level", alarmLevel);
		return (M)this;
	}

	public java.lang.Integer getAlarmLevel() {
		return getInt("alarm_level");
	}

	public M setAlarmCount(java.lang.Integer alarmCount) {
		set("alarm_count", alarmCount);
		return (M)this;
	}

	public java.lang.Integer getAlarmCount() {
		return getInt("alarm_count");
	}

	public M setTimes(java.lang.Long times) {
		set("times", times);
		return (M)this;
	}

	public java.lang.Long getTimes() {
		return getLong("times");
	}

	public M setDeal(java.lang.Integer deal) {
		set("deal", deal);
		return (M)this;
	}

	public java.lang.Integer getDeal() {
		return getInt("deal");
	}

	public M setDate(java.util.Date date) {
		set("date", date);
		return (M)this;
	}

	public java.util.Date getDate() {
		return get("date");
	}

}
