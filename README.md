#微信SDK

##写在之前
[微信公众平台开发者文档](http://mp.weixin.qq.com/wiki/home/index.html)
####Get Start
1. 引入包
2. 在web.xml中加入,用来接收来自微信服务器发送过来的消息
```xml

    <filter>
        <filter-name>wxFilter</filter-name>
        <filter-class>com.piggsoft.filter.WXFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>wxFilter</filter-name>
        <url-pattern>/xx/xx</url-pattern>
    </filter-mapping>
```  

WXFilter是被动消息和事件的主入口，```url-pattern```可以任意配置，只需和公众号中的配置一致  
3. ````classpath````下加入````wx.properties````配置
```

    #用户唯一凭证
    appid = 123123
    #用户唯一凭证密钥
    secret = 1231231

```  
wx.properties 是整个SDK包的主配置文件，可以覆盖其中的任意配置（除非是你必须这样做）  
4. ````classpath````下加入````wx.xml````配置  
    在其中加入：
```xml

  <?xml version="1.0" encoding="UTF-8"?>
  <configuration>
      <listeners>
          <listener>com.piggsoft.listener.TextWXEventListenerTest</listener>
      </listeners>
  </configuration>

```