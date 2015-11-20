package com.piggsoft.utils;

/**
 * Created by user on 2015/11/16.
 */
public class UrlUtils {

    private static String baseUrl = ConfigUtils.getConfig().getString("url_wx_base");

    public static String tokenUrl = baseUrl + ConfigUtils.getConfig().getString("url_wx_token");

}