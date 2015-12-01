package com.piggsoft.configuration;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Properties;

/**
 * Context 工具类
 * <br>Created by fire pigg on 2015/11/30.
 *
 * @author piggsoft@163.com
 */
public class Context {

    /**
     * context 副本
     */
    private static ApplicationContext CONTEXT;

    /**
     * util:properties 副本
     */
    private static Properties PROPS;

    /**
     * 初始化工作
     *
     * @param configLocations 配置文件地址
     */
    public static void init(String[] configLocations) {
        init(configLocations, null);
    }

    /**
     * 初始化工作
     *
     * @param configLocations 配置文件地址
     * @param parent 父Context
     */
    public static void init(String[] configLocations, ApplicationContext parent) {
        if (null == parent || !(parent instanceof ConfigurableApplicationContext)) {
            init0(configLocations, parent);
        } else {
            initIntoParent(configLocations, parent);
        }
        afterInit();
    }

    /**
     * 初始化工作
     *
     * @param configLocations 配置文件地址
     * @param parent 父Context
     */
    private static void init0(String[] configLocations, ApplicationContext parent) {
        CONTEXT = new ClassPathXmlApplicationContext(configLocations, true, parent);
    }

    /**
     * 初始化工作, 像容器中注入配置，非父子
     * @param configLocations 配置文件地址
     * @param context 主容器
     */
    private static void initIntoParent(String[] configLocations, ApplicationContext context) {
        ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) context;
        ApplicationContext current = new ClassPathXmlApplicationContext(configLocations, true);
        applicationContext.setParent(current);
        applicationContext.refresh();
        CONTEXT = context;
        /*for (String configLocation : configLocations) {
            XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader((BeanDefinitionRegistry)
                    applicationContext.getBeanFactory());
            beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(applicationContext));
            beanDefinitionReader.loadBeanDefinitions(configLocation);
        }*/
    }

    /**
     * 初始化完成后进行一些工作
     */
    private static void afterInit() {
        PROPS = CONTEXT.getBean("properties", Properties.class);
    }

    public static ApplicationContext getContext() {
        return CONTEXT;
    }

    /**
     * 获取 util:properties
     * @return util:properties
     */
    public static Properties getProps() {
        return PROPS;
    }
}
