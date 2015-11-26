package com.piggsoft.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * @author piggsoft@163.com
 *         Created by fire pigg on 2015/11/26.
 */
public class ObjectUtils {
    /**
     * 判断object 是否为空
     * <br> 暂时内含 {@link StringUtils#isEmpty(CharSequence)}, {@link CollectionUtils#isEmpty(Collection)}
     * @param o 待检测的object
     * @return true or false
     */
    public static boolean isEmpty(Object o) {
        if (null == o) {
            return true;
        }
        if (o instanceof String) {
            return StringUtils.isEmpty((String) o);
        }
        if (o instanceof Collection) {
            return CollectionUtils.isEmpty((Collection) o);
        }
        return false;
    }

    /**
     * 判断object 是否不为空
     * <br> 暂时内含 {@link StringUtils#isEmpty(CharSequence)}, {@link CollectionUtils#isEmpty(Collection)}
     * @param o 待检测的object
     * @return true or false
     */
    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }
}
