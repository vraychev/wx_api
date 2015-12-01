package com.piggsoft.annotation.parser;

import com.piggsoft.annotation.XmlMsgType;
import com.piggsoft.event.WXEvent;
import com.piggsoft.utils.bean.BeanUtils;
import com.piggsoft.utils.bean.Xml2MapUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.Map;
import java.util.Set;

/**
 * 解析事件，消息的配置，并加入到缓存，当消息到来时，解析为java bean
 * <br>Created by user on 2015/11/18.
 * @author piggsoft@163.com
 */
@Component
public class Parser implements InitializingBean{

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Parser.class);

    /**
     * type == event 即为事件类型
     */
    private static final String EVENT_TYPE = "event";

    /**
     * 消息的解析缓存
     */
    private Map<String, Class> msgCache = new ConcurrentReferenceHashMap<String, Class>();
    /**
     * 事件的解析缓存
     */
    private Map<String, Class> eventCache = new ConcurrentReferenceHashMap<String, Class>();
    /**
     * 事件非空条件的解析缓存
     */
    private Map<String, String[]> eventConditionCache = new ConcurrentReferenceHashMap<String, String[]>();

    /**
     * 事件base包
     */
    @Value("${event.package}")
    private String eventPackage;

    /**
     * 对报文进行解析
     * @param content 消息
     * @return 解析后{@link WXEvent}
     */
    public WXEvent parse(String content) {
        Map<String, String> msg = Xml2MapUtils.xml2Map(content, "xml");
        String msgType = msg.get("msgType");
        String eventType = msg.get("event");
        //是推送事件
        if (EVENT_TYPE.equals(msgType) && StringUtils.isNotEmpty(eventType)) {
            String[] eventConditions = eventConditionCache.get(eventType);
            //根据事件类型和时间条件来判断具体是哪个事件
            Class clazz = getEventClass(msg, eventType, eventConditions);
            return createBean(clazz, msg);

        } else { //是消息
            //从cache中取到对应的class
            Class clazz = msgCache.get(msgType);
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
    private Class getEventClass(Map<String, String> msg, String eventType, String[] event_conditions) {
        if (ArrayUtils.isNotEmpty(event_conditions)) {
            boolean isValidate = true;
            for (String condition : event_conditions) {
                if (!msg.containsKey(condition)) {
                    isValidate = false;
                    break;
                }
            }
            if (isValidate) {
                return eventCache.get(eventType + "," + StringUtils.join(event_conditions, ","));
            } else {
                return eventCache.get(eventType);
            }
        } else {
            return eventCache.get(eventType);
        }
    }

    /**
     * 根据class生成一个{@link WXEvent}
     * @param clazz 缓存中的class
     * @param msg 解析成map的消息
     * @return {@link WXEvent}
     */
    private WXEvent createBean(Class clazz, Map<String, String> msg) {
        if (clazz == null) {
            return null;
        }
        return (WXEvent) BeanUtils.mapToBean(clazz, msg);
    }


    /**
     * 初始化缓存
     */
    private void initCache() {
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
                    eventCache.put(xmlMsgType.eventType() + "," + condition, clazz);
                    eventConditionCache.put(xmlMsgType.eventType(), xmlMsgType.eventCondition());
                } else {
                    eventCache.put(xmlMsgType.eventType(), clazz);
                }
            } else {
                msgCache.put(xmlMsgType.msgType(), clazz);
            }
        }
    }

    /**
     * 根据配置，扫描指定包下面的类，找到所有 标有{@link XmlMsgType}注解的类
     * @return {@link Set<Class>}
     */
    @SuppressWarnings({"unchecked"})
    private Set<Class> scanFieldClass() {
        return ClassScaner.scan(eventPackage, XmlMsgType.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initCache();
    }
}
