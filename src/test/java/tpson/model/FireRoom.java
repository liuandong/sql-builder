package tpson.model;

import com.jfinal.kit.StrKit;
import com.jfinal.model.MergeConfig;
import com.jfinal.model.MergeUtil;
import com.jfinal.model.iAssociation;
import com.jfinal.model.iAutoBind;
import tpson.model.base.BaseFireRoom;

import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class FireRoom extends BaseFireRoom<FireRoom> implements iAssociation,iAutoBind {
	public static final FireRoom dao = new FireRoom().dao();

	@Override
	public MergeConfig association(String fk) {
		fk = StrKit.isBlank(fk)?"fire_room_id":fk;
		return new MergeConfig(fk)
				.setAlias("name","fire_room_name")
				.setColumns("name");
	}

	@Override
	public void bind(List<?> data, String columnName) {
		switch (columnName){
			case "geo_id":
				MergeUtil.merge(data, CompanyGeo.dao, "geo_id");
				break;
		}
	}
}
