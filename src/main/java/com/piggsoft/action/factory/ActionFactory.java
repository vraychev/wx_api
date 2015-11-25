package com.piggsoft.action.factory;

import com.piggsoft.action.Action;

/**
 * @author piggsoft@163.com
 * Created by user on 2015/11/20.
 * Action抽象工厂
 */
public interface ActionFactory {
    /**
     * 获取Token的执行器
     * @return tokenAction
     */
    Action getTokenAction();
}
