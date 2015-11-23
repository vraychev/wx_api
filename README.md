#微信SDK

##写在之前
[微信公众平台开发者文档](http://mp.weixin.qq.com/wiki/home/index.html)
####Get Start
1. 引入包
3. ````classpath````下加入````wx.properties````配置
```
    #用户唯一凭证
    appid = 123123
    #用户唯一凭证密钥
    secret = 1231231
```
2. ````classpath````下加入````wx.xml````配置  
    在其中加入：
```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <configuration>
      <listeners>
          <listener>com.piggsoft.listener.TextWXEventListenerTest</listener>
      </listeners>
  </configuration>
```
