package org.idea.netty.framework.server.config;

import lombok.ToString;
import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.register.zookeeper.ZookeeperRegister;
import org.idea.netty.framework.server.register.zookeeper.ZookeeperRegisterFactory;


import java.util.HashMap;
import java.util.Map;

import static org.idea.netty.framework.server.ServerApplication.isServerStart;

/**
 * @author linhao
 * @date created in 3:34 下午 2020/10/7
 */
@ToString
public class ServiceConfig {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * The interface name of the exported service
     */
    private String interfaceName;


    private String[] methodNames;

    /**
     * The interface class of the exported service
     */
    private Class<?> interfaceClass;

    /**
     * 具体实现类对象
     */
    private Class<?> interfaceImplClass;

    /**
     * 过滤器
     */
    private String filter;

    /**
     * 服务配置
     */
    private ApplicationConfig applicationConfig;

    /**
     * 协议配置
     */
    private ProtocolConfig protocolConfig;


    /**
     * 暴露服务
     */
    public void export(URL url) {
        //判断服务是否启动
        if (!isServerStart()) {
            throw new RuntimeException("sever is close");
        }
        System.out.println("服务暴露");
        //在这里进行服务写入到zookeeper并且构建配置总线
        Map<String,String> parameterMap = new HashMap<>();
        parameterMap.put("serviceName",this.getServiceName());
        String[] methodNamesArr = this.getMethodNames();
        parameterMap.put("methods",methodNamesArr.toString());
        parameterMap.put("port", String.valueOf(this.getProtocolConfig().getPort()));
        parameterMap.put("host",this.getProtocolConfig().getHost());
        //默认初始化权重值
        parameterMap.put("weight","100");
        url.setParameters(parameterMap);
        url.setPath(this.getServiceName());
        //持久化写入到zookeeper
        ZookeeperRegisterFactory.buildZookeeperRegister().register(url);
    }


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    public void setApplicationConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    public ProtocolConfig getProtocolConfig() {
        return protocolConfig;
    }

    public void setProtocolConfig(ProtocolConfig protocolConfig) {
        this.protocolConfig = protocolConfig;
    }

    public Class<?> getInterfaceImplClass() {
        return interfaceImplClass;
    }

    public void setInterfaceImplClass(Class<?> interfaceImplClass) {
        this.interfaceImplClass = interfaceImplClass;
    }

    public String[] getMethodNames() {
        return methodNames;
    }

    public void setMethodNames(String[] methodNames) {
        this.methodNames = methodNames;
    }
}
