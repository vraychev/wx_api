package com.piggsoft.action;

import com.piggsoft.message.res.AccessToken;
import com.piggsoft.message.req.Req;
import com.piggsoft.message.req.TokenReq;
import com.piggsoft.utils.http.HttpMethod;
import com.piggsoft.utils.http.UrlUtils;

/**
 * @author piggsoft@163.com
 * Created by user on 2015/11/16.
 */
public class TokenAction extends Action {

    @Override
    protected String getUrl() {
        return UrlUtils.tokenUrl;
    }

    @Override
    protected Req getReq() {
        return new TokenReq();
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
