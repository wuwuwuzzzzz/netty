package com.wxz.netty.monitorevent;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 通用 handler 处理 I/o 事件
 *
 * @author wxz
 * @date 18:17 2024/8/18
 */
@Slf4j
public class HandlerClientMonitorEvent extends SimpleChannelInboundHandler<ByteBuf>
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

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        // 建立连接后该 channelActive() 方法只会被调用一次，这里的逻辑：建立连接后，字符序列被发送到服务器，编码格式是 utf-8
        String strDate = new SimpleDateFormat("HH:mm:ss SSS").format(new Date());
        ctx.writeAndFlush(Unpooled.copiedBuffer("这是客户端通过 Active 方法发送的消息" + strDate + "\n", CharsetUtil.UTF_8));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        // 通道处于非活动状态触发的动作，该方法只会在通道建立时调用一次
        Channel incoming = ctx.channel();
        log.info("服务器端掉线");
    }
}
