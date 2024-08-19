package com.wxz.netty.monitorevent;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * 服务器端启动类
 *
 * @author wxz
 * @date 21:40 2024/8/18
 */
@Slf4j
public class AppServerMonitorEvent
{
    private final int port;

    public AppServerMonitorEvent(int port)
    {
        this.port = port;
    }

    public void run() throws Exception
    {
        // Netty 的 Reactor 线程池，初始化了一个 NioEventLoop 数组，用来处理 I/O 操作
        NioEventLoopGroup group = new NioEventLoopGroup();

        try
        {
            // 用于启动 NIO 服务
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
             .channel(NioServerSocketChannel.class) // 通过工厂方法设计模式实例化一个 channel
             .localAddress(new InetSocketAddress(port)) // 设置监听端口
             .childHandler(new ChannelInitializer<SocketChannel>()
             {
                 // ChannelInitializer 是一个特殊的处理类
                 // 他的目的是帮助使用者配置一个新的 Channel，用于把许多自定义的处理类增加到 pipeline 上来
                 @Override
                 protected void initChannel(SocketChannel socketChannel) throws Exception
                 {
                     socketChannel.pipeline().addLast(new HandlerServerMonitorEvent());
                 }
             });

            // 绑定服务器，该实例将提供有关 IO 操作的结果或状态的信息
            ChannelFuture f = b.bind().sync();
            log.info("在 {} 上开启监听", f.channel().localAddress());

            // 阻塞操作，closeFuture() 开启一个 channel 的监听器（这期间 channel 在进行各项工作），直到链路断开
            f.channel().closeFuture().sync();
        }
        finally
        {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception
    {
        new AppServerMonitorEvent(8080).run();
    }
}
