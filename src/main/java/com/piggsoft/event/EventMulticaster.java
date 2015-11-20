package com.piggsoft.event;

import com.alibaba.fastjson.JSON;
import com.piggsoft.listener.WXEventListener;
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
     * 监听犬
     */
    private final ListenerRetriever defaultRetriever = new ListenerRetriever();

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
            try {
                addEventListener((WXEventListener) Class.forName(listenerBeanName).newInstance());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

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
        Class<? extends WXEvent> eventType = weEvent.getClass();
        Object source = weEvent.getSource();
        Class<?> sourceType = (source != null ? source.getClass() : null);
        ListenerCacheKey cacheKey = new ListenerCacheKey(eventType, sourceType);
        ListenerRetriever retriever = this.retrieverCache.get(cacheKey);
        if (retriever != null) {
            return retriever.getWxEventListeners();
        }
        else {
            retriever = new ListenerRetriever();
            LinkedList<WXEventListener> allListeners = new LinkedList<WXEventListener>();
            Set<WXEventListener> listeners;
            synchronized (this.defaultRetriever) {
                listeners = new LinkedHashSet<WXEventListener>(this.defaultRetriever.wxEventListeners);
            }
            for (WXEventListener listener : listeners) {
                if (supportsEvent(listener, eventType, sourceType)) {
                    retriever.wxEventListeners.add(listener);
                    allListeners.add(listener);
                }
            }
            OrderComparator.sort(allListeners);
            this.retrieverCache.put(cacheKey, retriever);
            return allListeners;
        }
    }

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
