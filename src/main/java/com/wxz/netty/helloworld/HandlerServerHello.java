package com.wxz.netty.helloworld;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务器端 I/O 处理类
 *
 * @author wxz
 * @date 18:17 2024/8/18
 */
@Slf4j
@ChannelHandler.Sharable
public class HandleServerHello extends ChannelInboundHandlerAdapter
{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        // 处理收到的数据，并反馈消息到客户端
        ByteBuf in = (ByteBuf) msg;
        log.debug("收到客户端发过来的消息：{}", in.toString(CharsetUtil.UTF_8));

        // 写入并发送信息到远端（客户端）
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好，我是服务器，我已经收到你发送的消息", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        // 出现异常的时候执行的动作
        log.debug(cause.getMessage());
        ctx.close();
    }
}
