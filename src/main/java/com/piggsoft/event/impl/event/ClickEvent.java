package com.piggsoft.event.impl.event;

import com.piggsoft.event.annotation.XmlMsgType;
import com.piggsoft.event.impl.EventMsg;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author piggsoft@163.com
 * Created by user on 2015/11/19.
 * 自定义菜单事件
 */
@XmlMsgType(msgType = "event", eventType = "CLICK")
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClickEvent extends EventMsg {
    /**
     * 事件KEY值，与自定义菜单接口中KEY值对应
     */
    @XmlElement(name = "EventKey")
    private String eventKey;

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }
}
