package org.idea.netty.framework.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.idea.netty.framework.server.channel.BaseInitClientChannelHandler;

import java.io.UnsupportedEncodingException;

/**
 * @author linhao
 * @date created in 5:29 下午 2020/10/1
 */
public class ClientApplication {

    private static EventLoopGroup clientLoopGroup;
    private static Bootstrap bootstrap;
    private static final String HOST = "127.0.0.1";
    private static final short PORT = 9099;
    private static boolean channelFutureIsReady = false;
    private static ChannelFuture channelFuture;

    public static ChannelFuture initClient() throws InterruptedException, UnsupportedEncodingException {
        if (channelFuture != null) {
            return channelFuture;
        }
        clientLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(clientLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                //允许传输对象参数
                ch.pipeline().addLast(new ObjectEncoder());
                ch.pipeline().addLast(new ObjectDecoder(1024 * 1024,
                        ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                ch.pipeline().addLast(new BaseInitClientChannelHandler());
            }
        });
        channelFuture = bootstrap.connect(HOST, PORT).sync();
        return channelFuture;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public void sendMsg(byte[] msg) {
        channelFuture.channel().writeAndFlush(msg);
    }
}
