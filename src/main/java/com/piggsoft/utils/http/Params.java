package com.piggsoft.utils.http;

import com.alibaba.fastjson.JSON;
import com.piggsoft.utils.config.ConfigUtils;

import java.util.HashMap;

/**
 * Created by user on 2015/11/16.
 */
public class Params extends HashMap<String, String> {
    public static Params create() {
        return new Params().init();
    }

    private Params init() {
        this.add("appid", ConfigUtils.getConfig().getString("appid"))
                .add("secret", ConfigUtils.getConfig().getString("secret"));
        return this;
    }

    public Params add(String key, String value) {
        super.put(key, value);
        return this;
    }

    public Params add(String key, int value) {
        super.put(key, String.valueOf(value));
        return this;
    }

    public Params add(String key, double value) {
        super.put(key, String.valueOf(value));
        return this;
    }

    public Params add(String key, float value) {
        super.put(key, String.valueOf(value));
        return this;
    }

    public Params add(String key, boolean value) {
        super.put(key, String.valueOf(value));
        return this;
    }

    public Params add(String key, Object value) {
        super.put(key, String.valueOf(value));
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}