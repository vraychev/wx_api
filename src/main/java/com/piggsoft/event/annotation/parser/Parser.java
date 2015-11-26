package com.piggsoft.event.annotation.parser;

import com.piggsoft.event.WXEvent;
import com.piggsoft.event.annotation.XmlMsgType;
import com.piggsoft.utils.bean.BeanUtils;
import com.piggsoft.utils.bean.Xml2MapUtils;
import com.piggsoft.utils.config.ConfigUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 解析事件，消息的配置，并加入到缓存，当消息到来时，解析为java bean
 * <br>Created by user on 2015/11/18.
 * @author piggsoft@163.com
 */
public class Parser {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Parser.class);

    /**
     * type == event 即为事件类型
     */
    public static final String EVENT_TYPE = "event";

    /**
     * 消息的解析缓存
     */
    public static final Map<String, Class> MSG_CACHE = new HashMap<String, Class>();
    /**
     * 事件的解析缓存
     */
    public static final Map<String, Class> EVENT_CACHE = new HashMap<String, Class>();
    /**
     * 事件非空条件的解析缓存
     */
    public static final Map<String, String[]> EVENT_CONDITION_CACHE = new HashMap<String, String[]>();

    static {
        initCache();
    }

    /**
     * 对报文进行解析
     * @param content 消息
     * @return 解析后{@link WXEvent}
     */
    public static WXEvent parse(String content) {
        Map<String, String> msg = Xml2MapUtils.xml2Map(content, "xml");
        String msgType = msg.get("msgType");
        String eventType = msg.get("event");
        //是推送事件
        if (EVENT_TYPE.equals(msgType) && StringUtils.isNotEmpty(eventType)) {
            String[] eventConditions = EVENT_CONDITION_CACHE.get(eventType);
            //根据事件类型和时间条件来判断具体是哪个事件
            Class clazz = getEventClass(msg, eventType, eventConditions);
            return createBean(clazz, msg);

        } else { //是消息
            //从cache中取到对应的class
            Class clazz = MSG_CACHE.get(msgType);
            return createBean(clazz, msg);

        }
    }

    /**
     * 获取本次事件对应的java bean
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
                return EVENT_CACHE.get(eventType + "," + StringUtils.join(event_conditions, ","));
            } else {
                return EVENT_CACHE.get(eventType);
            }
        } else {
            return EVENT_CACHE.get(eventType);
        }
    }

    /**
     * 根据class生成一个{@link WXEvent}
     * @param clazz 缓存中的class
     * @param msg 解析成map的消息
     * @return {@link WXEvent}
     */
    private static WXEvent createBean(Class clazz, Map<String, String> msg) {
        if (clazz == null) {
            return null;
        }
        WXEvent wxEvent = (WXEvent) BeanUtils.mapToBean(clazz, msg);
        return wxEvent;
    }


    /**
     * 初始化缓存
     */
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
                    EVENT_CACHE.put(xmlMsgType.eventType() + "," + condition, clazz);
                    EVENT_CONDITION_CACHE.put(xmlMsgType.eventType(), xmlMsgType.eventCondition());
                } else {
                    EVENT_CACHE.put(xmlMsgType.eventType(), clazz);
                }
            } else {
                MSG_CACHE.put(xmlMsgType.msgType(), clazz);
            }
        }
    }

    /**
     * 根据配置，扫描指定包下面的类，找到所有 标有{@link XmlMsgType}注解的类
     * @return {@link Set<Class>}
     */
    private static Set<Class> scanFieldClass() {
        return ClassScaner.scan(ConfigUtils.getConfig().getString("event.package"), XmlMsgType.class);
    }

}
