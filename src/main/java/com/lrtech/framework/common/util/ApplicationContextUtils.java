package com.lrtech.framework.common.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by IntelliJ IDEA.
 *
 * @author leizhimin 11-12-13 上午2:12
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
    }
}
