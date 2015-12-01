package com.piggsoft.utils;

import com.piggsoft.manager.AccessTokenManager;
import org.junit.Test;

/**
 * Created by user on 2015/11/24.
 */
public class AccessTokenManagerTest {

    long time;

    @Test
    public void test01() {
        time = System.currentTimeMillis();
        System.out.println(AccessTokenManager.getAccessToken());
        logTime();

        System.out.println(AccessTokenManager.getAccessToken());
        logTime();

        System.out.println(AccessTokenManager.getAccessToken());
        logTime();

        System.out.println(AccessTokenManager.getAccessToken());
        logTime();
    }

    @Test
    public void test02() throws InterruptedException {
        time = System.currentTimeMillis();
        for (int i =0; i< 10; i++) {
            System.out.println(AccessTokenManager.getAccessToken());
            logTime();
            Thread.sleep(1 * 1000);
        }
    }

    protected void logTime() {
        long time1 = System.currentTimeMillis();
        System.out.println(time1 - time);
        time = time1;
    }
}
