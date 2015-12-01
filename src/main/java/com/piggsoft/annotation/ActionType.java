package com.piggsoft.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <br>Created by fire pigg on 2015/11/30.
 *
 * @author piggsoft@163.com
 */
@Inherited
@Retention(RUNTIME)
@Target({PACKAGE, TYPE})
@Documented
public @interface ActionType {
    String value();
}
