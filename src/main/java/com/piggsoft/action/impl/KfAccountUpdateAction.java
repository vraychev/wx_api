package com.piggsoft.action.impl;

import com.piggsoft.action.Action;
import com.piggsoft.annotation.ActionType;
import com.piggsoft.manager.UrlManager;
import com.piggsoft.message.res.BaseRes;
import com.piggsoft.utils.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * 修改客服账号
 * Created by user on 2015/12/8.
 */
@Component
@ActionType("kfAccountUpdate")
public class KfAccountUpdateAction extends Action {

    /**
     * url 管理器
     */
    @Autowired
    private UrlManager urlManager;

    @Override
    protected URI getUri() {
        return urlManager.getDefaultUri(urlManager.getKfAccountUrl(), "update");
    }

    @Override
    protected HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected Class getResultType() {
        return BaseRes.class;
    }

    @Override
    protected <T> T postAction(Object result) {
        return (T) result;
    }

}
