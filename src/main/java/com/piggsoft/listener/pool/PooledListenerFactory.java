package com.piggsoft.listener.pool;

import com.piggsoft.listener.TextWXEventListener;
import com.piggsoft.listener.WXEventListener;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * Created by user on 2015/11/16.
 */
public class PooledListenerFactory extends BasePooledObjectFactory<WXEventListener> {



    @Override
    public WXEventListener create() throws Exception {
        return new TextWXEventListener();
    }

    @Override
    public PooledObject<WXEventListener> wrap(WXEventListener WXEventListener) {
        return new DefaultPooledObject<WXEventListener>(WXEventListener);
    }
}
