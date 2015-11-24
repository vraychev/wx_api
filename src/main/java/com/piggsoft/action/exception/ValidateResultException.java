package com.piggsoft.action.exception;

/**
 * Created by user on 2015/11/24.
 */
public class ValidateResultException extends Exception {

    private String errcode;
    private String errmsg;


    public ValidateResultException(String errcode, String errmsg) {
        super(errmsg);
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public ValidateResultException(Throwable cause, String errcode, String errmsg) {
        super(cause);
        this.errcode = errcode;
        this.errmsg = errmsg;
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
