package com.piggsoft.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.EventObject;

/**
 * Created by user on 2015/11/16.
 * @author piggsoft@163.com
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WXEvent extends EventObject {

    /**
     * 消息类型
     */
    @XmlElement(name = "MsgType")
    private String msgType;
    /**
     * 发送给谁
     */
    @XmlElement(name = "ToUserName")
    private String toUserName;
    /**
     * 是谁发出的
     */
    @XmlElement(name = "FromUserName")
    private String fromUserName;
    /**
     * 消息创建时间
     */
    @XmlElement(name = "CreateTime")
    private String createTime;
    /**
     * id, 可以用来去重
     */
    @XmlElement(name = "MsgId")
    private String msgId;

    public WXEvent() {
        this(new Object());
    }

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public WXEvent(Object source) {
        super(source);
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }
}
