package org.idea.netty.framework.server.test;

import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.config.ApplicationConfig;
import org.idea.netty.framework.server.config.ReferenceConfig;
import org.idea.netty.framework.server.register.support.AbstractZookeeperClient;
import org.idea.netty.framework.server.register.support.CuratorZookeeperClient;
import org.idea.netty.framework.server.test.service.GoodsService;
import org.idea.netty.framework.server.util.PropertiesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.idea.netty.framework.server.common.ConfigPropertiesKey.*;

/**
 * @author linhao
 * @date created in 3:39 下午 2020/10/8
 */
public class IettyClientStarter {

    public static Map<String, ReferenceConfig> referenceMaps = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("test-application");
        applicationConfig.setOwner("linhao");
        applicationConfig.setVersion("1.0.0");

        ReferenceConfig<GoodsService> refer = buildReference(GoodsService.class, "goodsService", "test-application");
        initClientConfig();
        GoodsService goodsService = refer.get();
        //获取zk的配置url
        while (true) {
            try {
                List<String> result = goodsService.findAll();
                System.out.println(result);
                Thread.sleep(2500);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private static <T> ReferenceConfig<T> buildReference(Class<T> clazz, String serviceName, String applicationName) {
        ReferenceConfig<T> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(applicationName);
        referenceConfig.setInterfaceClass(clazz);
        referenceConfig.setInterfaceName(clazz.getName());
        referenceConfig.setServiceName(serviceName);
        referenceConfig.setProtocol("ietty");
        referenceConfig.setAddress("127.0.0.1");
        IettyClientStarter.referenceMaps.put(clazz.getName(),referenceConfig);
        return referenceConfig;
    }


    public static void initClientConfig() {
        //初始化连接zk 获取配置信息
        //todo
        AbstractZookeeperClient abstractZookeeperClient = new CuratorZookeeperClient(PropertiesUtils.getPropertiesStr(REGISTER_ADDRESS_KEY),
                PropertiesUtils.getPropertiesInteger(REGISTER_ADDRESS_PORT_KEY));
        for (String key : IettyClientStarter.referenceMaps.keySet()) {
            ReferenceConfig config = referenceMaps.get(key);
            String className = config.getInterfaceName();
            String providerData = abstractZookeeperClient.getNodeData("/ietty/" + className + "/provider");
            String urlArr[] = providerData.split("##");
            List<URL> urls = new ArrayList<>();
            for (String url : urlArr) {
                URL currentUrl = URL.convertFromUrlStr(url);
                currentUrl.getParameters().get("host");
                currentUrl.getParameters().get("port");
                urls.add(currentUrl);
            }
            config.setUrls(urls);
            config.doRefRecord();
        }

//        ZookeeperRegister zookeeperRegister = ZookeeperRegisterFactory.getZookeeperRegister();
//        zookeeperRegister.startListenTask();
    }


}
