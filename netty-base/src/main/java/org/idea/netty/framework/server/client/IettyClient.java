package org.idea.netty.framework.server.client;

import org.idea.netty.framework.server.ServerApplication;
import org.idea.netty.framework.server.common.Service;
import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.config.ServiceConfig;
import org.idea.netty.framework.server.register.Register;
import org.idea.netty.framework.server.register.RegisterFactory;
import org.idea.netty.framework.server.register.support.AbstractRegistry;
import org.idea.netty.framework.server.register.support.AbstractZookeeperClient;
import org.idea.netty.framework.server.register.support.CuratorZookeeperClient;
import org.idea.netty.framework.server.spi.loader.ExtensionLoader;
import org.idea.netty.framework.server.util.AnnotationUtils;
import org.idea.netty.framework.server.util.PropertiesUtils;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

import static org.idea.netty.framework.server.common.ConfigPropertiesKey.REGISTER_ADDRESS_KEY;
import static org.idea.netty.framework.server.common.ConfigPropertiesKey.REGISTER_ADDRESS_PORT_KEY;

/**
 * @Author linhao
 * @Date created in 5:56 下午 2021/2/1
 */
public class IettyClient {


    private AbstractZookeeperClient abstractZookeeperClient;

    private Set<ServiceConfig> serviceConfigSet;

    public IettyClient() {
        String registerAddress = PropertiesUtils.getPropertiesStr(REGISTER_ADDRESS_KEY);
        String port = PropertiesUtils.getPropertiesStr(REGISTER_ADDRESS_PORT_KEY);
        this.abstractZookeeperClient = new CuratorZookeeperClient(registerAddress, Integer.valueOf(port));
        String packAgeName = ServerApplication.class.getPackage().getName();
        this.serviceConfigSet = AnnotationUtils.getServiceConfigByAnnotation(Service.class, packAgeName);
    }


    public static void main(String[] args) {
        IettyClient iettyClient = new IettyClient();
        while (true) {
            try {
                System.out.println("input your cmd");
                System.out.println("===============|||||||||||||||||||============");
                System.out.println("1 查询节点url属性");
                System.out.println("2 更新节点权重");
                Scanner scanner = new Scanner(System.in);
                int input = scanner.nextInt();
                switch (input) {
                    case 1:
                        iettyClient.listIettyProviderNode();
                        break;
                    case 2:
                        iettyClient.updateNodeWeight();
                        break;
                }
                System.out.println("===============|||||||||||||||||||============");
            }catch (Exception e) {
                System.err.println("@@@@@@@@@@@@@@@ input error,exp is "+e);
            }

        }
    }

    public void listIettyProviderNode() {

        for (ServiceConfig serviceConfig : serviceConfigSet) {
            String urls = abstractZookeeperClient.getNodeData("/ietty/" + serviceConfig.getInterfaceClass().getName() + "/provider");
            System.out.println(urls);
        }
    }

    public void updateNodeWeight() {
        System.out.println("输入serviceName");
        Scanner serviceNameScanner = new Scanner(System.in);
        String serviceName = serviceNameScanner.nextLine();

        System.out.println("输入weight");
        Scanner weightScanner = new Scanner(System.in);
        int weight = weightScanner.nextInt();
        for (ServiceConfig serviceConfig : serviceConfigSet) {
            if (serviceConfig.getInterfaceName().equals(serviceName)) {
                String nodeAddress = "/ietty/" + serviceConfig.getInterfaceClass().getName() + "/provider";
                String originUrlData = abstractZookeeperClient.getNodeData(nodeAddress);
                URL url = URL.convertFromUrlStr(originUrlData);
                url.getParameters().put("weight", String.valueOf(weight));
                String newUrlStr = URL.buildUrlStr(url);
                abstractZookeeperClient.updateNodeData(nodeAddress, newUrlStr);
                System.out.println("更新节点权重成功");
                return;
            }
        }
    }
}
