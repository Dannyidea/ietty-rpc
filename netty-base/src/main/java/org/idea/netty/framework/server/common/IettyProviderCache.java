package org.idea.netty.framework.server.common;

import org.idea.netty.framework.server.config.IettyProtocol;
import org.idea.netty.framework.server.rpc.provider.ProviderHandler;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author linhao
 * @Date created in 4:52 下午 2021/2/11
 */
public class IettyProviderCache {

    public static Map<Long, FutureTask<IettyProtocol>> PROVIDER_RESP_DATA = new ConcurrentHashMap<>();


    public static ProviderHandler providerHandler = new ProviderHandler(10);

    public static ExecutorService threadPool = new ThreadPoolExecutor(2, 2,
            0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(50));

    /**
     * 工作线程
     */
    public static Thread providerHandlerWorkerThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true){
                providerHandler.receiveHandler();
            }
        }
    });

}
