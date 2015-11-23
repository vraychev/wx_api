package com.piggsoft.event.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by user on 2015/11/18.
 */
@Inherited
@Retention(RUNTIME)
@Target({PACKAGE, TYPE})
@Documented
public @interface XmlMsgType {
    /**
     * 消息类型
     * @return
     */
    String msgType();

    /**
     * 事件类型
     * @return
     */
    String eventType() default "";

    /**
     * 事件条件（非空条件）
     * @return
     */
    String[] eventCondition() default {};
}
