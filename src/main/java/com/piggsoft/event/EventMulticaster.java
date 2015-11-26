package com.piggsoft.event;

import com.alibaba.fastjson.JSON;
import com.piggsoft.listener.WXEventListener;
import com.piggsoft.utils.bean.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.OrderComparator;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件分发器
 * <br/> 分发接收的被动消息和事件
 * <br/> 仿造Spring的事件分发器
 * <br/> Created by piggsoft on 2015/11/16.
 * @author piggsoft@163.com
 */
public class EventMulticaster {


    /**
     * 检索器监听
     */
    private final ListenerRetriever defaultRetriever = new ListenerRetriever();

    /**
     * 检索缓存
     */
    private final Map<ListenerCacheKey, ListenerRetriever> retrieverCache =
            new ConcurrentHashMap<ListenerCacheKey, ListenerRetriever>(64);

    /**
     * 添加监听器，并增加缓存
     * @param listener 用户自己实现{@link WXEventListener}
     */
    public void addEventListener(WXEventListener listener) {
        synchronized (this.defaultRetriever) {
            this.defaultRetriever.wxEventListeners.add(listener);
            this.retrieverCache.clear();
        }
    }

    /**
     * 通过类全名来添加监听器
     * @param listenerBeanName 实现{@link WXEventListener}的类全名
     */
    public void addEventListenerBean(String listenerBeanName) {
        synchronized (this.defaultRetriever) {
            addEventListener((WXEventListener) BeanUtils.newInstance(listenerBeanName));
        }
    }

    /**
     * 本次环境中一个event指允许一个监听
     * @param weEvent 事件
     * @return 选取到的listener，如果没找到返回null
     */
    public WXEventListener getListener(WXEvent weEvent) {
        Collection<WXEventListener> listeners = getListeners(weEvent);
        if (CollectionUtils.isEmpty(listeners)) {
            return null;
        }
        if (listeners.size() > 1) {
            throw new RuntimeException("too many listener on Event : " + JSON.toJSONString(weEvent));
        }
        return listeners.iterator().next();
    }

    /**
     * 根据事件来选取监听该事件的listener
     * @param weEvent {@link WXEvent}
     * @return 所有监听该事件的listener，如果没有返回空的collection
     */
    public Collection<WXEventListener> getListeners(WXEvent weEvent) {
        //事件的class
        Class<? extends WXEvent> eventType = weEvent.getClass();
        //事件调用源
        Object source = weEvent.getSource();
        //事件调用源的class
        Class<?> sourceType = (source != null ? source.getClass() : null);
        ListenerCacheKey cacheKey = new ListenerCacheKey(eventType, sourceType);
        //尝试从缓存中读取
        ListenerRetriever retriever = this.retrieverCache.get(cacheKey);
        if (retriever != null) {
            //一个检索管理下可能有多个Listener
            return retriever.getWxEventListeners();
        } else {
            retriever = new ListenerRetriever();
            LinkedList<WXEventListener> allListeners = new LinkedList<WXEventListener>();
            Set<WXEventListener> listeners;
            synchronized (this.defaultRetriever) {
                //从默认检索管理中取出所有，准备进行对比
                listeners = new LinkedHashSet<WXEventListener>(this.defaultRetriever.wxEventListeners);
            }
            for (WXEventListener listener : listeners) {
                //当支持这个事件时
                if (supportsEvent(listener, eventType, sourceType)) {
                    retriever.wxEventListeners.add(listener);
                    allListeners.add(listener);
                }
            }
            //权重排序
            OrderComparator.sort(allListeners);
            this.retrieverCache.put(cacheKey, retriever);
            return allListeners;
        }
    }

    /**
     * 判断是否支持该事件
     * @param wxEventListener 监听器
     * @param eventType 事件类型
     * @param sourceType 事件源类型
     * @return 支持 true， 不支持 false
     */
    protected boolean supportsEvent(WXEventListener wxEventListener, Class<? extends WXEvent> eventType, Class<?> sourceType) {
        Class<?> typeArg = GenericTypeResolver.resolveTypeArgument(wxEventListener.getClass(), WXEventListener.class);
        return (typeArg == null || typeArg.isAssignableFrom(eventType));
    }


    /**
     * @author piggsoft@163.com
     * 监听器缓存的key
     */
    private static final class ListenerCacheKey {

        /**
         * 事件类型
         */
        private final Class<?> eventType;

        /**
         * 事件源类型
         */
        private final Class<?> sourceType;

        private ListenerCacheKey(Class<?> eventType, Class<?> sourceType) {
            this.eventType = eventType;
            this.sourceType = sourceType;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null || !(other instanceof ListenerCacheKey)) {
                return false;
            }

            ListenerCacheKey otherKey = (ListenerCacheKey) other;
            return ObjectUtils.nullSafeEquals(this.eventType, otherKey.eventType) &&
                    ObjectUtils.nullSafeEquals(this.sourceType, otherKey.sourceType);
        }

        @Override
        public int hashCode() {
            return ObjectUtils.nullSafeHashCode(this.eventType) * 29 + ObjectUtils.nullSafeHashCode(this.sourceType);
        }
    }

    /**
     * 检索器
     * <br> 对listener进行处理
     */
    private static final class ListenerRetriever {

        /**
         * 监听器缓存
         */
        private final Set<WXEventListener> wxEventListeners;

        private ListenerRetriever() {
            this.wxEventListeners = new LinkedHashSet<WXEventListener>();
        }

        /**
         * 获取所有的监听器
         * @return 排序后的监听器，排序暂未实现
         */
        public Collection<WXEventListener> getWxEventListeners() {
            LinkedList<WXEventListener> allListeners = new LinkedList<WXEventListener>();
            for (WXEventListener listener : this.wxEventListeners) {
                allListeners.add(listener);
            }
            OrderComparator.sort(allListeners);
            return allListeners;
        }

    }

}
