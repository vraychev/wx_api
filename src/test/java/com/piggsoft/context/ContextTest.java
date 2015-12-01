package com.piggsoft.context;

import com.alibaba.fastjson.JSON;
import com.piggsoft.configuration.ActionMulticaster;
import com.piggsoft.configuration.Context;
import com.piggsoft.exception.ValidateException;
import com.piggsoft.message.req.Req;
import com.piggsoft.message.req.TokenReq;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

/**
 * <br>Created by fire pigg on 2015/12/01.
 *
 * @author piggsoft@163.com
 */
public class ContextTest {

    @Test
    public void test01() throws ValidateException {
        Context.init(new String[]{"classpath*:wx.spring.xml", "classpath:wx.customer.spring.xml"});
        ApplicationContext context = Context.getContext();
        ActionMulticaster multicaster = context.getBean(ActionMulticaster.class);
        Req req = new TokenReq();
        Object o = multicaster.multicast(req);
        System.out.println(JSON.toJSONString(o));
    }

    @Test
    public void test02() {
        Context.init(new String[]{"classpath*:wx.spring.xml", "classpath:wx.customer.spring.xml"});
        System.out.println(Context.getProps().getProperty("appid"));
    }
}
