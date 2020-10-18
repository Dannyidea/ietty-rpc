package org.idea.netty.framework.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.bootstrap.ServerBootstrapConfig;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.idea.netty.framework.server.channel.BaseInitServerChannelHandler;
import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.config.ServiceConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author linhao
 * @date created in 4:47 下午 2020/10/1
 */
public class ServerApplication {

    /**
     * 服务端数据处理
     */
    private static EventLoopGroup serverGroup;

    /**
     * 客户端连接处理
     */
    private static EventLoopGroup clientGroup;


    private static ServerBootstrap serverBootstrap;

    private static Set<ServiceConfig> serviceConfigSet;

    private static Map<String,ServiceConfig> serviceConfigMap = new ConcurrentHashMap<>();

    private static short DEFAULT_PORT = 9099;

    private static boolean isStart = false;


    public static boolean isServerStart(){
        return isStart;
    }

    public static void setServerConfigList(Set<ServiceConfig> serviceConfigListParam){
        serviceConfigSet = serviceConfigListParam;
        serviceConfigMap = new ConcurrentHashMap<>();
        for (ServiceConfig serviceConfig : serviceConfigSet) {
            URL url = new URL();
            url.setUsername("username");
            url.setPassword("password");
            serviceConfig.export(url);
            serviceConfigMap.putIfAbsent(serviceConfig.getInterfaceName(),serviceConfig);
        }
    }


    public static ServiceConfig getServiceConfig(String serviceName){
        return serviceConfigMap.get(serviceName);
    }


    /**
     * 启动服务
     */
    public static void start() throws InterruptedException {
        serverGroup = new NioEventLoopGroup();
        clientGroup = new NioEventLoopGroup();

        serverBootstrap = new ServerBootstrap();
        //显示服务端线程组，然后再是客户端线程组
        serverBootstrap.group(serverGroup, clientGroup);
        //指定相关的通信方式
        serverBootstrap.channel(NioServerSocketChannel.class);
        //缓冲区设置，每次接收数据都是放置数据在buffer中传输
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_SNDBUF, 16 * 1024)
                .option(ChannelOption.SO_RCVBUF, 16 * 1024)
                //设置心跳机制
                .option(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.childHandler(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                System.out.println("初始化netty信息通道");
                ch.pipeline().addLast(new ObjectDecoder(1024 * 1024,
                        ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                ch.pipeline().addLast(new ObjectEncoder());
                ch.pipeline().addLast(new BaseInitServerChannelHandler());
            }
        });

        ChannelFuture channelFuture = serverBootstrap.bind(DEFAULT_PORT).sync();
        isStart = true;
        System.out.println("===================== netty-simply-server is start success ===================== ");
        channelFuture.channel().closeFuture().sync();
        serverGroup.shutdownGracefully();
        clientGroup.shutdownGracefully();
    }

}
