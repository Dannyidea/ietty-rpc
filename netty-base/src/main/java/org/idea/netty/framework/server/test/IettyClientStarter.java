package org.idea.netty.framework.server.test;

import org.idea.netty.framework.server.ClientApplication;
import org.idea.netty.framework.server.config.ApplicationConfig;
import org.idea.netty.framework.server.config.ReferenceConfig;
import org.idea.netty.framework.server.proxy.JdkProxyFactory;

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
        Test t = referenceConfig.get();
        while (true) {
            Thread.sleep(1000);
            t.testStr("idea");
            System.out.println("发送消息");
        }
    }
}
