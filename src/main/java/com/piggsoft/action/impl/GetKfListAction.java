package com.piggsoft.action.impl;

import com.piggsoft.action.Action;
import com.piggsoft.annotation.ActionType;
import com.piggsoft.exception.ValidateException;
import com.piggsoft.manager.UrlManager;
import com.piggsoft.message.res.GetKfListRes;
import com.piggsoft.utils.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * <br>Created by fire pigg on 2015/12/08.
 *
 * @author piggsoft@163.com
 */
@Component
@ActionType("kfList")
public class GetKfListAction extends Action {

    /**
     * url 管理器
     */
    @Autowired
    private UrlManager urlManager;

    @Override
    protected URI getUri() {
        return urlManager.getDefaultUri(urlManager.getCgiPath(), "customservice", "getkflist");
    }

    @Override
    protected HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    protected Class getResultType() {
        return GetKfListRes.class;
    }

    @Override
    protected <T> T postAction(Object result) {
        return (T) result;
    }
}
