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
     * accessToken
     */
    @JSONField(name = "access_token")
    private String accessToken;

    public BaseReq() {
        this.accessToken = AccessTokenManager.getAccessToken();
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
