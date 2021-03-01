package org.idea.netty.framework.server.channel;

import com.alibaba.fastjson.JSON;
import org.idea.netty.framework.server.ServerApplication;
import org.idea.netty.framework.server.common.utils.ConvertUtils;
import org.idea.netty.framework.server.config.IettyProtocol;
import org.idea.netty.framework.server.config.Invocation;
import org.idea.netty.framework.server.config.ServiceConfig;
import org.idea.netty.framework.server.rpc.RpcData;
import org.idea.netty.framework.server.spi.filter.ProviderFilter;
import org.idea.netty.framework.server.spi.loader.ExtensionLoader;
import org.idea.netty.framework.server.util.StringUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static org.idea.netty.framework.server.spi.loader.ExtensionLoader.initClassInstance;
import static org.idea.netty.framework.server.util.CollectionUtils.isMapNotEmpty;

/**
 * @Author linhao
 * @Date created in 10:48 上午 2021/2/20
 */
public class ProviderHandlerTask implements Callable<IettyProtocol> {

    private RpcData rpcData;

    public ProviderHandlerTask(RpcData rpcData) {
        this.rpcData = rpcData;
    }

    /**
     * 格式转换
     *
     * @param bytes
     * @return
     */
    private Invocation toInvocationFromByte(byte[] bytes) {
        String json = new String(bytes);
        return JSON.parseObject(json, Invocation.class);
    }

    @Override
    public IettyProtocol call() {
        IettyProtocol iettyProtocol = rpcData.getIettyProtocol();
        if (iettyProtocol.getMAGIC() == 0) {
            throw new IllegalArgumentException("illegal magic check");
        }
        System.out.println("start to do job");
        Invocation invocation = toInvocationFromByte(iettyProtocol.getBody());
        try {
            //注意如果是个接口则不能进行实现
            ServiceConfig serviceConfig = ServerApplication.getServiceConfig(invocation.getServiceName());
            if (serviceConfig == null) {
                throw new RuntimeException("serviceConfig can not be null, " + invocation.getServiceName() + "" +
                        " could not be found in ServerApplication.serviceConfigMap");
            }
            Object invocationClass = serviceConfig.getInterfaceImplClass().newInstance();
            String[] methodParameterTypeArr = invocation.getMethodParameterTypes();
            Object[] argsArr = invocation.getArguments();
            Class[] clazzArr;
            Object[] args;
            if (StringUtils.isNotStringArrEmpty(methodParameterTypeArr)) {
                clazzArr = new Class[methodParameterTypeArr.length];
                args = new Object[methodParameterTypeArr.length];
                for (int i = 0; i < clazzArr.length; i++) {
                    Class parameterTypeClazz = Class.forName(methodParameterTypeArr[i]);
                    clazzArr[i] = parameterTypeClazz;
                    args[i] = argsArr[i];
                }
            } else {
                clazzArr = new Class[0];
                args = new Object[0];
            }
            Method method = invocationClass.getClass().getMethod(invocation.getMethodName(), clazzArr);

            Map<String, Class<?>> classMap = ExtensionLoader.getExtensionClassMap();
            //会优先执行服务端的过滤器，然后再执行对应方法
            if (isMapNotEmpty(classMap)) {
                String currentFilterName = serviceConfig.getFilter();
                if (StringUtils.isNotEmpty(currentFilterName)) {
                    ProviderFilter providerFilter = (ProviderFilter) initClassInstance(currentFilterName);
                    providerFilter.doFilter(invocation);
                }
            }
            if (method.getReturnType().getName().equals(Void.TYPE.getName())) {
                method.invoke(invocationClass, args);
                iettyProtocol.setType(Void.TYPE);
            } else {
                Object returnVal = method.invoke(invocationClass, args);
                if (method.getReturnType().equals(List.class)) {
                    String jsonList = ConvertUtils.convertForList(returnVal);
                    iettyProtocol.setBody(ConvertUtils.stringToBytes(jsonList));
                } else if (method.getReturnType().equals(String.class)) {
                    iettyProtocol.setBody(ConvertUtils.stringToBytes((String) returnVal));
                } else if (method.getReturnType().equals(Integer.class)) {
                    iettyProtocol.setBody(ConvertUtils.intToByte((Integer) returnVal));
                } else if (method.getReturnType().equals(Long.class)) {
                    iettyProtocol.setBody(ConvertUtils.longToByte((long) returnVal));
                }
                iettyProtocol.setType(method.getReturnType());
            }
        } catch (Exception e) {
            throw new RuntimeException("[ProviderHandlerTask] 出现未知异常,e is {}", e);
        }
        return iettyProtocol;
    }
}
