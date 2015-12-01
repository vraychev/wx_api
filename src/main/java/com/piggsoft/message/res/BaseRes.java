package com.piggsoft.message.res;

/**
 * 基础返回数据
 * <br>Created by user on 15-12-1
 *
 * @author piggsoft@163.com
 */
public class BaseRes {
    /**
     * 响应代码
     */
    private String errcode;
    /**
     * 响应消息
     */
    private String errmsg;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
