package org.idea.netty.framework.server.test;

import org.idea.netty.framework.server.ServerApplication;
import org.idea.netty.framework.server.config.ApplicationConfig;
import org.idea.netty.framework.server.config.ProtocolConfig;
import org.idea.netty.framework.server.config.ServiceConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linhao
 * @date created in 3:38 下午 2020/10/7
 */
public class MainServer {

    public static void main(String[] args) throws InterruptedException {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("test-application");
        applicationConfig.setOwner("linhao");
        applicationConfig.setVersion("1.0.0");

        List<ServiceConfig> serviceConfigs = new ArrayList<>();
        ServiceConfig serviceConfig = new ServiceConfig();
        serviceConfig.setInterfaceClass(Test.class);
        serviceConfig.setInterfaceImplClass(TestImpl.class);
        serviceConfig.setInterfaceName("testImpl");
        serviceConfig.setFilter("testFilter");
        serviceConfigs.add(serviceConfig);

        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setHost("127.0.0.1");
        protocolConfig.setName("dubbo-test");
        protocolConfig.setPort(8089);
        ServerApplication.setServerConfigList(serviceConfigs);
        ServerApplication.start();

    }
}
