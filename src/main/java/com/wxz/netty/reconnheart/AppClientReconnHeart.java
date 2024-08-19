package com.wxz.netty.reconnheart;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 客户端启动类
 *
 * @author wxz
 * @date 21:40 2024/8/18
 */
public class AppClientReconnHeart
{
    private final String host;

    private final int port;

    public AppClientReconnHeart(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception
    {
        new AppClientReconnHeart("127.0.0.1", 8080).run();
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
                      // 超时检测
                      socketChannel.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
                      // 添加自定义的 Handler
                      socketChannel.pipeline().addLast(new HandlerClientReconnHeart());
                  }
              });

            // 连接到远程节点 等待连接完成
            ChannelFuture future = bs.connect();

            future.addListener(new ListenerClientReconnHeart());

            // 每隔 6 秒，自动向服务器发送一条消息
            while (true)
            {
                Thread.sleep(6000);
                String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                future.channel().writeAndFlush(Unpooled.copiedBuffer(strDate, CharsetUtil.UTF_8));
            }

        }
        finally
        {
            group.shutdownGracefully().sync();
        }
    }
}
