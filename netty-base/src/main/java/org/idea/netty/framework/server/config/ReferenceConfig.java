package org.idea.netty.framework.server.config;

import io.netty.channel.ChannelFuture;
import org.idea.netty.framework.server.ClientApplication;
import org.idea.netty.framework.server.proxy.JdkProxyFactory;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;

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

    private String requestId;

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
        System.out.println("请求的魔数：" + iettyProtocol.getMAGIC());
        //这里面发送请求到服务端
        channelFuture.channel().writeAndFlush(iettyProtocol);
    }

    public T get() {
        this.requestId = UUID.randomUUID().toString();
        return JdkProxyFactory.getProxy(interfaceClass, this);
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
        iettyProtocol.setRequestId(this.getRequestId());
        //0正常请求状态
        iettyProtocol.setStatus((short) 0);
        //随机数字
        iettyProtocol.setMAGIC(new Random().nextInt(10000));
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

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
