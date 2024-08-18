package com.wxz.netty.helloworld;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 通用 handler 处理 I/o 事件
 *
 * @author wxz
 * @date 18:17 2024/8/18
 */
@Slf4j
public class HandlerClientHello extends SimpleChannelInboundHandler<ByteBuf>
{
    /**
     * 处理接收到的消息
     *
     * @param channelHandlerContext
     * @param byteBuf
     * @author wxz
     * @date 18:18 2024/8/18
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception
    {
        log.info("接收到的消息：{}", byteBuf.toString(CharsetUtil.UTF_8));
    }

    /**
     * 处理 I/O 事件的异常
     *
     * @param ctx
     * @param cause
     * @author wxz
     * @date 21:37 2024/8/18
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        log.info(cause.getMessage());
        ctx.close();
    }
}
