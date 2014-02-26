package com.lrtech.framework.ext.spring;

import com.lrtech.framework.common.Encrypt;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 * 重写PropertyPlaceholderConfigurer的processProperties方法实现
 *
 * @author leizhimin 2012-03-14 16:47
 */
public class PropertyPlaceholderConfigurerExt extends PropertyPlaceholderConfigurer{

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
            throws BeansException {
        String password = props.getProperty("jdbc.password");
        if (password != null) {
            //解密jdbc.password属性值，并重新设置
            props.setProperty("jdbc.password", Encrypt.decode(password));
        }
        super.processProperties(beanFactory, props);

    }
}
