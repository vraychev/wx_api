package com.piggsoft.configuration;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * <br>Created by fire pigg on 2015/12/03.
 *
 * @author piggsoft@163.com
 */
public class ErrorCodeParser {


    private String tokenTimeoutErrcode = "42001";

    public void validate(String errcode, TokenTimeoutCallback tokenTimeoutCallback) {
        if (!hasErrorCode(errcode)) {
            return;
        }
        if (isSucces(errcode)) {
            return;
        }
        if (tokenIsTimeout(errcode)) {
            tokenTimeoutCallback.callback();
        }
    }

    public boolean hasErrorCode(String errcode) {
        return StringUtils.isNotEmpty(errcode);
    }

    public boolean isSucces(String errcode) {
        return false;
    }

    /**
     * 检查token是否过期
     * @param errcode 消息代码
     * @return true timeout or false
     */
    public boolean tokenIsTimeout(String errcode) {
        return tokenTimeoutErrcode.equals(errcode);
    }
}
