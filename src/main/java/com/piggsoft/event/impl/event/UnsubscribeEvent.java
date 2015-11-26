package com.piggsoft.event.impl.event;

import com.piggsoft.event.annotation.XmlMsgType;
import com.piggsoft.event.impl.EventMsg;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author piggsoft@163.com
 * Created by user on 2015/11/19.
 * 取消关注事件
 */
@XmlMsgType(msgType = "event", eventType = "unsubscribe")
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnsubscribeEvent extends EventMsg {
}
