package com.wxz.netty.endecoder;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 客户端行解码器
 *
 * @author wxz
 * @date 10:25 2024/8/20
 */
@Slf4j
public class HandlerClientLineBase extends SimpleChannelInboundHandler<String>
{
    /**
     * 处理接收到的消息
     *
     * @param channelHandlerContext
     * @param info
     * @author wxz
     * @date 18:18 2024/8/18
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String info) throws Exception
    {
        log.info("接收到的消息：{}", info.trim());
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
        String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String strMsg = "你好，服务器" + strDate + "\n" + "22222";

        log.info("已连接上服务器，现在发送一条消息：{}", strMsg);
        ctx.writeAndFlush(Unpooled.copiedBuffer(strMsg, CharsetUtil.UTF_8));
    }
}