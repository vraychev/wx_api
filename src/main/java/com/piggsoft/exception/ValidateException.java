package com.piggsoft.exception;

/**
 * Created by user on 2015/11/24.
 * @author piggsoft@163.com
 */
public class ValidateException extends Exception {

    /**
     * 微信返回的错误消息代码
     */
    private String errcode;
    /**
     * 微信返回的详细错误
     */
    private String errmsg;


    public ValidateException(String errcode, String errmsg) {
        super(errcode + ":" + errmsg);
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public ValidateException(Throwable cause, String errcode, String errmsg) {
        super(cause);
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public ValidateException(Throwable cause) {
        super(cause);
    }

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
