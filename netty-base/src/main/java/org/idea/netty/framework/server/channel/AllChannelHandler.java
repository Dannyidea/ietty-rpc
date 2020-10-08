package org.idea.netty.framework.server.channel;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author linhao
 * @date created in 3:26 下午 2020/10/8
 */
public class AllChannelHandler {

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8,8,1, TimeUnit.MINUTES
    ,new SynchronousQueue<>(),new ThreadPoolExecutor.AbortPolicy());

    public static void channelRead(Runnable r){
        threadPoolExecutor.execute(r);
    }

    public static void shutDown(){
        threadPoolExecutor.shutdown();
    }
}
