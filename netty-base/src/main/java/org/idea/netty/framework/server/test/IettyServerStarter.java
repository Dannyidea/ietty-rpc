package org.idea.netty.framework.server.test;

import org.idea.netty.framework.server.ServerApplication;
import org.idea.netty.framework.server.common.Service;
import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.config.ApplicationConfig;
import org.idea.netty.framework.server.config.ProtocolConfig;
import org.idea.netty.framework.server.config.RegisterConfig;
import org.idea.netty.framework.server.config.ServiceConfig;
import org.idea.netty.framework.server.register.RegisterFactory;
import org.idea.netty.framework.server.register.zookeeper.ZookeeperRegister;
import org.idea.netty.framework.server.register.zookeeper.ZookeeperRegisterFactory;
import org.idea.netty.framework.server.spi.filter.ProviderFilter;
import org.idea.netty.framework.server.spi.loader.ExtensionLoader;
import org.idea.netty.framework.server.util.AnnotationUtils;
import org.idea.netty.framework.server.util.PropertiesUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import static org.idea.netty.framework.server.common.ConfigPropertiesKey.*;

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
    public static void initServerAndStart(ApplicationConfig applicationConfig, ProtocolConfig protocolConfig, RegisterConfig registerConfig) throws InterruptedException {
        //加载服务类
        String packAgeName = ServerApplication.class.getPackage().getName();
        //初始化本地的一些配置
        initLocationConfig(applicationConfig, protocolConfig, registerConfig);

        //初始化过滤器的spi实现
        ExtensionLoader extensionLoader = new ExtensionLoader();
        try {
            extensionLoader.loadDirectory(ProviderFilter.class);
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
        //todo
        ZookeeperRegister zookeeperRegister = ZookeeperRegisterFactory.getZookeeperRegister();
        zookeeperRegister.startListenTask();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (zookeeperRegister == null) {
                    return;
                }
                Set<URL> urlSet = zookeeperRegister.getRegistryURLSet();
                Iterator<URL> iterator = urlSet.iterator();
                while (iterator.hasNext()) {
                    URL url = iterator.next();
                    zookeeperRegister.unRegister(url);
                }
                System.out.println("exit ietty");
            }
        }));

        //构建配置总线，并且写入到zookeeper
        ServerApplication.start();
    }

    private static void initLocationConfig(ApplicationConfig applicationConfig, ProtocolConfig protocolConfig, RegisterConfig registerConfig) {
        PropertiesUtils.putPropertiesValue(IETTY_APPLICATION_VERSION, applicationConfig.getVersion());
        PropertiesUtils.putPropertiesValue(IETTY_APPLICATION_NAME, applicationConfig.getName());

        PropertiesUtils.putPropertiesValue(IETTY_PROTOCOL_NAME,protocolConfig.getName());
        PropertiesUtils.putPropertiesValue(IETTY_PROTOCOL_HOST,protocolConfig.getHost());
        PropertiesUtils.putPropertiesValue(IETTY_PROTOCOL_PORT, String.valueOf(protocolConfig.getPort()));

        PropertiesUtils.putPropertiesValue(REGISTER_ADDRESS_KEY,registerConfig.getAddress());
        PropertiesUtils.putPropertiesValue(REGISTER_ADDRESS_PORT_KEY, String.valueOf(registerConfig.getPort()));
    }

    public static void main(String[] args) throws InterruptedException {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("ietty-v2-application");
        applicationConfig.setOwner("linhao");
        applicationConfig.setVersion("1.0.0");

        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setHost("127.0.0.1");
        protocolConfig.setName("ietty-2");
        protocolConfig.setPort(8090);

        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setAddress("127.0.0.1");
        registerConfig.setPort(2181);
        registerConfig.setUsername("username");
        registerConfig.setPassword("password");
        registerConfig.setProtocol("zookeeper");

        initServerAndStart(applicationConfig, protocolConfig, registerConfig);

    }
}
