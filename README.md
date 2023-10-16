# 介绍
> 项目中为了方便写的一些工具类，之前的好多辅助类由于电脑重装的原因，都丢失了，很可惜。这次就都放到git上管理起来
> 把这个作为平时的积累，零碎点的总结

# 知识点 (对应[路径](src/main/java/rechard/my/point))
- java调用js
[JavaInvokejs](src/main/java/rechard/my/point/javainvokejs/JavaInvokejs.java)

- 数据库的调用和mybatis log的定义化处理
[DbTest](src/main/java/rechard/my/point/dbcall/DbTest.java)
> base64和md5的区别
MD5: 全称为message digest algorithm 5(信息摘要算法), 可以进行加密, 但是不能解密, 属于单向加密, 通常用于文件校验
Base64: 把任意序列的8位字节描述为一种不易为人识别的形式, 通常用于邮件、http加密. 登陆的用户名和密码字段通过它加密, 可以进行加密和解密.