package org.idea.netty.framework.server.config;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import lombok.Data;
import org.idea.netty.framework.server.ClientApplication;
import org.idea.netty.framework.server.proxy.JdkProxyFactory;
import org.idea.netty.framework.server.test.Test;

import java.io.UnsupportedEncodingException;

/**
 * @author linhao
 * @date created in 11:57 上午 2020/10/8
 */
public class ReferenceConfig<T> {

    private String protocol;

    /**
     * The interface name of the reference service
     */
    private String interfaceName;

    /**
     * The interface class of the reference service
     */
    private Class<T> interfaceClass;

    private String application;

    private ChannelFuture channelFuture;


    /**
     * 执行远程调用
     *
     * @param invocation
     * @return
     */
    public void doRef(Invocation invocation) {
        if (channelFuture == null) {
            try {
                channelFuture = ClientApplication.initClient();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        IettyProtocol iettyProtocol = buildIettyProtocol(invocation);
        channelFuture.channel().writeAndFlush(iettyProtocol);
    }

    public T get(){
        return JdkProxyFactory.getProxy(interfaceClass,this);
    }

    /**
     * 构建请求协议数据
     *
     * @param invocation
     * @return IettyProtocol对象
     */
    public IettyProtocol buildIettyProtocol(Invocation invocation) {
        IettyProtocol iettyProtocol = new IettyProtocol();
        iettyProtocol.setBody(invocation.toByteArray());
        iettyProtocol.setEvent((byte) 1);
        iettyProtocol.setReqOrResp((byte) 0);
        iettyProtocol.setSerializationType("jdk");
        //0正常请求状态
        iettyProtocol.setStatus((short) 0);
        iettyProtocol.setMAGIC((short) 0xdabb);
        return iettyProtocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

}
