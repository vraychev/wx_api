package com.piggsoft.utils;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.junit.Assert;
import org.apache.commons.configuration.Configuration;
import org.junit.Test;

import java.util.List;

/**
 * Created by user on 2015/11/23.
 */
public class ConfigUtilsTest {

    long time = System.currentTimeMillis();

    @Test
    public void testGetDefaultConfig() {
        Configuration configuration = ConfigUtils.getConfig();
        Assert.assertNotNull("错误，默认配置读取错误", configuration);
        Assert.assertEquals(7200000, configuration.getInt("accessTokenSchedule"));
        Assert.assertEquals("com.piggsoft.event", configuration.getString("event.package"));
    }

    @Test
    public void testWXConfig() {
        Configuration configuration = ConfigUtils.getConfig();
        Configuration wxConfig = ConfigUtils.getConfig(configuration.getString("wx_config_file"));
        Assert.assertTrue(wxConfig instanceof XMLConfiguration);
        XMLConfiguration wxXmlConfiguration = (XMLConfiguration)wxConfig;
        List<HierarchicalConfiguration> hierarchicalConfigurations = wxXmlConfiguration.configurationsAt("listeners.listener");
        Assert.assertEquals(1, hierarchicalConfigurations.size());
        Assert.assertEquals("com.piggsoft.listener.TextWXEventListenerTest",
                hierarchicalConfigurations.get(0).getString(""));
    }


    @Test
    public void testSpeed() {
        time = System.currentTimeMillis();
        for(int i=0; i<100; i++) {
            ConfigUtils.getConfig();
            logTime();
        }
        for(int i=0; i<100; i++) {
            ConfigUtils.getConfig(ConfigUtils.getConfig().getString("wx_config_file"));
            logTime();
        }
    }


    protected void logTime() {
        long time1 = System.currentTimeMillis();
        System.out.println(time1 - time);
        time = time1;
    }
}
