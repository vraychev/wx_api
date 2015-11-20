package com.piggsoft.message.req;

import com.piggsoft.utils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by user on 2015/11/16.
 */
public class Req {

    private String appid;
    private String secret;
    private String access_token;

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

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Map<String, Object> toParams() {
        return BeanUtils.beanToMap(this);
    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
    }
}
