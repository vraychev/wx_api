package com.piggsoft.filter;

import com.alibaba.fastjson.JSON;
import com.piggsoft.event.EventMulticaster;
import com.piggsoft.listener.WXEventListener;
import com.piggsoft.listener.pool.PooledListenerFactory;
import com.piggsoft.utils.ConfigUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by user on 2015/11/16.
 */
public class WXFilter implements Filter {

    private static String WX_CONFIG_FILE = ConfigUtils.getConfig().getString("wx_config_file");

    private EventMulticaster multicaster = new EventMulticaster();

    public void init(FilterConfig filterConfig) throws ServletException {
        GenericObjectPoolConfig conf = new GenericObjectPoolConfig();
        conf.setMaxTotal(20);
        conf.setMaxIdle(10);
        GenericObjectPool pool = new GenericObjectPool<WXEventListener>(new PooledListenerFactory(), conf);
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(new Runnable() {
            public void run() {

            }
        });

        initListener();

    }

    public void initListener() {
        Configuration configuration = ConfigUtils.getConfig(WX_CONFIG_FILE);
        XMLConfiguration xmlConfig = (XMLConfiguration)configuration;
        List<HierarchicalConfiguration> hierarchicalConfigurations = xmlConfig.configurationsAt("listeners.listener");
        for (HierarchicalConfiguration hierarchicalConfiguration : hierarchicalConfigurations) {
            multicaster.addEventListenerBean(hierarchicalConfiguration.getString(""));
        }
    }

    public static void main(String[] args) {
        new WXFilter().initListener();
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    public void destroy() {

    }
}
