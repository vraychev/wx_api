package com.piggsoft.event.impl.event;

import com.piggsoft.event.annotation.XmlMsgType;
import com.piggsoft.event.impl.EventMsg;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by user on 2015/11/19.
 */
@XmlMsgType(msgType = "event", eventType = "SCAN")
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScanEvent extends EventMsg {
    @XmlElement(name = "EventKey")
    private String eventKey;
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
