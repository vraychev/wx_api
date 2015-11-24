package com.piggsoft.utils;

import com.piggsoft.action.bean.rsp.AccessToken;
import com.piggsoft.action.exception.ValidateResultException;
import com.piggsoft.utils.config.ConfigUtils;
import com.piggsoft.utils.factory.ActionFactoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by user on 2015/11/16.
 */
public class AccessTokenManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenManager.class);

    private static AccessToken accessTokenCache = null;

    public static final String ACCESS_TOKEN_KEY = "access_token";


    public static String getAccessToken() {
        if (null == accessTokenCache) {
            synchronized (AccessTokenManager.class) {
                if (null == accessTokenCache) {
                    accessTokenCache = getAccessTokenFromWX();
                    startSchedule();
                }
            }
        }
        return accessTokenCache.getAccess_token();
    }

    private static AccessToken getAccessTokenFromWX() {
        try {
            return ActionFactoryUtils.getActionFactory().getTokenAction().action();
        } catch (ValidateResultException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    private static synchronized void startSchedule() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                AccessTokenManager.clearCache();
            }
        }, accessTokenCache.getExpires_in() * 1000);
    }

    private static synchronized void clearCache() {
        AccessTokenManager.accessTokenCache = null;
    }

}
