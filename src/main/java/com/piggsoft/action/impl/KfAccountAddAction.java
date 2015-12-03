package com.piggsoft.action.impl;

import com.piggsoft.action.Action;
import com.piggsoft.annotation.ActionType;
import com.piggsoft.exception.ValidateException;
import com.piggsoft.manager.UrlManager;
import com.piggsoft.message.res.BaseRes;
import com.piggsoft.utils.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 添加客服帐号
 * <br>Created by user on 2015/12/01
 * @author piggsoft@163.com
 */
@Component
@ActionType("kfAccountAdd")
public class KfAccountAddAction extends Action {


    /**
     * url 管理器
     */
    @Autowired
    private UrlManager urlManager;


    @Override
    protected URI getUri() throws ValidateException {
        try {
            return urlManager.getDefaultUri(urlManager.getKfAccountUrl());
        } catch (URISyntaxException e) {
            throw new ValidateException(e);
        }
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
