package com.piggsoft.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.piggsoft.action.exception.ValidateResultException;
import com.piggsoft.utils.config.ConfigUtils;
import com.piggsoft.utils.http.HttpMethod;
import com.piggsoft.utils.http.HttpUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by user on 2015/11/16.
 */
public abstract class Action {

    protected abstract String getUrl();

    protected abstract Map<String, Object> getParams();

    protected abstract HttpMethod getHttpMethod();

    protected abstract Class getResultType();

    public <T> T action() throws ValidateResultException {
        String url = getUrl();
        Map<String, Object> params = getParams();
        HttpMethod method = getHttpMethod();
        preAction(url, params, method);
        String result = null;
        switch (method) {
            case GET :
                result = HttpUtils.get(url, params);
                break;
            case POST:
                result = HttpUtils.post(url, params);
                break;
            default:
                break;
        }
        JSONObject jsonObject = JSON.parseObject(result);
        validate(jsonObject);
        Object o = TypeUtils.castToJavaBean(jsonObject, getResultType());
        return postAction(o);
    }

    protected abstract <T> T postAction(Object result);

    protected void preAction(String url, Map<String, Object> params, HttpMethod method) {
        params.put("appid", ConfigUtils.getConfig().getString("appid"));
        params.put("secret", ConfigUtils.getConfig().getString("secret"));
    }

    protected void validate(JSONObject jsonObject) throws ValidateResultException {
        String errcode = jsonObject.getString("errcode");
        if (StringUtils.isNotEmpty(errcode)) {
            String errmsg = jsonObject.getString("errmsg");
            throw new ValidateResultException(errcode, errmsg);
        }
    }
}
