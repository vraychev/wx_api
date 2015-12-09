package com.piggsoft.action.impl;

/**
 * Created by user on 2015/12/9.
 */

import com.piggsoft.action.Action;
import com.piggsoft.annotation.ActionType;
import com.piggsoft.manager.UrlManager;
import com.piggsoft.message.res.BaseRes;
import com.piggsoft.utils.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * 删除客服账号
 * Created by user on 2015/12/8.
 */
@Component
@ActionType("kfAccountDel")
public class KfAccountDelAction extends Action {

    /**
     * url 管理器
     */
    @Autowired
    private UrlManager urlManager;

    @Override
    protected URI getUri() {
        return urlManager.getDefaultUri(urlManager.getKfAccountUrl(), "del");
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
