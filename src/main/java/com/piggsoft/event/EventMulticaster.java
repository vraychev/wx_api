package com.piggsoft.event;

import com.alibaba.fastjson.JSON;
import com.piggsoft.listener.WXEventListener;
import com.piggsoft.utils.BeanUtils;
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

    public void addEventListener(WXEventListener listener) {
        synchronized (this.defaultRetriever) {
            this.defaultRetriever.wxEventListeners.add(listener);
            this.retrieverCache.clear();
        }
    }

    public void addEventListenerBean(String listenerBeanName) {
        synchronized (this.defaultRetriever) {
            addEventListener((WXEventListener) BeanUtils.newInstance(listenerBeanName));
        }
    }

    /**
     * 本次环境中一个event指允许一个监听
     * @param weEvent
     * @return
     */
    public WXEventListener getApplicationListener(WXEvent weEvent) {
        Collection<WXEventListener> listeners = getApplicationListeners(weEvent);
        if (CollectionUtils.isEmpty(listeners)) {
            return null;
        }
        if (listeners.size() > 1) {
            throw new RuntimeException("too many listener on Event : " + JSON.toJSONString(weEvent));
        }
        return listeners.iterator().next();
    }

    public Collection<WXEventListener> getApplicationListeners(WXEvent weEvent) {
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

    //是否支持该事件
    protected boolean supportsEvent(WXEventListener WXEventListener, Class<? extends WXEvent> eventType, Class<?> sourceType) {
        Class<?> typeArg = GenericTypeResolver.resolveTypeArgument(WXEventListener.getClass(), WXEventListener.class);
        return (typeArg == null || typeArg.isAssignableFrom(eventType));
    }


    private static class ListenerCacheKey {

        private final Class<?> eventType;

        private final Class<?> sourceType;

        public ListenerCacheKey(Class<?> eventType, Class<?> sourceType) {
            this.eventType = eventType;
            this.sourceType = sourceType;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
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
    private class ListenerRetriever {

        public final Set<WXEventListener> wxEventListeners;

        public ListenerRetriever() {
            this.wxEventListeners = new LinkedHashSet<WXEventListener>();
        }

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
