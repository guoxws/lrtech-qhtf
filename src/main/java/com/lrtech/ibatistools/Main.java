package com.lrtech.ibatistools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lrtech.ibatistools.bean.Table;
import com.lrtech.ibatistools.common.DBTools;
import com.lrtech.ibatistools.metadata.DataSourceMetaData;
import com.lrtech.ibatistools.metadata.MySQLDataSourceMetaData;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author leizhimin 12-1-5 上午10:29
 */
public class Main {

    private static Configuration cfg;       //模版配置对象
    private static String generate_pattern;
    private static String template_path;
    private static String src_path;
    private static String pkg_root;
    private static String subpkg_dao;
    private static String subpkg_entity;
    private static String subpkg_sqlmap;
    private static String separator = "/";

    static {
        reloadcfg();
    }

    public static boolean reloadcfg() {
        boolean flag = true;
        Properties prop = new Properties();
        InputStream ins = Main.class.getResourceAsStream("/com/lrtech/ibatistools/config.properties");
        try {
            prop.load(ins);
            generate_pattern = prop.getProperty("generate.pattern", "append");
            template_path = prop.getProperty("template.path");
            src_path = formatPath(prop.getProperty("project.src.path"));
            pkg_root = prop.getProperty("package.root");
            subpkg_dao = prop.getProperty("package.dao");
            subpkg_entity = prop.getProperty("package.entity");
            subpkg_sqlmap = prop.getProperty("package.sqlmap");
            separator = System.getProperty("file.separator", "/");
            //初始化FreeMarker配置
            //创建一个Configuration实例
            cfg = new Configuration();
            cfg.setEncoding(new Locale("CN","zh"),"UTF-8");
            //设置FreeMarker的模版文件夹位置
            cfg.setDirectoryForTemplateLoading(new File(template_path));
        } catch (IOException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    public static String getDaoPackage() {
        return pkg_root + "." + subpkg_dao;
    }

    public static String getEntityPackage() {
        return pkg_root + "." + subpkg_entity;
    }

    public static String getSqlmapPackage() {
        return pkg_root + "." + subpkg_sqlmap;
    }

    public static String getDaoFoler() {
        return src_path + separator + getDaoPackage().replace('.', separator.charAt(0));
    }

    public static String getEntityFoler() {
        return src_path + separator + getEntityPackage().replace('.', separator.charAt(0));
    }

    public static String getSqlmapFoler() {
        return src_path + separator + getSqlmapPackage().replace('.', separator.charAt(0));
    }

    public static void main(String[] args) throws IOException, TemplateException {
        DataSourceMetaData dbmd = MySQLDataSourceMetaData.instatnce();
        List<Table> tableList = dbmd.getAllTableMetaData(DBTools.makeConnection());

        Template t_entity = cfg.getTemplate("entity.ftl");
        Template t_dao = cfg.getTemplate("dao.ftl");
        Template t_sqlMap = cfg.getTemplate("sqlmap.ftl");
        Template t_daoconfig = cfg.getTemplate("daoconfig.ftl");
        Template t_springcfg = cfg.getTemplate("springcofig.ftl");

        String daoFolder = getDaoFoler();
        String sqlmapFolder = getSqlmapFoler();
        String entityFolder = getEntityFoler();
        String daoPackage = getDaoPackage();
        String entityPackage = getEntityPackage();
        

        File daofd = new File(daoFolder);
        File sqlmapfd = new File(sqlmapFolder);
        File entityfd = new File(entityFolder);
        File rootfd = new File(src_path);
        if (!rootfd.exists()) daofd.mkdirs();
        if (!daofd.exists()) daofd.mkdirs();
        if (!sqlmapfd.exists()) sqlmapfd.mkdirs();
        if (!entityfd.exists()) entityfd.mkdirs();

        Map<String, String> map = new HashMap<String, String>();
        map.put("daoPackage", daoPackage);
        map.put("entityPackage", entityPackage);

        List<String> tableNameList = new ArrayList<String>();
        for (Table table : tableList) {
            JSONObject obj = (JSONObject) JSON.toJSON(table);
            String x = JSON.toJSONString(table);
            obj.putAll(map);
            tableNameList.add(table.getName());
            OutputStreamWriter entity_out = new FileWriter(entityfd + separator + toUpperCaseFirstOne(table.getName()) + ".java");
            OutputStreamWriter dao_out = new FileWriter(daofd + separator + toUpperCaseFirstOne(table.getName()) + "DAO.java");
            OutputStreamWriter sqlmap_out = new FileWriter(sqlmapfd + separator + toUpperCaseFirstOne(table.getName()) + ".xml");
            t_entity.process(obj, entity_out);
            t_dao.process(obj, dao_out);
            t_sqlMap.process(obj, sqlmap_out);
        }

        Map<String,Object> cfgMap = new HashMap<String, Object>(1);
        cfgMap.put("tableNameList",tableNameList);
        cfgMap.put("daoPackage",getDaoPackage());
        cfgMap.put("sqlmapPackage",getSqlmapPackage());
        cfgMap.put("entityPackage",getEntityPackage());
        OutputStreamWriter daoConfig_out = new FileWriter(src_path + separator+"daoConfig.xml");
        t_daoconfig.process(cfgMap,daoConfig_out);
//        OutputStreamWriter spring_out = new FileWriter(src_path + separator+"SpringCfg.xml");
//        t_springcfg.process(cfgMap,spring_out);
    }

    //首字母转大写
    public static String toUpperCaseFirstOne(String s) {
//        String str="hello";
//        str =str.replaceFirst(str.substring(0, 1),str.substring(0, 1).toUpperCase())  ;

        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    /**
     * 格式化文件路径，将其中不规范的分隔转换为系统标准的分隔符,并且去掉末尾的"/"符号。
     *
     * @param path 文件路径
     * @return 格式化后的文件路径
     */
    public static String formatPath(String path) {
        String reg0 = "\\+";
        String temp = path.trim().replaceAll(reg0, "/");
        if (temp.endsWith("/")) {
            temp = temp.substring(0, temp.length() - 1);
        }
        return temp.replaceAll("/", System.getProperty("file.separator"));
    }
}
