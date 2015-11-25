package com.piggsoft.utils.config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置工具类
 * <br/>从xml或者properties文件中读取配置
 * Created by user on 2015/11/12.
 *
 */
public class ConfigUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUtils.class);

    /**
     * 默认配置
     */
    private static final String NAME_PATTERN = "classpath*:**/wx.properties";
    /**
     * 配置文件缓存，通过pattern缓存
     */
    private static final Map<String, Configuration> CACHE = new HashMap<String, Configuration>();
    /**
     * lock
     */
    private static Object lock = new Object();

    static {
        createConfiguration(NAME_PATTERN);
    }

    /**
     * 获取配置
     * @param pattern
     * @return
     */
    public static Configuration getConfig(String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            return null;
        }
        if (CACHE.containsKey(pattern)) {
            return CACHE.get(pattern);
        } else {
            synchronized (lock) {
                if (CACHE.containsKey(pattern)) {
                    return CACHE.get(pattern);
                } else {
                    return createConfiguration(pattern);
                }
            }
        }
    }

    protected static Configuration createConfiguration(String pattern) {
        Configuration configuration = null;
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(pattern);
            //class 目录下，在第一次，翻转下，使class的配置覆盖其他的
            ArrayUtils.reverse(resources);
            for (Resource resource : resources) {
                if (configuration == null) {
                    configuration =  createConfiguration(resource.getFile());
                } else {
                    Configuration source = createConfiguration(resource.getFile());
                    ConfigurationUtils.copy(source, configuration);
                }
            }
        } catch (ConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        CACHE.put(pattern, configuration);
        return configuration;
    }

    protected static Configuration createConfiguration(File file) throws ConfigurationException {
        if (StringUtils.endsWith(file.getName(), ".properties")) {
           return new PropertiesConfiguration(file);
        } else if (StringUtils.endsWith(file.getName(), ".xml")) {
            return new XMLConfiguration(file);
        }
        return null;
    }

    public static Configuration getConfig() {
        return CACHE.get(NAME_PATTERN);
    }
}
