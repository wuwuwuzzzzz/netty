package com.wxz.netty.endecoder;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 服务器端行解码器
 *
 * @author wxz
 * @date 18:17 2024/8/18
 */
@Slf4j
@ChannelHandler.Sharable
public class HandlerServerLineBase extends ChannelInboundHandlerAdapter
{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        // 处理收到的数据，并反馈消息到客户端
        String strInfo = (String) msg;
        log.info("收到客户端发过来的消息：{}", strInfo.trim());

        // 写入并发送信息到远端（客户端）
        String strDate = new SimpleDateFormat("HH:mm:ss SSS").format(new Date());
        String strMsg = "你好，客户端：" + strDate + "\n" + "33333";

        log.info("向客户端反馈的消息：{}", strMsg);
        ctx.writeAndFlush(Unpooled.copiedBuffer(strMsg, CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        // 出现异常的时候执行的动作
        log.info(cause.getMessage());
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception
    {
        // 新建立连接时触发的动作
        Channel incoming = ctx.channel();
        log.info("客户端：{}", incoming.remoteAddress() + "已连接上来");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
    {
        // 连接断开时触发的动作
        Channel incoming = ctx.channel();
        log.info("客户端：{}", incoming.remoteAddress() + "已断开");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        // 通道处于活动状态触发的动作，该方法只会在通道建立时调用一次
        Channel incoming = ctx.channel();
        log.info("客户端：{}", incoming.remoteAddress() + "在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        // 通道处于非活动状态触发的动作，该方法只会在通道建立时调用一次
        Channel incoming = ctx.channel();
        log.info("客户端：{}", incoming.remoteAddress() + "掉线");
    }
}
