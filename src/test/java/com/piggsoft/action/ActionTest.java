package com.piggsoft.action;

import com.alibaba.fastjson.JSON;
import com.piggsoft.configuration.ActionMulticaster;
import com.piggsoft.configuration.Context;
import com.piggsoft.exception.ValidateException;
import com.piggsoft.message.req.GetKfListReq;
import com.piggsoft.message.req.KfAccountAddReq;
import com.piggsoft.message.res.BaseRes;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <br>Created by fire pigg on 2015/12/02.
 *
 * @author piggsoft@163.com
 */
public class ActionTest {

    @BeforeClass
    public static void init() {
        Context.init(new String[]{"classpath*:wx.spring.xml", "classpath:wx.customer.spring.xml"});
        ApplicationContext context = Context.getContext();
    }

    @Test
    public void testKfAccountAddAction() throws ValidateException {
        ActionMulticaster multicaster = Context.getContext().getBean(ActionMulticaster.class);
        KfAccountAddReq req = new KfAccountAddReq();
        req.setKfAccount("test@gh_ed595887caa3");
        req.setNickname("客服");
        req.setPassword(DigestUtils.md5Hex("123456q"));
        BaseRes res = multicaster.multicast(req);
        System.out.println(JSON.toJSONString(res));
    }


    @Test
    public void testGetKfListAction() throws ValidateException {
        ActionMulticaster multicaster = Context.getContext().getBean(ActionMulticaster.class);
        GetKfListReq req = new GetKfListReq();
        BaseRes res = multicaster.multicast(req);
        System.out.println(JSON.toJSONString(res));
    }

}
