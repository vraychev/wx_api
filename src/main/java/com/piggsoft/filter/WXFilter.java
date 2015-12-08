package com.piggsoft.filter;

import com.alibaba.fastjson.JSON;
import com.piggsoft.configuration.Context;
import com.piggsoft.configuration.EventMulticaster;
import com.piggsoft.event.WXEvent;
import com.piggsoft.annotation.parser.Parser;
import com.piggsoft.listener.WXEventListener;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXB;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * WX 拦截器
 * Created by user on 2015/11/16.
 * @author piggsoft@163.com
 */
public class WXFilter implements Filter {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WXFilter.class);

    /**
     * 默认配置地址
     */
    private static final String DEFAULT_CONFIG_LOCATION = "classpath*:wx.spring.xml";

    /**
     * 默认配置地址
     */
    private static final String CUSTOMER_CONFIG_LOCATION = "classpath*:wx.customer.spring.xml";

    /**
     * 事件分发器
     */
    private EventMulticaster multicaster;

    /**
     * 被动报文解析
     */
    private Parser parser;

    //多线程任务管理器
    //private static final ExecutorService service = Executors.newCachedThreadPool();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        WebApplicationContext parent = WebApplicationContextUtils.findWebApplicationContext(filterConfig.getServletContext());
        String configLocations = filterConfig.getInitParameter("wxConfigLocations");
        if (StringUtils.isEmpty(configLocations)) {
            Context.init(new String[]{DEFAULT_CONFIG_LOCATION, CUSTOMER_CONFIG_LOCATION}, parent);
        } else {
            String[] locations = StringUtils.split(configLocations, ",");
            ArrayUtils.add(locations, 0,  DEFAULT_CONFIG_LOCATION);
            Context.init(locations, parent);
        }

        ApplicationContext applicationContext = Context.getContext();
        multicaster = applicationContext.getBean(EventMulticaster.class);
        parser = applicationContext.getBean(Parser.class);
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String method = request.getMethod();
        //如果是get 就代表是微信服务器发来的，验证服务器有效性的请求
        if ("GET".equals(method)) {
            doValidate(servletRequest, servletResponse);
            return;
        }
        String content = StreamUtils.copyToString(servletRequest.getInputStream(), Charset.forName("UTF-8"));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("接收到的报文为：\r\n{}\r\n", content);
        }
        //解析接收的消息
        WXEvent event = parser.parse(content);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("接收到的报文为序列化后：\r\n{}\r\n", JSON.toJSONString(event));
        }
        //根据接收到的消息找到对应的监听
        WXEventListener listener = multicaster.getListener(event);
        if (listener == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("未找到对应的Listener，返回");
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
        if (response != null) {
            JAXB.marshal(response, servletResponse.getOutputStream());
        } else {
            servletResponse.getOutputStream().print("");
        }
    }

    /**
     * 验证服务器地址的有效性
     * @param servletRequest request
     * @param servletResponse response
     */
    private void doValidate(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        String signature = servletRequest.getParameter("signature");
        String timestamp = servletRequest.getParameter("timestamp");
        String nonce = servletRequest.getParameter("nonce");
        String echostr = servletRequest.getParameter("echostr");

        if (checkSignature(signature, timestamp, nonce)) {
            servletResponse.getOutputStream().print(echostr);
        } else {
            servletResponse.getOutputStream().print("");
        }
    }

    /**
     * 验证签名
     * @param signature signature
     * @param timestamp timestamp
     * @param nonce nonce
     * @return 通过true or 不通过false
     */
    private boolean checkSignature(String signature, String timestamp, String nonce) {
        String token = Context.getProps().getProperty("token");
        String[] array = new String[] {token, timestamp, nonce};
        Arrays.sort(array);
        String str = StringUtils.join(array);
        String digestStr = null;
        try {
            digestStr = Hex.encodeHexString(MessageDigest.getInstance("SHA-1").digest(org.apache.commons.codec.binary.StringUtils.getBytesUtf8(str)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return signature.equals(digestStr);
    }

    @Override
    public void destroy() {

    }
}
