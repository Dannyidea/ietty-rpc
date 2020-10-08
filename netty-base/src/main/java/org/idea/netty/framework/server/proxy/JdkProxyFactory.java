package org.idea.netty.framework.server.proxy;

import org.idea.netty.framework.server.config.Invocation;
import org.idea.netty.framework.server.config.ReferenceConfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 * jdk实现的代理工厂
 * @author linhao
 * @date created in 5:15 下午 2020/10/8
 */
public class JdkProxyFactory {

    public static <T> T getProxy(final Class interfaceClass,ReferenceConfig referenceConfig){
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),new Class[]{interfaceClass},new InvocationHandler(){
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("执行代理方法");
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
                invocation.setServiceClass(interfaceClass);
                invocation.setServiceVersion("1.0.0");
                invocation.setAttachments(new HashMap<>(1));
                referenceConfig.doRef(invocation);
                return "success";
            }
        });
    }
}
