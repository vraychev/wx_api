package com.piggsoft.utils.http;

import com.piggsoft.utils.config.ConfigUtils;

/**
 * @author piggsoft@163.com
 * Created by user on 2015/11/16.
 * 统一的url管理
 */
public class UrlManager {

    /**
     * base路径
     */
    private static String BASE_URL = ConfigUtils.getConfig().getString("url_wx_base");

    /**
     * token 路径
     */
    private static String TOKEN_URL = BASE_URL + ConfigUtils.getConfig().getString("url_wx_token");

    public static String getTokenUrl() {
        return TOKEN_URL;
    }
}