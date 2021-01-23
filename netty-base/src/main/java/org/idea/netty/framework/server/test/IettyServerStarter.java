package org.idea.netty.framework.server.test;

import org.idea.netty.framework.server.ServerApplication;
import org.idea.netty.framework.server.common.Service;
import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.config.ApplicationConfig;
import org.idea.netty.framework.server.config.ProtocolConfig;
import org.idea.netty.framework.server.config.RegisterConfig;
import org.idea.netty.framework.server.config.ServiceConfig;
import org.idea.netty.framework.server.register.Register;
import org.idea.netty.framework.server.register.RegisterFactory;
import org.idea.netty.framework.server.register.zookeeper.ZookeeperRegister;
import org.idea.netty.framework.server.register.zookeeper.ZookeeperRegisterFactory;
import org.idea.netty.framework.server.spi.filter.Filter;
import org.idea.netty.framework.server.spi.loader.ExtensionLoader;
import org.idea.netty.framework.server.util.AnnotationUtils;

import java.io.IOException;
import java.util.Iterator;
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
    public static void initServerAndStart(ApplicationConfig applicationConfig,ProtocolConfig protocolConfig,RegisterConfig registerConfig) throws InterruptedException {
        //加载服务类
        String packAgeName = ServerApplication.class.getPackage().getName();
        //初始化过滤器的spi实现
        ExtensionLoader extensionLoader = new ExtensionLoader();
        try {
            extensionLoader.loadDirectory(Filter.class);
            extensionLoader.loadDirectory(RegisterFactory.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<ServiceConfig> serviceConfigSet = AnnotationUtils.getServiceConfigByAnnotation(Service.class, packAgeName);
        for (ServiceConfig serviceConfig : serviceConfigSet) {
            serviceConfig.setApplicationConfig(applicationConfig);
            serviceConfig.setProtocolConfig(protocolConfig);
            serviceConfig.setRegisterConfig(registerConfig);
        }
        ServerApplication.setServerConfigList(serviceConfigSet);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                ZookeeperRegister zookeeperRegister = ZookeeperRegisterFactory.getZookeeperRegister();
                if(zookeeperRegister==null){
                    return;
                }
                Set<URL> urlSet = zookeeperRegister.getRegistryURLSet();
                Iterator<URL> iterator = urlSet.iterator();
                while (iterator.hasNext()){
                    URL url = iterator.next();
                    zookeeperRegister.unRegister(url);
                }
                System.out.println("exit ietty");
            }
        }));

        //构建配置总线，并且写入到zookeeper
        ServerApplication.start();
    }

    public static void main(String[] args) throws InterruptedException {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("ietty-v2-application");
        applicationConfig.setOwner("linhao");
        applicationConfig.setVersion("1.0.0");

        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setHost("127.0.0.1");
        protocolConfig.setName("ietty");
        protocolConfig.setPort(8089);

        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setAddress("127.0.0.1");
        registerConfig.setPort(2181);
        registerConfig.setUsername("username");
        registerConfig.setPassword("password");
        registerConfig.setProtocol("zookeeper");

        initServerAndStart(applicationConfig,protocolConfig,registerConfig);

    }
}
