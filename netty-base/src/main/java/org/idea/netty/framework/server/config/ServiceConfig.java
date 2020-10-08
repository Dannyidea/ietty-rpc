package org.idea.netty.framework.server.config;

import lombok.Data;

import java.util.List;

import static org.idea.netty.framework.server.ServerApplication.isServerStart;

/**
 * @author linhao
 * @date created in 3:34 下午 2020/10/7
 */
public class ServiceConfig {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * The interface name of the exported service
     */
    private String interfaceName;

    /**
     * The interface class of the exported service
     */
    private Class<?> interfaceClass;

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
    public void export() {
        //判断服务是否启动
        if (!isServerStart()) {
            throw new RuntimeException("sever is close");
        }
        System.out.println("服务暴露");
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
}
