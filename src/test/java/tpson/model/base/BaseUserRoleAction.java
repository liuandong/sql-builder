package tpson.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by SqlBuilder, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseUserRoleAction<M extends BaseUserRoleAction<M>> extends Model<M> implements IBean,IUserRoleAction {

	public M setId(java.lang.Long id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Long getId() {
		return getLong("id");
	}

	public M setRoleId(java.lang.Long roleId) {
		set("role_id", roleId);
		return (M)this;
	}

	public java.lang.Long getRoleId() {
		return getLong("role_id");
	}

	public M setActionId(java.lang.Long actionId) {
		set("action_id", actionId);
		return (M)this;
	}

	public java.lang.Long getActionId() {
		return getLong("action_id");
	}

}
