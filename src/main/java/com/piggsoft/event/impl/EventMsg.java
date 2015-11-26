package com.piggsoft.event.impl;

import com.piggsoft.event.WXEvent;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <br>Created by user on 2015/11/19.
 * @author piggsoft@163.com
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class EventMsg extends WXEvent {

    /**
     * 事件类型
     */
    @XmlElement(name = "Event")
    private String event;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
