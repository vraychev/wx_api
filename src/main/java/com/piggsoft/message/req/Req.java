package com.piggsoft.message.req;

import java.util.Map;

/**
 * <br>Created by user on 2015/11/16.
 * @author piggsoft@163.com
 */
public interface Req {



    /**
     * 转换为map
     * @return 转换后的map
     */
    Map<String, Object> toParams();

}