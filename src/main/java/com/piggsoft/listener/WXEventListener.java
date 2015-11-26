package com.piggsoft.listener;

import com.piggsoft.event.WXEvent;

/**
 * 事件监听器
 * <br>Created by user on 2015/11/16.
 * @param <Event> {@link WXEvent}
 * @author piggsoft@163.com
 */
public interface WXEventListener<Event extends WXEvent> {

    /**
     *  触发事件时
     * @param event 具体的事件
     * @return 返回的事件
     */
    WXEvent onEvent(WXEvent event);
}
