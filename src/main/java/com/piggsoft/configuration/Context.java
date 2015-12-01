package com.piggsoft.configuration;

import org.springframework.context.ApplicationContext;
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
        CONTEXT = new ClassPathXmlApplicationContext(configLocations, true);
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
