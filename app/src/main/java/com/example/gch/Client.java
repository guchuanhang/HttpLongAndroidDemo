package com.example.gch;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
    public static final String SERVER_IP = "192.168.0.132";
    public static final int SERVER_PORT = 8080;

//    public static void main(String[] args) {
//        Client bootstrap = new Client(SERVER_PORT, SERVER_IP);
//    }

    private int port;
    private String host;
    private SocketChannel socketChannel;

    public Client(int port, String host) {
        this.host = host;
        this.port = port;
        start();
    }

    private void start() {

        Thread thread = new Thread(new Runnable() {
            public void run() {
                EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.channel(NioSocketChannel.class)
                        // 保持连接
                        .option(ChannelOption.SO_KEEPALIVE, true)
                        // 有数据立即发送
                        .option(ChannelOption.TCP_NODELAY, true)
                        // 绑定处理group
                        .group(eventLoopGroup).remoteAddress(host, port)
                        .handler(new SimpleClientHandler());
                // 进行连接
                ChannelFuture future;
                try {
                    future = bootstrap.connect(host, port).sync();
                    // 判断是否连接成功
                    if (future.isSuccess()) {
                        // 得到管道，便于通信
                        socketChannel = (SocketChannel) future.channel();
                        System.out.println("客户端开启成功...");
                    } else {
                        System.out.println("客户端开启失败...");
                    }
                    // 等待客户端链路关闭，就是由于这里会将线程阻塞，导致无法发送信息，所以我这里开了线程
                    future.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //优雅地退出，释放相关资源
                    eventLoopGroup.shutdownGracefully();
                }
            }
        });

        thread.start();
    }

}