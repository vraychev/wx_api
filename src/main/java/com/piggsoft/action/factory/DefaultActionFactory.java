package com.piggsoft.action.factory;

import com.piggsoft.action.Action;
import com.piggsoft.action.TokenAction;

/**
 * @author piggsoft@163.com
 * Created by user on 2015/11/20.
 */
public class DefaultActionFactory implements ActionFactory {

    public Action getTokenAction() {
        return new TokenAction();
    }
}
