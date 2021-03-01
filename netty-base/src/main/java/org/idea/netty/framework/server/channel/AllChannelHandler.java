package org.idea.netty.framework.server.channel;

import java.util.concurrent.*;

/**
 * @author linhao
 * @date created in 3:26 下午 2020/10/8
 */
public class AllChannelHandler {

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,8,1, TimeUnit.MINUTES
    ,new SynchronousQueue<>(),new ThreadPoolExecutor.AbortPolicy());

    public static void channelRead(FutureTask futureTask){
        threadPoolExecutor.execute(futureTask);
    }

    public static void shutDown(){
        threadPoolExecutor.shutdown();
    }
}
