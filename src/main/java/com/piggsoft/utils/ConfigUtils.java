package com.piggsoft.utils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;

/**
 * Created by user on 2015/11/12.
 *
 */
public class ConfigUtils {


    private static final String NAME_PATTERN = "classpath*:**/wx.properties";
    private static Configuration configuration;

    static {
        configuration = getConfig(NAME_PATTERN);
    }

    public static Configuration getConfig(String pattern) {
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        return configuration;
    }
}
