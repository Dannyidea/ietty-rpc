package org.idea.netty.framework.server.channel;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.idea.netty.framework.server.ServerApplication;
import org.idea.netty.framework.server.bean.User;
import org.idea.netty.framework.server.config.IettyProtocol;
import org.idea.netty.framework.server.config.Invocation;
import org.idea.netty.framework.server.config.ServiceConfig;
import org.idea.netty.framework.server.spi.filter.Filter;
import org.idea.netty.framework.server.spi.filter.IettyServerFilter;
import org.idea.netty.framework.server.spi.loader.ExtensionLoader;
import org.idea.netty.framework.server.util.StringUtils;
import sun.jvm.hotspot.runtime.Bytes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import static org.idea.netty.framework.server.spi.loader.ExtensionLoader.initClassInstance;
import static org.idea.netty.framework.server.util.CollectionUtils.isMapNotEmpty;


/**
 * 共享通道，意味着每次请求进来都会使用同一个handler
 *
 * @author linhao
 * @date created in 5:13 下午 2020/10/1
 */
@ChannelHandler.Sharable
public class BaseInitServerChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        IettyProtocol data = (IettyProtocol) msg;
        Invocation invocation = toInvocationFromByte(data.getBody());
        AllChannelHandler.channelRead(new Runnable() {
            @Override
            public void run() {
                try {
                    //注意如果是个接口则不能进行实现
                    ServiceConfig serviceConfig = ServerApplication.getServiceConfig(invocation.getServiceName());
                    if (serviceConfig == null) {
                        throw new RuntimeException("serviceConfig can not be null!");
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
                    if(isMapNotEmpty(classMap)){
                        String currentFilterName = serviceConfig.getFilter();
                        if(StringUtils.isNotEmpty(currentFilterName)){
                            Filter filter = (Filter) initClassInstance(currentFilterName);
                            filter.doFilter(invocation);
                        }
                    }
                    if(method.getReturnType().getName().equals(Void.TYPE.getName())){
                        method.invoke(invocationClass,args);
                    } else {
                        Object returnVal = method.invoke(invocationClass,args);
                        byte[] bytes = returnVal.toString().getBytes();
                        //响应结果数据
                        data.setBody(bytes);
                    }
                    //响应的内容
                    ctx.writeAndFlush(data);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) {
        System.out.println("通道刷新");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("出现了异常问题"+cause);
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }

    private Invocation toInvocationFromByte(byte[] bytes) {
        String json = new String(bytes);
        return JSON.parseObject(json, Invocation.class);
    }
}
