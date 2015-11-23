package com.piggsoft.listener;

import com.piggsoft.event.WXEvent;
import com.piggsoft.event.impl.msg.TextMsg;

public class TextWXEventListenerTest implements WXEventListener<TextMsg> {
    public WXEvent onEvent(WXEvent event) {
        System.out.println("on text event");
        return null;
    }
}