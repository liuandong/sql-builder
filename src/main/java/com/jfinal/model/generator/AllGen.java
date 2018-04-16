package com.jfinal.model.generator;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.generator.BaseModelGenerator;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.activerecord.generator.ModelGenerator;
import com.jfinal.plugin.druid.DruidPlugin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuandong on 2017/11/17.
 */
public class AllGen {

    private static String _jdbc;
    private static String _user;
    private static String _password;

    public static void gen(String jdbc,String dbName,String user,String password,String prefixStr){

        _jdbc = jdbc;
        _user = user;
        _password = password;

        // base model 所使用的包名
        String modelPackageName = "tpson.model";
        String baseModelPackageName = modelPackageName + ".base";
        // base model 文件保存路径
        System.out.println(PathKit.getWebRootPath());
        String baseModelOutputDir = PathKit.getWebRootPath() + "/src/main/java/tpson/model/base";
        System.out.println("model url:" + baseModelOutputDir);

        String modelOutputDir = baseModelOutputDir + "/..";

        // 创建生成器
        BaseModelGenerator gen = new BaseModelAndInterfaceGen(baseModelPackageName,baseModelOutputDir);
        ModelGenerator modelGen = new ModelGenerator(modelPackageName, baseModelPackageName, modelOutputDir);
        Generator gernerator = new Generator(getDataSource() , gen, modelGen);

        gernerator.setGenerateDaoInModel(true);
        gernerator.setGenerateChainSetter(true);
        // 设置是否生成字典文件
        gernerator.setGenerateDataDictionary(false);
        // 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
        gernerator.setRemovedTableNamePrefixes(prefixStr);

        // 添加除前缀外的所有表名
        gernerator.addExcludedTable(getExcTab(dbName,prefixStr));
        // 生成
        gernerator.generate();
    }

    private static DataSource getDataSource(){
        DruidPlugin druidPlugin = new DruidPlugin(_jdbc,_user,_password);
        druidPlugin.setInitialSize(1);
        druidPlugin.setMinIdle(1);
        druidPlugin.start();
        return druidPlugin.getDataSource();
    }

    private static String[] getExcTab(String dbName,String preName){
        String sql="SELECT table_name " +
                "from information_schema.tables " +
                "WHERE table_schema = '"  +dbName+ "' " +
                "AND table_name NOT LIKE '"+preName+"%'";

        List<String> list = new ArrayList<String>();
        Connection conn = null;
        try {
            conn = getDataSource().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs=stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        String[] s=new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            s[i]= list.get(i);
        }
        return s;
    }

}
