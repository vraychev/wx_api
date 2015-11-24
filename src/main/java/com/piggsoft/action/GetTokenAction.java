package com.piggsoft.action;

import com.piggsoft.action.bean.rsp.AccessToken;
import com.piggsoft.utils.http.HttpMethod;
import com.piggsoft.utils.http.UrlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2015/11/16.
 */
public class GetTokenAction extends Action {

    @Override
    public String getUrl() {
        return UrlUtils.tokenUrl;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("grant_type", "client_credential ");
        return map;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public Class getResultType() {
        return AccessToken.class;
    }

    @Override
    protected <T> T postAction(Object result) {
        return null;
    }
}
