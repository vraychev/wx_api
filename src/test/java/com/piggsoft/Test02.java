package com.piggsoft;

import com.piggsoft.listener.WXEventListener;
import com.piggsoft.listener.pool.PooledListenerFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by user on 2015/11/20.
 */
public class Test02 {

    @Test
    public void test01() {
        GenericObjectPoolConfig conf = new GenericObjectPoolConfig();
        conf.setMaxTotal(20);
        conf.setMaxIdle(10);
        GenericObjectPool pool = new GenericObjectPool<WXEventListener>(new PooledListenerFactory(), conf);
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(new Runnable() {
            public void run() {

            }
        });
    }
}
