package com.piggsoft.manager;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.scheme.Scheme;
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
        return kfAccountUrl;
    }

    public void setKfAccountUrl(String kfAccountUrl) {
        this.kfAccountUrl = kfAccountUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
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
    public URI getDefaultUri(String path) throws URISyntaxException {
        URIBuilder builder = getUriBuilder();
        builder.setPath(path)
                .setParameter("access_token", AccessTokenManager.getAccessToken());
        return builder.build();
    }
}