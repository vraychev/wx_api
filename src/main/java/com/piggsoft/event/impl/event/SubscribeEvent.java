package com.piggsoft.event.impl.event;

import com.piggsoft.event.annotation.XmlMsgType;
import com.piggsoft.event.impl.EventMsg;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by user on 2015/11/19.
 */
@XmlMsgType(msgType = "event", eventType = "subscribe")
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubscribeEvent extends EventMsg {
}
