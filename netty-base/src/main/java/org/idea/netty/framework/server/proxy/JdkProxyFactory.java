package org.idea.netty.framework.server.proxy;

import org.idea.netty.framework.server.common.utils.ConvertUtils;
import org.idea.netty.framework.server.config.Invocation;
import org.idea.netty.framework.server.config.ReferenceConfig;
import org.idea.netty.framework.server.rpc.provider.RpcRespData;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import static org.idea.netty.framework.server.common.IettyConsumerCache.CLIENT_RESP_MAP;


/**
 * 客户端
 * jdk实现的代理工厂
 *
 * @author linhao
 * @date created in 5:15 下午 2020/10/8
 */
public class JdkProxyFactory {

    public static <T> T getProxy(final Class interfaceClass, ReferenceConfig referenceConfig) {

        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                long clientSessionId = requestProvider(interfaceClass, referenceConfig, proxy, method, args);
                System.out.println(CLIENT_RESP_MAP);
                while (CLIENT_RESP_MAP.get(clientSessionId) != null && CLIENT_RESP_MAP.get(clientSessionId).getIettyProtocol() == null) {
                }
                RpcRespData resp = CLIENT_RESP_MAP.get(clientSessionId);
                byte[] respData = resp.getIettyProtocol().getBody();
                Type type = resp.getIettyProtocol().getType();
                CLIENT_RESP_MAP.remove(clientSessionId);
                if (type.equals(List.class)) {
                    String json = ConvertUtils.byteToString(respData);
                    return ConvertUtils.convertForListFromFile(json, String.class);
                } else if (type.equals(Integer.class)) {
                    return ConvertUtils.byteToInt(respData);
                } else if (type.equals(Long.class)) {
                    return ConvertUtils.byteToInt(respData);
                } else if (type.equals(String.class)) {
                    return ConvertUtils.byteToString(respData);
                } else {
                    throw new RuntimeException("暂时不支持其他格式数据转换");
                }
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
    public static long requestProvider(final Class interfaceClass, ReferenceConfig referenceConfig, Object proxy, Method method, Object[] args) {
        Invocation invocation = new Invocation();
        invocation.setMethodName(method.getName());
        if (args != null) {
            String[] argsType = new String[args.length];
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                argsType[i] = arg.getClass().getName();
            }
            invocation.setMethodParameterTypes(argsType);
            invocation.setArguments(args);
        }
        invocation.setServiceName(referenceConfig.getServiceName());
        invocation.setServiceClass(interfaceClass);
        invocation.setServiceVersion("1.0.0");
        invocation.setAttachments(new HashMap<>(1));
        invocation.setUrls(referenceConfig.getUrls());
        //发送请求
        return referenceConfig.doRef(invocation);
    }
}
