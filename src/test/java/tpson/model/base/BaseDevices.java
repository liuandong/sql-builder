package tpson.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by SqlBuilder, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseDevices<M extends BaseDevices<M>> extends Model<M> implements IBean,IDevices {

	public M setId(java.lang.Long id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Long getId() {
		return getLong("id");
	}

	public M setCompanyId(java.lang.Long companyId) {
		set("company_id", companyId);
		return (M)this;
	}

	public java.lang.Long getCompanyId() {
		return getLong("company_id");
	}

	public M setType(java.lang.String type) {
		set("type", type);
		return (M)this;
	}

	public java.lang.String getType() {
		return getStr("type");
	}

	public M setVersion(java.lang.Integer version) {
		set("version", version);
		return (M)this;
	}

	public java.lang.Integer getVersion() {
		return getInt("version");
	}

	public M setAlarmTypeId(java.lang.Long alarmTypeId) {
		set("alarm_type_id", alarmTypeId);
		return (M)this;
	}

	public java.lang.Long getAlarmTypeId() {
		return getLong("alarm_type_id");
	}

	public M setAlarmLevel(java.lang.Integer alarmLevel) {
		set("alarm_level", alarmLevel);
		return (M)this;
	}

	public java.lang.Integer getAlarmLevel() {
		return getInt("alarm_level");
	}

	public M setName(java.lang.String name) {
		set("name", name);
		return (M)this;
	}

	public java.lang.String getName() {
		return getStr("name");
	}

	public M setCode(java.lang.String code) {
		set("code", code);
		return (M)this;
	}

	public java.lang.String getCode() {
		return getStr("code");
	}

	public M setIp(java.lang.String ip) {
		set("ip", ip);
		return (M)this;
	}

	public java.lang.String getIp() {
		return getStr("ip");
	}

	public M setPort(java.lang.Integer port) {
		set("port", port);
		return (M)this;
	}

	public java.lang.Integer getPort() {
		return getInt("port");
	}

	public M setFireRoomId(java.lang.Long fireRoomId) {
		set("fire_room_id", fireRoomId);
		return (M)this;
	}

	public java.lang.Long getFireRoomId() {
		return getLong("fire_room_id");
	}

	public M setDetail(java.lang.String detail) {
		set("detail", detail);
		return (M)this;
	}

	public java.lang.String getDetail() {
		return getStr("detail");
	}

	public M setLastTime(java.lang.Integer lastTime) {
		set("last_time", lastTime);
		return (M)this;
	}

	public java.lang.Integer getLastTime() {
		return getInt("last_time");
	}

	public M setIsDelete(java.lang.Boolean isDelete) {
		set("is_delete", isDelete);
		return (M)this;
	}

	public java.lang.Boolean getIsDelete() {
		return get("is_delete");
	}

	public M setOnline(java.lang.Boolean online) {
		set("online", online);
		return (M)this;
	}

	public java.lang.Boolean getOnline() {
		return get("online");
	}

	public M setGeoId(java.lang.Long geoId) {
		set("geo_id", geoId);
		return (M)this;
	}

	public java.lang.Long getGeoId() {
		return getLong("geo_id");
	}

	public M setStatOnline(java.lang.String statOnline) {
		set("stat_online", statOnline);
		return (M)this;
	}

	public java.lang.String getStatOnline() {
		return getStr("stat_online");
	}

	public M setStatValue(java.lang.String statValue) {
		set("stat_value", statValue);
		return (M)this;
	}

	public java.lang.String getStatValue() {
		return getStr("stat_value");
	}

}
