package com.piggsoft.manager;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.scheme.Scheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author piggsoft@163.com
 * Created by user on 2015/11/16.
 * 统一的url管理
 */
@Component
public class UrlManager {

    /**
     * base路径
     */
    @Value("${url_wx_base}")
    private String baseUrl;

    /**
     * token 路径
     */
    @Value("${url_wx_token}")
    private String tokenUrl;

    /**
     * appid
     */
    @Value("${appid}")
    private String appid;
    /**
     * secret
     */
    @Value("${secret}")
    private String secret;
    /**
     * 客服接口根路径
     */
    @Value("${url_kf_account}")
    private String kfAccountUrl;


    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getTokenUrl() {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https")
            .setHost(baseUrl)
                .setPath(tokenUrl)
                .addParameter("appid", appid)
                .addParameter("secret", secret)
                .addParameter("grant_type", "client_credential");
        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.println(new UrlManager().getTokenUrl());
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

}