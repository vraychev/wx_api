package com.piggsoft.event.impl.msg;

import com.piggsoft.event.annotation.XmlMsgType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author piggsoft@163.com
 * Created by user on 2015/11/19.
 * 小视频消息
 */
@XmlMsgType(msgType = "shortvideo")
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShortVideoMsg extends VideoMsg {
}
