package com.piggsoft.event.impl.event;

import com.piggsoft.event.annotation.XmlMsgType;
import com.piggsoft.event.impl.EventMsg;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 扫描带参数二维码事件
 * <br>Created by user on 2015/11/19.
 * @author piggsoft@163.com
 */
@XmlMsgType(msgType = "event", eventType = "SCAN")
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScanEvent extends EventMsg {
    /**
     * 事件KEY值，qrscene_为前缀，后面为二维码的参数值
     */
    @XmlElement(name = "EventKey")
    private String eventKey;
    /**
     * 二维码的ticket，可用来换取二维码图片
     */
    @XmlElement(name = "Ticket")
    private String ticket;

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
