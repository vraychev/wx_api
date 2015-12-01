package com.piggsoft.action.impl;

import com.piggsoft.action.Action;
import com.piggsoft.message.res.BaseRes;
import com.piggsoft.utils.http.HttpMethod;

/**
 * 添加客服帐号
 * <br>Created by user on 2015/12/01
 * @author piggsoft@163.com
 */
public class KfAccountAddAction extends Action {

    @Override
    protected String getUrl() {
        return null;
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
        return null;
    }
}
