# HttpLongAndroidDemo
使用Netty在安卓客户端如何实现聊天功能

使用Handler进行线程间通信写起来比较复杂。


为了便于演示效果，这是使用了EventBus 将数据传递到UI线程。展示到界面上。

对于粘包等问题，可以采用自定义结构体（添加一个字段记录结构体大小）

注意： 需要修改SERVER_IP  SERVER_PORT 为自己服务器使用的ip地址和端口。   

确保手机和服务器在同一个WIFI下！
