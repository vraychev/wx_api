package com.piggsoft.utils.factory;

import com.piggsoft.action.factory.ActionFactory;
import com.piggsoft.utils.bean.BeanUtils;
import com.piggsoft.utils.config.ConfigUtils;

/**
 * @author piggsoft@163.com
 * Created by user on 2015/11/20.
 * {@link ActionFactory} ActionFactory 实例获取的工具类
 */
public class ActionFactoryUtils {

    /**
     * {@link ActionFactory} 缓存的副本
     */
    private static ActionFactory ACTION_FACTORY;

    /**
     * 如果cache存在，返回cache
     * <br/>从配置文件读取actionFactoryClass， 然后实例化一个
     * @return {@link ActionFactory}
     */
    public static ActionFactory getActionFactory() {
        if (null == ACTION_FACTORY) {
            synchronized (ActionFactoryUtils.class) {
                if (null == ACTION_FACTORY) {
                    String className = ConfigUtils.getConfig().getString("actionFactoryClass");
                    ACTION_FACTORY = BeanUtils.newInstance(className);
                }
            }
        }
        return ACTION_FACTORY;
    }

}
