package tpson.model;

import com.jfinal.kit.StrKit;
import com.jfinal.model.MergeConfig;
import com.jfinal.model.iAssociation;
import tpson.model.base.BaseAlarmType;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class AlarmType extends BaseAlarmType<AlarmType> implements iAssociation {

	public static final AlarmType dao = new AlarmType().dao();

	@Override
	public MergeConfig association(String fk) {
		fk = StrKit.isBlank(fk)?"alarm_type_id":fk;
		return new MergeConfig(fk)
				.setAlias("name","alarm_type_name")
				.setColumns("name");
	}
}