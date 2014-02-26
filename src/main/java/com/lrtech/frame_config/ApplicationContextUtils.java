package com.lrtech.frame_config;//import com.asiainfo.tdmc.dao.Cfg_codeDAO;
//import com.asiainfo.tdmc.entity.Cfg_code;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Spring上下文工具
 *
 * @author leizhimin 2012-03-05 22:05
 */
public class ApplicationContextUtils {
    private static ApplicationContext applicationContext;

    static {
        if (applicationContext == null)
            applicationContext = rebuildApplicationContext();
    }

    public static ApplicationContext rebuildApplicationContext() {
        return new ClassPathXmlApplicationContext("/spring-core.xml");
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        rebuildApplicationContext();
        if (applicationContext == null) {
            System.out.println("ApplicationContext is null");
        } else {
            System.out.println("ApplicationContext is not null!");
        }

//        Cfg_codeDAO cfg_codeDAO = (Cfg_codeDAO)applicationContext.getBean("cfg_codeDAO");
//        List<Cfg_code> list = cfg_codeDAO.query(new HashMap<String, Object>());
//        for (Cfg_code cfg_code : list) {
//            System.out.println(cfg_code.getCode());
//        }

    }
}