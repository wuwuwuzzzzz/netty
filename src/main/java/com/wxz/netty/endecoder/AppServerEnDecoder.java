package com.wxz.netty.endecoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
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
public class AppServerEnDecoder
{
    private final int port;

    public AppServerEnDecoder(int port)
    {
        this.port = port;
    }

    public static void main(String[] args) throws Exception
    {
        new AppServerEnDecoder(8080).run();
    }

    public void run() throws Exception
    {
        // 负责接收客户端的连接
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // 负责处理消息 I/O
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try
        {
            // 用于启动 NIO 服务
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
             .channel(NioServerSocketChannel.class) // 通过工厂方法设计模式实例化一个 channel
             .localAddress(new InetSocketAddress(port)) // 设置监听端口
             .option(ChannelOption.SO_BACKLOG, 128) // 最大保持连接数 128， option 主要针对 boss 线程组
             .childOption(ChannelOption.SO_KEEPALIVE, true) // 启用心跳保活机制，childOption 主要是针对 worker 线程组
             .childHandler(new InitializerServerEnDeCoder());

            // 绑定服务器，该实例将提供有关 IO 操作的结果或状态的信息
            ChannelFuture f = b.bind().sync();
            log.info("在 {} 上开启监听", f.channel().localAddress());

            // 阻塞操作，closeFuture() 开启一个 channel 的监听器（这期间 channel 在进行各项工作），直到链路断开
            f.channel().closeFuture().sync();
        }
        finally
        {
            bossGroup.shutdownGracefully().sync();
            workGroup.shutdownGracefully().sync();
        }
    }
}
