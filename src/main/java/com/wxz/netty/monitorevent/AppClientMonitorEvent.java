package com.wxz.netty.monitorevent;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 客户端启动类
 *
 * @author wxz
 * @date 21:40 2024/8/18
 */
public class AppClientMonitorEvent
{
    private final String host;

    private final int port;

    public AppClientMonitorEvent(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception
    {
        new AppClientMonitorEvent("127.0.0.1", 8080).run();
    }

    /**
     * 配置相应的参数，提供连接到远端的方法
     *
     * @author wxz
     * @date 21:43 2024/8/18
     */
    public void run() throws Exception
    {
        // I/O 线程池
        NioEventLoopGroup group = new NioEventLoopGroup();

        try
        {
            // 客户端辅助启动类
            Bootstrap bs = new Bootstrap();
            bs.group(group)
              .channel(NioSocketChannel.class) // 实例化一个 Channel
              .remoteAddress(new InetSocketAddress(host, port))
              .handler(new ChannelInitializer<SocketChannel>() // 进行通道初始化配置
              {
                  @Override
                  protected void initChannel(SocketChannel socketChannel) throws Exception
                  {
                      // 添加自定义的 Handler
                      socketChannel.pipeline().addLast(new HandlerClientMonitorEvent());
                  }
              });

            // 连接到远程节点 等待连接完成
            ChannelFuture future = bs.connect().sync();

            // 阻塞操作 closeFuture() 开启一个 channel 的监听器（这期间 channel 在进行各项工作），直到链路断开
            future.channel().closeFuture().sync();

        }
        finally
        {
            group.shutdownGracefully().sync();
        }
    }
}
