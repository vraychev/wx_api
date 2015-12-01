package com.piggsoft.message.req;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.piggsoft.manager.AccessTokenManager;
import com.piggsoft.utils.bean.BeanUtils;

import java.util.Map;

/**
 * <br>Created by fire pigg on 2015/11/26.
 * @author piggsoft@163.com
 */
public class BaseReq implements Req {

    /**
     * 转换为map
     * @return 转换后的map
     */
    public Map<String, Object> toParams() {
        return BeanUtils.beanToMap(this);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
