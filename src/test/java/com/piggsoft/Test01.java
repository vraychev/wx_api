package com.piggsoft;

import com.alibaba.fastjson.JSON;
import com.piggsoft.event.WXEvent;
import com.piggsoft.event.annotation.parser.Parser;
import com.piggsoft.filter.WXFilter;
import com.piggsoft.listener.WXEventListener;
import org.apache.commons.collections.ListUtils;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by user on 2015/11/16.
 */
public class Test01 {

    @Test
    public void test01() {
        WXFilter filter = new WXFilter();
        filter.initListener();
        String s = "<xml>\n" +
                " <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                " <FromUserName><![CDATA[fromUser]]></FromUserName> \n" +
                " <CreateTime>1348831860</CreateTime>\n" +
                " <MsgType><![CDATA[text]]></MsgType>\n" +
                " <Content><![CDATA[this is a test]]></Content>\n" +
                " <MsgId>1234567890123456</MsgId>\n" +
                " </xml>\n";
        WXEvent event = Parser.parse(s);
        Collection<WXEventListener> listeners = filter.multicaster.getApplicationListeners(event);
        for (WXEventListener listener : listeners) {
            listener.onEvent(event);
        }
    }

}
