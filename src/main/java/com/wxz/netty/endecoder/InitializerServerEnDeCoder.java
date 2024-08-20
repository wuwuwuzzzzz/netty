package com.wxz.netty.endecoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 服务器端 handler 初始化配置类，在 channel 注册到 EventLoop 后，对这个 channel 添加一些初始化的 handler
 *
 * @author wxz
 * @date 17:20 2024/8/19
 */
public class InitializerServerEnDeCoder extends ChannelInitializer<SocketChannel>
{
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception
    {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("framer", new FixedLengthFrameDecoder(CommonTools.FIXEDLENGTHFRAME_LENGTH)); // 定长解码器
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast(new HandlerServerFixedLength()); // 自定义业务逻辑 handler
    }
}