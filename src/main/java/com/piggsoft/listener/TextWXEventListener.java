package com.piggsoft.listener;

import com.piggsoft.event.WXEvent;
import com.piggsoft.event.impl.msg.TextMsg;

/**
 * Created by user on 2015/11/16.
 */
public class TextWXEventListener implements WXEventListener<TextMsg> {
    public WXEvent onEvent(WXEvent event) {
        System.out.println("on text event");
        return null;
    }
}
