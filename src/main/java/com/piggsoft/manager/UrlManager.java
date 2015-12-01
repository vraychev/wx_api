package com.piggsoft.manager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getTokenUrl() {
        return baseUrl + tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }
}