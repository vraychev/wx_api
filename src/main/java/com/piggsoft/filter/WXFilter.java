package com.piggsoft.filter;

import com.alibaba.fastjson.JSON;
import com.piggsoft.event.EventMulticaster;
import com.piggsoft.event.WXEvent;
import com.piggsoft.event.annotation.parser.Parser;
import com.piggsoft.listener.WXEventListener;
import com.piggsoft.utils.ConfigUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.xml.bind.JAXB;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by user on 2015/11/16.
 */
public class WXFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WXFilter.class);

    //配置文件的路径，默认是classpath下
    private static String WX_CONFIG_FILE = ConfigUtils.getConfig().getString("wx_config_file");

    //事件分发器
    public EventMulticaster multicaster = new EventMulticaster();

    //多线程任务管理器
    private static final ExecutorService service = Executors.newCachedThreadPool();

    public void init(FilterConfig filterConfig) throws ServletException {
        init();

    }

    public void init() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("WXFilter 开始初始化");
        }
        Configuration configuration = ConfigUtils.getConfig(WX_CONFIG_FILE);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("读取配置xml文件完成");
        }
        XMLConfiguration xmlConfig = (XMLConfiguration)configuration;
        List<HierarchicalConfiguration> hierarchicalConfigurations = xmlConfig.configurationsAt("listeners.listener");
        for (HierarchicalConfiguration hierarchicalConfiguration : hierarchicalConfigurations) {
            String listenerClazzName = hierarchicalConfiguration.getString("");
            multicaster.addEventListenerBean(listenerClazzName);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Listener {} 注册完成", listenerClazzName);
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("WXFilter 初始化结束");
        }
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String content = StreamUtils.copyToString(servletRequest.getInputStream(), Charset.forName("UTF-8"));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("接收到的报文为：\r\n{}\r\n", content);
        }
        WXEvent event = Parser.parse(content);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("接收到的报文为序列化后：\r\n{}\r\n", JSON.toJSONString(event));
        }
        WXEventListener listener = multicaster.getApplicationListener(event);
        if (listener == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("未找到对应的listener，返回");
            }
            return;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("找到的Listener为: {}", listener.getClass().getSimpleName());
        }
        WXEvent response = listener.onEvent(event);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("事件处理后返回的结果为： {}", JSON.toJSONString(response));
        }
        JAXB.marshal(response, servletResponse.getOutputStream());
    }

    public void destroy() {

    }
}
