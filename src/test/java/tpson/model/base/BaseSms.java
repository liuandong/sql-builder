package tpson.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by SqlBuilder, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSms<M extends BaseSms<M>> extends Model<M> implements IBean,ISms {

	public M setId(java.lang.Long id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Long getId() {
		return getLong("id");
	}

	public M setPhone(java.lang.String phone) {
		set("phone", phone);
		return (M)this;
	}

	public java.lang.String getPhone() {
		return getStr("phone");
	}

	public M setData(java.lang.String data) {
		set("data", data);
		return (M)this;
	}

	public java.lang.String getData() {
		return getStr("data");
	}

	public M setTime(java.lang.Integer time) {
		set("time", time);
		return (M)this;
	}

	public java.lang.Integer getTime() {
		return getInt("time");
	}

}