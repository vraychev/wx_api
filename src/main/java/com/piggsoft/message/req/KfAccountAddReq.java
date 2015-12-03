package com.piggsoft.message.req;

import com.alibaba.fastjson.annotation.JSONField;
import com.piggsoft.annotation.ActionType;

/**
 * 添加客服帐号
 * <br>Created by fire pigg on 2015/12/02.
 *
 * @author piggsoft@163.com
 */
@ActionType("kfAccountAdd")
public class KfAccountAddReq extends BaseReq {
    /**
     * kf账号
     */
    @JSONField(name = "kf_account")
    private String kfAccount;
    /**
     * 用户名
     */
    private String nickname;
    /**
     * 密码
     */
    private String password;

    public String getKfAccount() {
        return kfAccount;
    }

    public void setKfAccount(String kfAccount) {
        this.kfAccount = kfAccount;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}