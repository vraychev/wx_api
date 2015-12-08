package com.piggsoft.manager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

/**
 * @author piggsoft@163.com
 * Created by user on 2015/11/16.
 * 统一的url管理
 */
@Component
public class UrlManager {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UrlManager.class);

    /**
     *  http 请求编码
     */
    @Value("${http_encoding}")
    private String httpEncoding;
    /**
     * base路径
     */
    @Value("${host}")
    private String host;

    /**
     * cig路径
     */
    @Value("${path_cgi}")
    private String cgiPath;
    /**
     * 自助服务路径
     */
    @Value("${path_customservice}")
    private String customservicePath;
    /**
     * token 路径
     */
    @Value("${path_token}")
    private String tokenUrl;

    /**
     * 客服接口根路径
     */
    @Value("${path_kf_account}")
    private String kfAccountUrl;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getKfAccountUrl() {
        return customservicePath + kfAccountUrl;
    }

    public void setKfAccountUrl(String kfAccountUrl) {
        this.kfAccountUrl = kfAccountUrl;
    }

    public String getTokenUrl() {
        return cgiPath + tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getCgiPath() {
        return cgiPath;
    }

    public void setCgiPath(String cgiPath) {
        this.cgiPath = cgiPath;
    }

    public String getCustomservicePath() {
        return customservicePath;
    }

    public void setCustomservicePath(String customservicePath) {
        this.customservicePath = customservicePath;
    }

    /**
     * 获取 URIBuilder，已加入 scheme， host， encoding
     * @return {@link URIBuilder}
     */
    public URIBuilder getUriBuilder() {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https")
                .setHost(host)
                .setCharset(Charset.forName(httpEncoding));
        return builder;
    }

    /**
     * 获取 URIBuilder，已加入 scheme， host， encoding
     * @return {@link URIBuilder}
     * @throws URISyntaxException URISyntaxException
     */
    public URI getDefaultUri(String... paths) {
        URIBuilder builder = getUriBuilder();
        String path = StringUtils.join(paths, "/");
        builder.setPath(path)
                .setParameter("access_token", AccessTokenManager.getAccessToken());
        try {
            return builder.build();
        } catch (URISyntaxException e) {
            //不应该出错。
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
}