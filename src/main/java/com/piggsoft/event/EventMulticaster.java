package com.piggsoft.event;

import com.piggsoft.listener.WXEventListener;
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
 * Created by user on 2015/11/16.
 */
public class EventMulticaster {



    private final ListenerRetriever defaultRetriever = new ListenerRetriever(false);

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
            retriever = new ListenerRetriever(true);
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

    private class ListenerRetriever {

        public final Set<WXEventListener> wxEventListeners;

        private final boolean preFiltered;

        public ListenerRetriever(boolean preFiltered) {
            this.wxEventListeners = new LinkedHashSet<WXEventListener>();
            this.preFiltered = preFiltered;
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
