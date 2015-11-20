package com.piggsoft.utils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by user on 2015/11/16.
 */
public class AccessTokenManager {

    private static String accessTokenCache = null;


    public static String getAccessToken() {
        if (null == accessTokenCache) {
            synchronized (AccessTokenManager.class) {
                if (null == accessTokenCache) {
                    accessTokenCache = getAccessTokenFromWX();
                    startSchedule();
                }
            }
        }
        return accessTokenCache;
    }

    private static String getAccessTokenFromWX() {
        return UUID.randomUUID().toString();
    }

    private static synchronized void startSchedule() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                AccessTokenManager.clearCache();
            }
        }, ConfigUtils.getConfig().getLong("accessTokenSchedule"));
    }

    private static synchronized void clearCache() {
        AccessTokenManager.accessTokenCache = null;
    }

}
