package com.piggsoft.event.annotation.parser;

import com.piggsoft.event.WXEvent;
import com.piggsoft.event.annotation.XmlMsgType;
import com.piggsoft.utils.ConfigUtils;
import com.piggsoft.utils.Xml2MapUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by user on 2015/11/18.
 */
public class Parser {

    public static final String EVENT_TYPE = "event";

    public static final Map<String, Class> cache = new HashMap<String, Class>();
    public static final Map<String, Class> event_cache = new HashMap<String, Class>();
    public static final Map<String, String[]> event_condition_cache = new HashMap<String, String[]>();

    static {
        initCache();
    }

    public static WXEvent parse(String content) {
        Map<String, String> msg = Xml2MapUtils.xml2Map(content);
        String msgType = msg.get("msgType");
        String eventType = msg.get("event");
        //是推送事件
        if (EVENT_TYPE.equals(msgType) && StringUtils.isNotEmpty(eventType)) { //是推送事件，是消息
            String[] event_conditions = event_condition_cache.get(eventType);
            Class clazz = getEventClass(msg, eventType, event_conditions);
            return createBean(clazz, msg);

        } else { //是消息
            Class clazz = cache.get(msgType);
            return createBean(clazz, msg);

        }
    }

    private static Class getEventClass(Map<String, String> msg, String eventType, String[] event_conditions) {
        if (ArrayUtils.isNotEmpty(event_conditions)) {
            boolean isValidate = true;
            for (String condition : event_conditions) {
                if (!msg.containsKey(condition)) {
                    isValidate = false;
                    break;
                }
            }
            if (isValidate) {
                return event_cache.get(eventType + "," + StringUtils.join(event_conditions, ","));
            } else {
                return event_cache.get(eventType);
            }
        } else {
            return event_cache.get(eventType);
        }
    }

    private static WXEvent createBean(Class clazz, Map<String, String> msg) {
        if (clazz == null) {
            return null;
        }
        try {
            Object o = clazz.newInstance();
            BeanUtils.populate(o, msg);
            return (WXEvent)o;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }



    private static void initCache() {
        Set<Class> classes = scanFieldClass();
        if (CollectionUtils.isEmpty(classes)) {
            return;
        }
        for (Class clazz : classes) {
            XmlMsgType xmlMsgType = (XmlMsgType) clazz.getAnnotation(XmlMsgType.class);
            if (EVENT_TYPE.equals(xmlMsgType.msgType())) {
                if (ArrayUtils.isNotEmpty(xmlMsgType.eventCondition())) {
                    String condition = StringUtils.join(xmlMsgType.eventCondition(), ",");
                    event_cache.put(xmlMsgType.eventType() + "," + condition, clazz);
                    event_condition_cache.put(xmlMsgType.eventType(), xmlMsgType.eventCondition());
                } else {
                    event_cache.put(xmlMsgType.eventType(), clazz);
                }
            } else {
                cache.put(xmlMsgType.msgType(), clazz);
            }
        }
    }

    private static Set<Class> scanFieldClass() {
        return ClassScaner.scan(ConfigUtils.getConfig().getString("event.package"), XmlMsgType.class);
    }


    public static void main(String[] args) throws Exception {
        String s = "<xml>\n" +
                " <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                " <FromUserName><![CDATA[fromUser]]></FromUserName> \n" +
                " <CreateTime>1348831860</CreateTime>\n" +
                " <MsgType><![CDATA[text]]></MsgType>\n" +
                " <Content><![CDATA[this is a test]]></Content>\n" +
                " <MsgId>1234567890123456</MsgId>\n" +
                " </xml>\n";
        WXEvent event = parse(s);
        System.out.println(event);

        s = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
                "<CreateTime>123456789</CreateTime>\n" +
                "<MsgType><![CDATA[event]]></MsgType>\n" +
                "<Event><![CDATA[subscribe]]></Event>\n" +
                "</xml>\n";
        event = parse(s);
        System.out.println(event);

        s = "<xml><ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
                "<CreateTime>123456789</CreateTime>\n" +
                "<MsgType><![CDATA[event]]></MsgType>\n" +
                "<Event><![CDATA[subscribe]]></Event>\n" +
                "<EventKey><![CDATA[qrscene_123123]]></EventKey>\n" +
                "<Ticket><![CDATA[TICKET]]></Ticket>\n" +
                "</xml>";
        event = parse(s);
        System.out.println(event);
    }

}
