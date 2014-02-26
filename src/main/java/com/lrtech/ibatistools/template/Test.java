package com.lrtech.ibatistools.template;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lrtech.ibatistools.bean.Table;
import com.lrtech.ibatistools.common.DBTools;
import com.lrtech.ibatistools.metadata.DataSourceMetaData;
import com.lrtech.ibatistools.metadata.MySQLDataSourceMetaData;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author leizhimin 11-12-29 下午4:03
 */
public class Test {

    private Configuration cfg;      //模版配置对象

    public void init() throws Exception {
        //初始化FreeMarker配置
        //创建一个Configuration实例
        cfg = new Configuration();
        //设置FreeMarker的模版文件夹位置
        cfg.setDirectoryForTemplateLoading(new File("G:\\projects\\testDAO2\\src\\com\\lavasoft\\ibatistools\\template"));
    }

    public void process() throws Exception {
        DataSourceMetaData dbmd = MySQLDataSourceMetaData.instatnce();
        List<Table> tableList = dbmd.getAllTableMetaData(DBTools.makeConnection());
//        for (Table table : tableList) {
//            System.out.println(table);
//        }
        JSONObject obj = (JSONObject) JSON.toJSON(tableList.get(0));
//        System.out.println(obj.get("name"));
//        System.out.println(obj.toJSONString());


//        List<String> barList = new ArrayList<String>();
//        barList.add("aaaa");
//        barList.add("bbbb");
//        barList.add("cccc");
//        //构造填充数据的Map
//        Map map = new HashMap();
//        map.put("foo", "lavasoft");
//        map.put("barList", barList);
        //创建模版对象
//        Template t = cfg.getTemplate("sqlmap.ftl");
//        Template t = cfg.getTemplate("dao.ftl");
        Template t = cfg.getTemplate("entity.ftl");
        //在模版上执行插值操作，并输出到制定的输出流中
        t.process(obj, new OutputStreamWriter(System.out));
    }

    public static void main(String[] args) throws Exception {
        Test hf = new Test();
        hf.init();
        hf.process();
    }


}
