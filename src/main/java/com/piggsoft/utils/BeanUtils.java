package com.piggsoft.utils;

import org.apache.commons.beanutils.BeanMap;
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
 * Created by user on 2015/11/16.
 */
public class BeanUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtils.class);

    public static Map<String, Object> beanToMap(Object obj) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            BeanInfo info = Introspector.getBeanInfo(obj.getClass());
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                Method reader = pd.getReadMethod();
                if (pd.getName().compareToIgnoreCase("class") == 0) {
                    continue;
                }
                if (reader != null)
                    result.put(pd.getName(), reader.invoke(obj));
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

    public static <T> T mapToBean(Class<T> clazz, Map<String, ? extends Object> map) {
        T t = null;
        try {
            t = clazz.newInstance();
            org.apache.commons.beanutils.BeanUtils.populate(t, map);
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (InstantiationException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return t;
    }

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

    public static <T> T newInstance(String className) {
        try {
            return newInstance(ClassUtils.getClass(className));
        } catch (ClassNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

}
