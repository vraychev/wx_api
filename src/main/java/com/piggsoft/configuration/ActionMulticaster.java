package com.piggsoft.configuration;

import com.piggsoft.action.Action;
import com.piggsoft.annotation.ActionType;
import com.piggsoft.exception.ValidateException;
import com.piggsoft.message.req.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ObjectUtils;

import java.util.Map;

/**
 * <br>Created by fire pigg on 2015/11/30.
 *
 * @author piggsoft@163.com
 */
@Component
public class ActionMulticaster {

    /**
     * action 缓存
     */
    private static final Map<String, Action> GET_ACTION_CACHE =
            new ConcurrentReferenceHashMap<String, Action>(256);

    /**
     * 所有继承{@link Action}的类
     */
    @Autowired
    private Action[] actions;

    /**
     * 分发action
     *
     * @param req asd
     * @param <T> asd
     * @return asd
     * @throws ValidateException ValidateException
     */
    public <T> T multicast(Req req) throws ValidateException {
        ActionType actionType = AnnotationUtils.findAnnotation(req.getClass(), ActionType.class);
        Action action = getAction(actionType);
        if (!ObjectUtils.isEmpty(action)) {
            return action.action(req);
        }
        return null;
    }

    /**
     * 查找符合 req的 action
     *
     * @param reqActionType req 的注解
     * @return {@link Action}
     */
    private Action getAction(ActionType reqActionType) {
        String type = reqActionType.value();
        Action currentAction = GET_ACTION_CACHE.get(type);
        if (currentAction == null) {
            currentAction = getAction(type);
            if (currentAction != null) {
                GET_ACTION_CACHE.put(type, currentAction);
            }
        }
        return currentAction;
    }

    /**
     * 根据 type 查找
     * @param type type
     * @return {@link Action}
     */
    private Action getAction(String type) {
        Action currentAction = null;
        for (Action action : actions) {
            ActionType actionType = AnnotationUtils.findAnnotation(action.getClass(), ActionType.class);
            if (type.equals(actionType.value())) {
                currentAction = action;
                break;
            }
        }
        return currentAction;
    }

    /**
     * set
     * @param actions actions
     */
    public void setActions(Action[] actions) {
        this.actions = actions;
    }
}