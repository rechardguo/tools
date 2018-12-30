### 刷CSDN blog流量工具

#### 思路
1.从代理平台里爬取到ip,port，主要有
- https://www.kuaidaili.com/free/inha/1  1代表页数
- https://www.xicidaili.com/nn/1 1代表页数

> 支持扩展不同的代理网站收集，注意的收集到每个proxy,要放到队列里,代码如下
`ProxyFilterCollector.put(new Proxy(ip,port));`
  校验代理是否有效的线程会从队列里获取到这个proxy并进行校验。

现在很多的代理网站会设置限制，如果ip访问过多次就不让访问了。这时候可以设置代理去访问，详细看
`ProxyTool.getContent`

2.使用ip,port作为代理取访问blog url

3.config配置
默认的配置在flush_config.properties里，支持命令模式里的
-D,例如要改变校验proxy所用的url,可以配置
-Dproxy.validation.url=www.baidu.com



#### 问题
1.必须设置timeout过期时间，否则卡死
2.日志打印太多

#### 代理知识
https://help.kuaidaili.com/wiki/

#### 参考
https://www.kuaidaili.com/
https://www.cnblogs.com/hong-fithing/p/7617855.html