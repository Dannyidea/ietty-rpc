package org.idea.netty.framework.server.proxy;

import org.idea.netty.framework.server.config.Invocation;
import org.idea.netty.framework.server.config.ReferenceConfig;
import org.idea.netty.framework.server.rpc.RpcContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * jdk实现的代理工厂
 * @author linhao
 * @date created in 5:15 下午 2020/10/8
 */
public class JdkProxyFactory {

    //响应数据
    public static Object returnValue;

    public static Map<String, RpcContext> RPC_CONTEXT_MAP = new ConcurrentHashMap<>();

    private static ExecutorService requestHandlerExecutorService = new ThreadPoolExecutor(2, 2, 30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(20), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"ierry-jdk-proxy-thread");
        }
    });

    private static ExecutorService responseHandlerExecutorService = new ThreadPoolExecutor(2, 2, 30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(20), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"ierry-jdk-proxy-thread");
        }
    });

    public static <T> T getProxy(final Class interfaceClass,ReferenceConfig referenceConfig){

        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),new Class[]{interfaceClass},new InvocationHandler(){
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                requestHandlerExecutorService.submit(new Runnable() {
//                    @Override
//                    public void run() {
//                        requestProvider (interfaceClass,referenceConfig,proxy,method,args);
//                        RPC_CONTEXT_MAP.put(referenceConfig.getRequestId(),new RpcContext(false,null));
//                    }
//                });
//                System.out.println("test");
                int j =0;
                for(;;){
                    j++;
                    if(2>3){
                        break;
                    }
                }
//                while (!(RPC_CONTEXT_MAP.get(referenceConfig.getRequestId())!=null && RPC_CONTEXT_MAP.get(referenceConfig.getRequestId()).isFinish())){
//                    System.out.println("wait");
//                    System.out.println(RPC_CONTEXT_MAP.get(referenceConfig.getRequestId()));
//                }
                return RPC_CONTEXT_MAP.get(referenceConfig.getRequestId()).getResponseData();
            }
        });
    }

    /**
     * 请求服务端数据
     *
     * @param interfaceClass
     * @param referenceConfig
     * @param proxy
     * @param method
     * @param args
     */
    public static void requestProvider(final Class interfaceClass,ReferenceConfig referenceConfig,Object proxy, Method method, Object[] args){
        System.out.println("执行代理方法"+method.getName());
        Invocation invocation = new Invocation();
        invocation.setMethodName(method.getName());
        if(args!=null){
            String[] argsType = new String[args.length];
            for (int i =0;i<args.length;i++) {
                Object arg = args[i];
                argsType[i] = arg.getClass().getName();
            }
            invocation.setMethodParameterTypes(argsType);
            invocation.setArguments(args);
        }
        invocation.setServiceName(referenceConfig.getInterfaceName());
        invocation.setServiceClass(interfaceClass);
        invocation.setServiceVersion("1.0.0");
        invocation.setAttachments(new HashMap<>(1));

        //发送请求
        referenceConfig.doRef(invocation);
    }
}
