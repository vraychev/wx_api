package com.piggsoft;

import com.alibaba.fastjson.JSON;
import com.piggsoft.filter.WXFilter;
import org.apache.commons.collections.ListUtils;
import org.junit.Test;

/**
 * Created by user on 2015/11/16.
 */
public class Test01 {

    @Test
    public void test01() {
       new WXFilter().initListener();
    }

}
