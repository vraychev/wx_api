package com.piggsoft.message.req;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.piggsoft.annotation.ActionType;
import com.piggsoft.configuration.Context;
import com.piggsoft.utils.bean.BeanUtils;

import java.util.Map;

/**
 * <br>Created by user on 2015/11/16.
 * @author piggsoft@163.com
 */
@ActionType("token")
public class TokenReq implements Req {

    /**
     * 静态APP_ID
     */
    private static final String APP_ID = Context.getProps().getProperty("appid");

    /**
     * 静态SECRET
     */
    private static final String SECRET = Context.getProps().getProperty("secret");

    /**
     * appid
     */
    private String appid;
    /**
     * secret
     */
    private String secret;
    /**
     * grant_type
     */
    @JSONField(name = "grant_type")
    private String grantType;

    public TokenReq() {
        this.appid = APP_ID;
        this.secret = SECRET;
        this.grantType = "client_credential";
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

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
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
