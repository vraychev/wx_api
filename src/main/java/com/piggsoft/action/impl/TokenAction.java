package com.piggsoft.action.impl;

import com.piggsoft.action.Action;
import com.piggsoft.annotation.ActionType;
import com.piggsoft.message.res.AccessToken;
import com.piggsoft.utils.http.HttpMethod;
import com.piggsoft.manager.UrlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by user on 2015/11/16.
 * @author piggsoft@163.com
 */
@Component
@ActionType("token")
public class TokenAction extends Action {


    /**
     * url 管理器
     */
    @Autowired
    private UrlManager urlManager;

    @Override
    protected String getUrl() {
        return urlManager.getTokenUrl();
    }

    @Override
    protected HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    protected Class getResultType() {
        return AccessToken.class;
    }

    @Override
    protected <T> T postAction(Object result) {
        return (T) result;
    }
}
