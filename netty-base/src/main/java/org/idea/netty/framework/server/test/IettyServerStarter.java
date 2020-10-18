package org.idea.netty.framework.server.test;

import org.idea.netty.framework.server.ServerApplication;
import org.idea.netty.framework.server.common.Service;
import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.config.ApplicationConfig;
import org.idea.netty.framework.server.config.ProtocolConfig;
import org.idea.netty.framework.server.config.ServiceConfig;
import org.idea.netty.framework.server.spi.filter.Filter;
import org.idea.netty.framework.server.spi.loader.ExtensionLoader;
import org.idea.netty.framework.server.util.AnnotationUtils;

import java.io.IOException;
import java.util.Set;

/**
 * @author linhao
 * @date created in 3:38 下午 2020/10/7
 */
public class IettyServerStarter {

    /**
     * 初始化资源并且启动服务
     *
     * @throws InterruptedException
     */
    public static void initServerAndStart() throws InterruptedException {
        //加载服务类
        String packAgeName = ServerApplication.class.getPackage().getName();
        Set<ServiceConfig> serviceConfigSet = AnnotationUtils.getServiceConfigByAnnotation(Service.class, packAgeName);
        ServerApplication.setServerConfigList(serviceConfigSet);
        //构建配置总线，并且写入到zookeeper

        //初始化过滤器的spi实现
        ExtensionLoader extensionLoader = new ExtensionLoader();
        try {
            extensionLoader.loadDirectory(Filter.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerApplication.start();
    }

    public static void main(String[] args) throws InterruptedException {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("test-application");
        applicationConfig.setOwner("linhao");
        applicationConfig.setVersion("1.0.0");

        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setHost("127.0.0.1");
        protocolConfig.setName("dubbo-test");
        protocolConfig.setPort(8089);

        initServerAndStart();

    }
}
