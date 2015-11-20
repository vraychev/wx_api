package com.piggsoft.filter;

import com.piggsoft.event.EventMulticaster;
import com.piggsoft.event.WXEvent;
import com.piggsoft.event.annotation.parser.Parser;
import com.piggsoft.listener.WXEventListener;
import com.piggsoft.utils.ConfigUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
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

    private static String WX_CONFIG_FILE = ConfigUtils.getConfig().getString("wx_config_file");

    public EventMulticaster multicaster = new EventMulticaster();

    private static final ExecutorService service = Executors.newCachedThreadPool();

    public void init(FilterConfig filterConfig) throws ServletException {
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

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String content = StreamUtils.copyToString(servletRequest.getInputStream(), Charset.forName("UTF-8"));
        WXEvent event = Parser.parse(content);
        WXEventListener listener = multicaster.getApplicationListener(event);
        if (listener == null) {
            return;
        }
        WXEvent response = listener.onEvent(event);
        JAXB.marshal(response, servletResponse.getOutputStream());
    }

    public void destroy() {

    }
}
