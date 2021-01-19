package org.idea.netty.framework.server.test;

import org.idea.netty.framework.server.config.ApplicationConfig;
import org.idea.netty.framework.server.config.ReferenceConfig;
import org.idea.netty.framework.server.test.service.GoodsService;
import org.idea.netty.framework.server.test.service.Test;
import org.idea.netty.framework.server.test.service.UserService;

/**
 * @author linhao
 * @date created in 3:39 下午 2020/10/8
 */
public class IettyClientStarter {

    public static void main(String[] args) throws InterruptedException {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("test-application");
        applicationConfig.setOwner("linhao");
        applicationConfig.setVersion("1.0.0");

        ReferenceConfig<Test> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(applicationConfig.getName());
        referenceConfig.setInterfaceClass(Test.class);
        referenceConfig.setInterfaceName("testImpl");

        Test testImpl = (Test) buildReference(Test.class,"testImpl","test-application");
        UserService userService = (UserService) buildReference(UserService.class,"userService","test-application");
        GoodsService goodsService = (GoodsService) buildReference(GoodsService.class,"goodsService","test-application");
        userService.findAll();

        while (true) {
//            testImpl.doTest("test");
//            testImpl.testStr("idea");
//            userService.addUser();
//            goodsService.addGoods();
//            goodsService.findAll();
            Thread.sleep(5000);
        }
    }

    private static <T> Object buildReference(Class<T> clazz,String interfaceName,String applicationName){
        ReferenceConfig<T> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(applicationName);
        referenceConfig.setInterfaceClass(clazz);
        referenceConfig.setInterfaceName(interfaceName);
        return referenceConfig.get();
    }
}
