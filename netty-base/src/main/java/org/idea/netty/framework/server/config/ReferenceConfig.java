package org.idea.netty.framework.server.config;

import io.netty.channel.ChannelFuture;
import org.idea.netty.framework.server.ClientApplication;
import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.proxy.JdkProxyFactory;
import org.idea.netty.framework.server.register.Register;
import org.idea.netty.framework.server.register.RegisterFactory;
import org.idea.netty.framework.server.register.zookeeper.ZookeeperRegisterFactory;
import org.idea.netty.framework.server.rpc.filter.IettyConsumerFilterChain;

import java.util.*;

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

    private String requestId;

    private String serviceName;

    private List<URL> urls;

    /**
     * 消费方的ip地址
     */
    private String address;

    /**
     * 执行远程调用
     *
     * @param invocation
     * @return
     */
    public void doRef(Invocation invocation) {
        //这里面需要先执行一系列的过滤链路filter 每个filter都会返回一个invoker对象
        //过滤逻辑中会吧invocation中的urls不断筛选，最终只需要一个url配置
        IettyConsumerFilterChain.doFilter(invocation);
        String port = invocation.getReferUrl().getParameters().get("port");
        System.out.println(port);
        ChannelFuture channelFuture = null;
        try {
            channelFuture = ClientApplication.initClient(invocation.getReferUrl());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        IettyProtocol iettyProtocol = buildIettyProtocol(invocation);
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

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public List<URL> getUrls() {
        return urls;
    }

    public void setUrls(List<URL> urls) {
        this.urls = urls;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 将自己订阅的信息写入到zk节点下边的consumer模块
     */
    public void doRefRecord(){
        List<URL> urls = this.getUrls();
        RegisterFactory registerFactory = new ZookeeperRegisterFactory();
        String consumerUrlStr = URL.buildConsumerUrlStr(this);
        for (URL url : urls) {
            url.getParameters().put("saveUrlInDisk","false");
            Register register = registerFactory.createRegister(url);
            register.subscribe(consumerUrlStr,interfaceClass.getName());
        }
        registerFactory.startListenTask();
    }
}
