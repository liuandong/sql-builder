package tpson.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by SqlBuilder, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseUser<M extends BaseUser<M>> extends Model<M> implements IBean,IUser {

	public M setId(java.lang.Long id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Long getId() {
		return getLong("id");
	}

	public M setRoleId(java.lang.Integer roleId) {
		set("role_id", roleId);
		return (M)this;
	}

	public java.lang.Integer getRoleId() {
		return getInt("role_id");
	}

	public M setUsername(java.lang.String username) {
		set("username", username);
		return (M)this;
	}

	public java.lang.String getUsername() {
		return getStr("username");
	}

	public M setPassword(java.lang.String password) {
		set("password", password);
		return (M)this;
	}

	public java.lang.String getPassword() {
		return getStr("password");
	}

	public M setName(java.lang.String name) {
		set("name", name);
		return (M)this;
	}

	public java.lang.String getName() {
		return getStr("name");
	}

	public M setSex(java.lang.Integer sex) {
		set("sex", sex);
		return (M)this;
	}

	public java.lang.Integer getSex() {
		return getInt("sex");
	}

	public M setRemark(java.lang.String remark) {
		set("remark", remark);
		return (M)this;
	}

	public java.lang.String getRemark() {
		return getStr("remark");
	}

	public M setPaperType(java.lang.Integer paperType) {
		set("paper_type", paperType);
		return (M)this;
	}

	public java.lang.Integer getPaperType() {
		return getInt("paper_type");
	}

	public M setPaperNumber(java.lang.String paperNumber) {
		set("paper_number", paperNumber);
		return (M)this;
	}

	public java.lang.String getPaperNumber() {
		return getStr("paper_number");
	}

	public M setSalt(java.lang.String salt) {
		set("salt", salt);
		return (M)this;
	}

	public java.lang.String getSalt() {
		return getStr("salt");
	}

	public M setLoginTimes(java.lang.Integer loginTimes) {
		set("login_times", loginTimes);
		return (M)this;
	}

	public java.lang.Integer getLoginTimes() {
		return getInt("login_times");
	}

	public M setPhone(java.lang.String phone) {
		set("phone", phone);
		return (M)this;
	}

	public java.lang.String getPhone() {
		return getStr("phone");
	}

	public M setTel(java.lang.String tel) {
		set("tel", tel);
		return (M)this;
	}

	public java.lang.String getTel() {
		return getStr("tel");
	}

	public M setEmail(java.lang.String email) {
		set("email", email);
		return (M)this;
	}

	public java.lang.String getEmail() {
		return getStr("email");
	}

	public M setTag(java.lang.String tag) {
		set("tag", tag);
		return (M)this;
	}

	public java.lang.String getTag() {
		return getStr("tag");
	}

	public M setDefaultSms(java.lang.Boolean defaultSms) {
		set("default_sms", defaultSms);
		return (M)this;
	}

	public java.lang.Boolean getDefaultSms() {
		return get("default_sms");
	}

	public M setAddTime(java.lang.Long addTime) {
		set("add_time", addTime);
		return (M)this;
	}

	public java.lang.Long getAddTime() {
		return getLong("add_time");
	}

	public M setUpdateTime(java.lang.Long updateTime) {
		set("update_time", updateTime);
		return (M)this;
	}

	public java.lang.Long getUpdateTime() {
		return getLong("update_time");
	}

	public M setDeleteTime(java.lang.Integer deleteTime) {
		set("delete_time", deleteTime);
		return (M)this;
	}

	public java.lang.Integer getDeleteTime() {
		return getInt("delete_time");
	}

	public M setIsDelete(java.lang.Boolean isDelete) {
		set("is_delete", isDelete);
		return (M)this;
	}

	public java.lang.Boolean getIsDelete() {
		return get("is_delete");
	}

	public M setIsDisable(java.lang.Boolean isDisable) {
		set("is_disable", isDisable);
		return (M)this;
	}

	public java.lang.Boolean getIsDisable() {
		return get("is_disable");
	}

}