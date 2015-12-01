package com.piggsoft.manager;

import com.piggsoft.configuration.ActionMulticaster;
import com.piggsoft.configuration.Context;
import com.piggsoft.message.req.TokenReq;
import com.piggsoft.message.res.AccessToken;
import com.piggsoft.exception.ValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * AccessToken 管理器
 * <br>Created by user on 2015/11/16.
 * @author piggsoft@163.com
 */
public class AccessTokenManager {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenManager.class);

    /**
     * {@link AccessToken} 缓存副本
     */
    private static AccessToken ACCESS_TOKEN_CACHE = null;

    /**
     * 获取accessToken
     * <br/>如果当前的token已经过期，会从微信服务器重新取一次
     * <br/>如果当前的token未过期，直接返回当前的token
     * @return accessToken
     */
    public static String getAccessToken() {
        if (null == ACCESS_TOKEN_CACHE) {
            synchronized (AccessTokenManager.class) {
                if (null == ACCESS_TOKEN_CACHE) {
                    ACCESS_TOKEN_CACHE = getAccessTokenFromWX();
                    startSchedule();
                }
            }
        }
        return ACCESS_TOKEN_CACHE.getAccessToken();
    }

    /**
     * 从微信服务器回去accessToken
     * @return {@link AccessToken}
     */
    private static AccessToken getAccessTokenFromWX() {
        try {
            TokenReq req = new TokenReq();
            ActionMulticaster multicaster = Context.getContext().getBean(ActionMulticaster.class);
            return multicaster.multicast(req);
        } catch (ValidateException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 开始定时任务，失效期到时，执行清理cache
     */
    private static synchronized void startSchedule() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                AccessTokenManager.clearCache();
            }
        }, ACCESS_TOKEN_CACHE.getExpiresIn() * 1000);
    }

    /**
     * 清理accessToken msgCache
     */
    private static synchronized void clearCache() {
        AccessTokenManager.ACCESS_TOKEN_CACHE = null;
    }

}
