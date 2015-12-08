package com.piggsoft.message.res;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * <br>Created by fire pigg on 2015/12/08.
 *
 * @author piggsoft@163.com
 */
public class GetKfListRes {

    @JSONField(name = "kf_list")
    private List<Kf> kfList;

    public List<Kf> getKfList() {
        return kfList;
    }

    public void setKfList(List<Kf> kfList) {
        this.kfList = kfList;
    }

    public static class Kf {
        @JSONField(name = "kf_account")
        private String kfAccount;
        @JSONField(name = "kf_nick")
        private String kfNick;
        @JSONField(name = "kf_id")
        private String kfId;
        @JSONField(name = "kf_head_img_url")
        private String kfHeadImgUrl;

        public String getKfAccount() {
            return kfAccount;
        }

        public void setKfAccount(String kfAccount) {
            this.kfAccount = kfAccount;
        }

        public String getKfHeadImgUrl() {
            return kfHeadImgUrl;
        }

        public void setKfHeadImgUrl(String kfHeadImgUrl) {
            this.kfHeadImgUrl = kfHeadImgUrl;
        }

        public String getKfId() {
            return kfId;
        }

        public void setKfId(String kfId) {
            this.kfId = kfId;
        }

        public String getKfNick() {
            return kfNick;
        }

        public void setKfNick(String kfNick) {
            this.kfNick = kfNick;
        }
    }
}
