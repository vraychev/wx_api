package com.piggsoft.utils;

import com.piggsoft.action.factory.ActionFactory;

/**
 * Created by user on 2015/11/20.
 */
public class ActionFactoryUtils {

    private static ActionFactory actionFactory;

    public static ActionFactory getActionFactory() {
        if (null == actionFactory) {
            synchronized (ActionFactoryUtils.class) {
                if (null == actionFactory) {
                    String className = ConfigUtils.getConfig().getString("actionFactoryClass");
                    actionFactory = BeanUtils.newInstance(className);
                }
            }
        }
        return actionFactory;
    }

}
