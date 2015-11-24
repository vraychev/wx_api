package com.piggsoft.utils.factory;

import com.piggsoft.action.factory.ActionFactory;
import com.piggsoft.utils.bean.BeanUtils;
import com.piggsoft.utils.config.ConfigUtils;

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
