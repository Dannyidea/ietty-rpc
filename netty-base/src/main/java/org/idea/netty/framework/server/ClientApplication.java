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
import org.idea.netty.framework.server.common.URL;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author linhao
 * @date created in 5:29 下午 2020/10/1
 */
public class ClientApplication {

    private static EventLoopGroup clientLoopGroup;
    private static Bootstrap bootstrap;
    private static Map<URL, ChannelFuture> channelFutureMap = new ConcurrentHashMap<>();

    private static List<String> server = new ArrayList<>();

    /**
     * 内部已经有针对通道创立做出缓存处理
     *
     * @param url
     * @return
     * @throws InterruptedException
     * @throws UnsupportedEncodingException
     */
    public static ChannelFuture initClient(URL url) throws InterruptedException, UnsupportedEncodingException {
        ChannelFuture channelFuture = channelFutureMap.get(url);
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
        //todo
        channelFuture = bootstrap.connect(url.getParameters().get("host"), Integer.parseInt(url.getParameters().get("port"))).sync();
        channelFutureMap.put(url, channelFuture);
        return channelFuture;
    }

}
