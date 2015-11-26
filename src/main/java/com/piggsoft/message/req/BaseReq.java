package com.piggsoft.message.req;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.piggsoft.utils.AccessTokenManager;
import com.piggsoft.utils.bean.BeanUtils;
import com.piggsoft.utils.config.ConfigUtils;

import java.util.Map;

/**
 * <br>Created by fire pigg on 2015/11/26.
 * @author piggsoft@163.com
 */
public class BaseReq implements Req {


    /**
     * 静态APP_ID
     */
    private static final String APP_ID = ConfigUtils.getConfig().getString("appid");

    /**
     * 静态SECRET
     */
    private static final String SECRET = ConfigUtils.getConfig().getString("secret");

    /**
     * appid
     */
    private String appid;
    /**
     * secret
     */
    private String secret;
    /**
     * accessToken
     */
    @JSONField(name = "access_token")
    private String accessToken;

    public BaseReq() {
        this.accessToken = AccessTokenManager.getAccessToken();
        this.appid = APP_ID;
        this.secret = SECRET;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * 转换为map
     * @return 转换后的map
     */
    public Map<String, Object> toParams() {
        return BeanUtils.beanToMap(this);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
