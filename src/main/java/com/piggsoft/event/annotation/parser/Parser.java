package com.piggsoft.event.annotation.parser;

import com.piggsoft.event.WXEvent;
import com.piggsoft.event.annotation.XmlMsgType;
import com.piggsoft.utils.ConfigUtils;
import com.piggsoft.utils.Xml2MapUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by user on 2015/11/18.
 */
public class Parser {

    private static final Logger LOGGER = LoggerFactory.getLogger(Parser.class);

    public static final String EVENT_TYPE = "event";

    public static final Map<String, Class> cache = new HashMap<String, Class>();
    public static final Map<String, Class> event_cache = new HashMap<String, Class>();
    public static final Map<String, String[]> event_condition_cache = new HashMap<String, String[]>();

    static {
        initCache();
    }

    /**
     * 对报文进行解析
     * @param content
     * @return
     */
    public static WXEvent parse(String content) {
        Map<String, String> msg = Xml2MapUtils.xml2Map(content);
        String msgType = msg.get("msgType");
        String eventType = msg.get("event");
        //是推送事件
        if (EVENT_TYPE.equals(msgType) && StringUtils.isNotEmpty(eventType)) {
            String[] event_conditions = event_condition_cache.get(eventType);
            //根据事件类型和时间条件来判断具体是哪个事件
            Class clazz = getEventClass(msg, eventType, event_conditions);
            return createBean(clazz, msg);

        } else { //是消息
            //从cache中取到对应的class
            Class clazz = cache.get(msgType);
            return createBean(clazz, msg);

        }
    }

    /**
     *
     * @param msg 报文解析后的map
     * @param eventType 事件类型
     * @param event_conditions 事件条件
     * @return 对应的事件
     */
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
            //将map的
            BeanUtils.populate(o, msg);
            return (WXEvent)o;
        } catch (InstantiationException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }



    private static void initCache() {
        Set<Class> classes = scanFieldClass();
        if (CollectionUtils.isEmpty(classes)) {
            return;
        }
        for (Class clazz : classes) {
            //获取注解信息
            XmlMsgType xmlMsgType = (XmlMsgType) clazz.getAnnotation(XmlMsgType.class);
            //是不是事件类型
            if (EVENT_TYPE.equals(xmlMsgType.msgType())) {
                if (ArrayUtils.isNotEmpty(xmlMsgType.eventCondition())) {
                    String condition = StringUtils.join(xmlMsgType.eventCondition(), ",");
                    //cache中放入对照关系
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

    /**
     * 根据配置，扫描指定包下面的类，找到所有 标有{@link XmlMsgType}注解的类
     * @return
     */
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
