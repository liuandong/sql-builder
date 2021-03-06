package tpson.model;

import com.jfinal.model.MergeUtil;
import com.jfinal.model.iAutoBind;
import tpson.model.base.BaseUserRoleAction;

import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class UserRoleAction extends BaseUserRoleAction<UserRoleAction> implements iAutoBind {
	public static final UserRoleAction dao = new UserRoleAction().dao();

	@Override
	public void bind(List<?> data, String columnName) {
		switch (columnName){
			case "action_id":
				MergeUtil.merge(data, UserAction.dao, columnName);
				MergeUtil.merge(data, UserActionGroup.dao, "group_id");
				break;
			case "role_id":
				MergeUtil.merge(data, UserRole.dao, columnName);
				break;
		}
	}
}
