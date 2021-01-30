package org.idea.netty.framework.server.test;

import org.idea.netty.framework.server.common.ConfigPropertiesKey;
import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.config.ApplicationConfig;
import org.idea.netty.framework.server.config.ProtocolConfig;
import org.idea.netty.framework.server.config.ReferenceConfig;
import org.idea.netty.framework.server.config.RegisterConfig;
import org.idea.netty.framework.server.register.Register;
import org.idea.netty.framework.server.register.RegisterFactory;
import org.idea.netty.framework.server.register.support.AbstractRegistry;
import org.idea.netty.framework.server.register.support.AbstractZookeeperClient;
import org.idea.netty.framework.server.register.support.CuratorZookeeperClient;
import org.idea.netty.framework.server.register.zookeeper.ZookeeperRegister;
import org.idea.netty.framework.server.register.zookeeper.ZookeeperRegisterFactory;
import org.idea.netty.framework.server.spi.filter.ConsumerFilter;
import org.idea.netty.framework.server.spi.filter.ProviderFilter;
import org.idea.netty.framework.server.spi.loader.ExtensionLoader;
import org.idea.netty.framework.server.test.service.GoodsService;
import org.idea.netty.framework.server.test.service.Test;
import org.idea.netty.framework.server.test.service.UserService;
import org.idea.netty.framework.server.util.PropertiesUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.idea.netty.framework.server.common.ConfigPropertiesKey.*;

/**
 * @author linhao
 * @date created in 3:39 下午 2020/10/8
 */
public class IettyClientStarter {

    private static List<ReferenceConfig> referenceConfigs = new LinkedList<>();

    private static Map<String,ReferenceConfig> referenceMaps = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("test-application");
        applicationConfig.setOwner("linhao");
        applicationConfig.setVersion("1.0.0");

        ReferenceConfig<Test> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(applicationConfig.getName());
        referenceConfig.setInterfaceClass(Test.class);
        referenceConfig.setInterfaceName("testImpl");

        ReferenceConfig<GoodsService> refer =  buildReference(GoodsService.class, "goodsService", "test-application");
        initClientConfig();
        GoodsService goodsService = refer.get();
        //获取zk的配置url
        while (true) {
            goodsService.findAll();
            Thread.sleep(5000);
        }
    }

    private static <T> ReferenceConfig<T> buildReference(Class<T> clazz, String serviceName, String applicationName) {
        ReferenceConfig<T> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(applicationName);
        referenceConfig.setInterfaceClass(clazz);
        referenceConfig.setInterfaceName(clazz.getName());
        referenceConfig.setServiceName(serviceName);
        IettyClientStarter.referenceConfigs.add(referenceConfig);
        return referenceConfig;
    }


    public static void initClientConfig() {
        //初始化连接zk 获取配置信息
        //todo
        AbstractZookeeperClient abstractZookeeperClient = new CuratorZookeeperClient(PropertiesUtils.getPropertiesStr(REGISTER_ADDRESS_KEY),
                PropertiesUtils.getPropertiesInteger(REGISTER_ADDRESS_PORT_KEY));
        for (ReferenceConfig config : IettyClientStarter.referenceConfigs) {
            String className = config.getInterfaceName();
            String providerData = abstractZookeeperClient.getNodeData("/ietty/" + className + "/provider");
            URL url = URL.convertFromUrlStr(providerData);
            url.getParameters().get("host");
            url.getParameters().get("port");
            List<URL> urls = new ArrayList<>();
            urls.add(url);
            config.setUrls(urls);
            referenceMaps.put(className,config);
        }
    }


}
