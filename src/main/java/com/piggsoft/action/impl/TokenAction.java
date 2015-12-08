package com.piggsoft.action.impl;

import com.piggsoft.action.Action;
import com.piggsoft.annotation.ActionType;
import com.piggsoft.manager.UrlManager;
import com.piggsoft.message.res.AccessToken;
import com.piggsoft.utils.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by user on 2015/11/16.
 *
 * @author piggsoft@163.com
 */
@Component
@ActionType("token")
public class TokenAction extends Action {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAction.class);

    /**
     * appid
     */
    @Value("${appid}")
    private String appid;
    /**
     * secret
     */
    @Value("${secret}")
    private String secret;
    /**
     * url 管理器
     */
    @Autowired
    private UrlManager urlManager;


    @Override
    protected URI getUri(){
        try {
            return urlManager.getUriBuilder()
                    .setPath(urlManager.getTokenUrl())
                    .addParameter("appid", appid)
                    .addParameter("secret", secret)
                    .addParameter("grant_type", "client_credential")
                    .build();
        } catch (URISyntaxException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
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
