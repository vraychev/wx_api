package com.piggsoft.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.piggsoft.action.exception.ValidateException;
import com.piggsoft.utils.config.ConfigUtils;
import com.piggsoft.utils.http.HttpMethod;
import com.piggsoft.utils.http.HttpUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author piggsoft@163.com
 * Created by user on 2015/11/16.
 */
public abstract class Action {

    /**
     * 获取当前action对应的微信服务器的完整url
     * <br/> 从 {@link com.piggsoft.utils.http.UrlUtils} 取
     * @return 完整url
     */
    protected abstract String getUrl();

    /**
     * 获取本次操作需要传说的参数
     * @return 装载参数的map
     */
    protected abstract Map<String, Object> getParams();

    /**
     * 获取http的方式
     * @return post or get {@link HttpMethod}
     */
    protected abstract HttpMethod getHttpMethod();

    /**
     * 返回结果对应的java type
     * @return java type
     */
    protected abstract Class getResultType();

    /**
     * 执行操作
     * <br/>抽象基本方法
     * @param <T> 类型判定
     * @return 解析后的结果
     * @throws ValidateException 当微信返回错误信息时抛出
     */
    public <T> T action() throws ValidateException {
        String url = getUrl();
        Map<String, Object> params = getParams();
        HttpMethod method = getHttpMethod();
        preAction(url, params, method);
        String result = null;
        switch (method) {
            case GET :
                result = HttpUtils.get(url, params);
                break;
            case POST:
                result = HttpUtils.post(url, params);
                break;
            default:
                break;
        }
        JSONObject jsonObject = JSON.parseObject(result);
        validate(jsonObject);
        Object o = TypeUtils.castToJavaBean(jsonObject, getResultType());
        return postAction(o);
    }

    /**
     * 结果正常返回之后
     * @param result 根据{@link Action#getResultType()}解析后的结果
     * @param <T> 类型判定
     * @return 返回处理之后的结果
     */
    protected abstract <T> T postAction(Object result);

    /**
     * 请求发出之间的操作
     * @param url 完整的url
     * @param params 参数
     * @param method http method
     */
    protected void preAction(String url, Map<String, Object> params, HttpMethod method) {
        params.put("appid", ConfigUtils.getConfig().getString("appid"));
        params.put("secret", ConfigUtils.getConfig().getString("secret"));
    }

    /**
     * 验证返回的结果
     * @param jsonObject 返回的结果
     * @throws ValidateException 当微信返回错误信息时抛出
     */
    protected void validate(JSONObject jsonObject) throws ValidateException {
        String errcode = jsonObject.getString("errcode");
        if (StringUtils.isNotEmpty(errcode)) {
            String errmsg = jsonObject.getString("errmsg");
            throw new ValidateException(errcode, errmsg);
        }
    }
}
