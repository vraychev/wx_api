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

    String msgType();

    String eventType() default "";

    String[] eventCondition() default {};
}
