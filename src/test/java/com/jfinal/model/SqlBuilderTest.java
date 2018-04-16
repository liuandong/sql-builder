package com.jfinal.model;

import com.jfinal.FormatUtil;
import com.jfinal.jfinalTest;
import com.jfinal.kit.JsonKit;
import org.junit.Test;
import tpson.model.Alarm;
import tpson.model.AlarmType;
import tpson.model.FireRoom;
import tpson.model.SensorsType;
import tpson.model.base.IAlarm;
import tpson.model.base.IFireRoom;

import java.util.List;
import java.util.Map;

/**
 * Created by liuandong on 2017/10/30.
 */
public class SqlBuilderTest extends jfinalTest implements IFireRoom,IAlarm {
    @Test
    public void update() throws Exception {
        System.out.println(SqlBuilder.dao(FireRoom.dao)
                .set(fireRoom -> {
                    fireRoom.setPhone("123321");
                    fireRoom.setName("test111");
                })
                .where(FireRoom._id, 1)
                .dump()
                .update());

        FireRoom fireRoom = new FireRoom();
        fireRoom.setPhone("22222");
        fireRoom.setName("aaaaa");

        System.out.println(SqlBuilder.dao(FireRoom.dao)
                .set(fireRoom)
                .where(FireRoom._id, 1)
                .dump()
                .update());

        System.out.println(SqlBuilder.dao(FireRoom.dao)
                .set(FireRoom._phone, "123321")
                .set(FireRoom._name, "test111")
                .where(FireRoom._id, 1)
                .dump()
                .update());
    }

    @Test
    public void forEach(){
        SqlBuilder.dao(AlarmType.dao)
                .orderBy("id")
                .allForEach(2,alarmType -> {
                    System.out.println(alarmType.getName());
                });
    }

    @Test
    public void select(){
        System.out.println(
                SqlBuilder.dao(Alarm.dao)
                        .columnsExcept("id","name")
                        .dump()
                        .select()
        );
    }

    @Test
    public void count(){
        System.out.println(
                SqlBuilder.dao(SensorsType.dao)
                        .dump()
                        .count()
        );
    }

    @Test
    public void page(){
        FormFilter f= new FormFilter("page_number/1/page_size/10/order_name/add_time/order_type/asc");

        FormatUtil.printJson(
                JsonKit.toJson(
                SqlBuilder.dao(Alarm.dao)
                        .columnsExcept(Alarm._dealUserId)
                        .where(alarm -> {
                            alarm.setId(1l);
                            alarm.setCompanyId(1l);
                        })
                        .dump()
                        .allPage(f)
        ));
    }

    @Test
    public void map(){
        FormFilter f= new FormFilter("page_number/1/page_size/10/order_name/add_time/order_type/asc");

        Map result = SqlBuilder.dao(Alarm.dao)
                .columnsExcept(Alarm._dealUserId)
                .where(alarm -> {
                    alarm.setId(1l);
                    alarm.setCompanyId(1l);
                })
                .dump()
                .selectMap("company_id");

        FormatUtil.printJson(
                JsonKit.toJson(result));
    }

}