package com.wxz.netty.reconnheart;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 通过接口 ChannelFutureListener 来实现客户端的自动重连，有别于断线重连
 *
 * @author wxz
 * @date 11:03 2024/8/19
 */
@Slf4j
public class ListenerClientReconnHeart implements ChannelFutureListener
{
    public AppClientReconnHeart appClientReconnHeart = new AppClientReconnHeart("127.0.0.1", 8080);

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception
    {
        if (!channelFuture.isSuccess())
        {
            EventLoop loop = channelFuture.channel().eventLoop();
            loop.schedule(() ->
            {
                try
                {
                    log.info("自动启动客户端，开始连接服务器...");
                    appClientReconnHeart.run();
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }, 5, TimeUnit.SECONDS);
        }
        else
        {
            log.info("服务器连接成功...");
        }
    }
}
