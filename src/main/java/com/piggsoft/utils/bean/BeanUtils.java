package com.piggsoft.utils.bean;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author piggsoft@163.com
 * Created by user on 2015/11/16.
 * Bean 相关的工具类
 */
public class BeanUtils {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * java bean 转成 map
     * @param obj 需要转map的bean
     * @return 转之后的map，有Exception后为null
     */
    public static Map<String, Object> beanToMap(Object obj) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            BeanInfo info = Introspector.getBeanInfo(obj.getClass());
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                Method reader = pd.getReadMethod();
                if (pd.getName().compareToIgnoreCase("class") == 0) {
                    continue;
                }
                if (reader != null) {
                    result.put(pd.getName(), reader.invoke(obj));
                }
            }
        } catch (IntrospectionException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * map to java object
     * @param <T> 类型
     * @param clazz Object 对应的class
     * @param map 需要反序列化的map
     * @return 反序列化之后的bean，如果有Exception就为null
     */
    public static <T> T mapToBean(Class<T> clazz, Map<String, Object> map) {
        T t = null;
        try {
            t = clazz.newInstance();
            mapToBean(t, map);
        } catch (InstantiationException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return t;
    }

    /**
     * 往bean里面注入map的属性
     * @param t bean
     * @param map map
     * @param <T> 类型
     * @return 注入完之后的bean
     */
    public static <T> T mapToBean(T t, Map<String, Object> map) {
        try {
            org.apache.commons.beanutils.BeanUtils.populate(t, map);
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return t;
    }

    /**
     * 通过class来new一个bean，统一起来，好做异常管理
     * @param clazz 指定的class
     * @param <T> 类型泛型
     * @return 初始化之后的bean，or null
     */
    public static <T> T newInstance(Class clazz) {
        try {
            return (T)clazz.newInstance();
        } catch (InstantiationException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 通过className来new一个bean，统一起来，好做异常管理
     * @param className 指定的class全名
     * @param <T> 类型泛型
     * @return 初始化之后的bean，or null
     */
    public static <T> T newInstance(String className) {
        try {
            return newInstance(ClassUtils.getClass(className));
        } catch (ClassNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

}
