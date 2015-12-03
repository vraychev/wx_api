package com.piggsoft.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.piggsoft.exception.ValidateException;
import com.piggsoft.manager.UrlManager;
import com.piggsoft.message.req.Req;
import com.piggsoft.utils.http.HttpMethod;
import com.piggsoft.utils.http.HttpUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;

/**
 * Created by user on 2015/11/16.
 * @author piggsoft@163.com
 */
public abstract class Action {

    /**
     * 成功code集合
     */
    private static final String[] SUCCESS_CODES = {"0"};

    /**
     * 获取当前action对应的微信服务器的完整url
     * <br/> 从 {@link UrlManager} 取
     * @return 完整url
     */
    protected abstract URI getUri() throws ValidateException;

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
     * @param req 参数
     * @param <T> 类型判定
     * @return 解析后的结果
     * @throws ValidateException 当微信返回错误信息时抛出
     */
    public <T> T action(Req req) throws ValidateException {
        URI uri = getUri();
        HttpMethod method = getHttpMethod();
        preAction(uri, req, method);
        String result = null;
        switch (method) {
            case GET :
                result = HttpUtils.get(uri, req.toParams());
                break;
            case POST:
                result = HttpUtils.post(uri, req.toString());
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
     * @param uri 完整的url
     * @param req req
     * @param method http method
     */
    protected void preAction(URI uri, Req req, HttpMethod method) {
    }

    /**
     * 验证返回的结果
     * @param jsonObject 返回的结果
     * @throws ValidateException 当微信返回错误信息时抛出
     */
    protected void validate(JSONObject jsonObject) throws ValidateException {
        String errcode = jsonObject.getString("errcode");
        if (StringUtils.isNotEmpty(errcode) && !ArrayUtils.contains(SUCCESS_CODES, errcode)) {
            String errmsg = jsonObject.getString("errmsg");
            throw new ValidateException(errcode, errmsg);
        }
    }
}
