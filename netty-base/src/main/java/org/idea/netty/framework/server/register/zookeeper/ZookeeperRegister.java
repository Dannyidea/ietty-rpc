package org.idea.netty.framework.server.register.zookeeper;

import io.netty.util.internal.StringUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.common.event.ZookeeperRegistryEventHandler;
import org.idea.netty.framework.server.config.ReferenceConfig;
import org.idea.netty.framework.server.register.support.AbstractZookeeperClient;
import org.idea.netty.framework.server.register.support.CuratorZookeeperClient;
import org.idea.netty.framework.server.register.support.FailBackRegistry;
import org.idea.netty.framework.server.test.IettyClientStarter;
import org.idea.netty.framework.server.util.PropertiesUtils;

import java.util.*;

import static org.idea.netty.framework.server.common.ConfigPropertiesKey.*;
import static org.idea.netty.framework.server.common.URL.buildUrlStr;
import static org.idea.netty.framework.server.spi.loadbalance.WeightLoadBalance.lastIndexVisitMap;
import static org.idea.netty.framework.server.spi.loadbalance.WeightLoadBalance.randomWeightMap;
import static org.idea.netty.framework.server.test.IettyClientStarter.referenceMaps;


/**
 * @author linhao
 * @date created in 11:08 下午 2020/10/13
 */
public class ZookeeperRegister extends FailBackRegistry {

    private String ROOT = "/ietty";

    private AbstractZookeeperClient zkClient;

    private final static Integer TIMEOUT = 3000;


    public ZookeeperRegister(URL url) {
        super(url);
        zkClient = new CuratorZookeeperClient(PropertiesUtils.getPropertiesStr(REGISTER_ADDRESS_KEY), PropertiesUtils.getPropertiesInteger(REGISTER_ADDRESS_PORT_KEY));
    }

    public void startListenTask() {
        if (zkClient == null || StringUtil.isNullOrEmpty(PropertiesUtils.getPropertiesStr(REGISTER_ADDRESS_KEY))) {
            System.err.println("节点数据或者客户端对象数据异常，请校验配置");
            return;
        }
        Thread nodeTask = new Thread(new ClientListenerTask((CuratorFramework) zkClient.getClient(), ROOT_PATH, this));
        nodeTask.start();
    }

    private String getProviderPath(URL url) {
        return ROOT + "/" + url.getParameters().get("serviceName") + "/provider";
    }

    private String getConsumerPath(String providerServiceName) {
        return ROOT + "/" + providerServiceName + "/consumer";
    }

    @Override
    public void doRegister(URL url) {
        System.out.println("ZookeeperRegister is begin to doRegister");
        if (!zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String urlDataStr = buildUrlStr(url);
        String originNodeData = zkClient.getNodeData(getProviderPath(url));
        if (!StringUtil.isNullOrEmpty(originNodeData)) {
            if (originNodeData.endsWith("##")) {
                urlDataStr = originNodeData + urlDataStr;
            } else {
                urlDataStr = originNodeData + "##" + urlDataStr;
            }
            zkClient.updateNodeData(getProviderPath(url), urlDataStr);
        } else {
            zkClient.createTemporaryData(getProviderPath(url), urlDataStr);
        }
        System.out.println("【doRegister】ietty register provider config is :" + urlDataStr);
    }

    @Override
    public void unRegister(URL url) {
        super.unRegister(url);
        String servicePath = ROOT + "/" + url.getParameters().get("serviceName");
        String providerPath = servicePath + "/provider";
        try {
            String originUrlData = zkClient.getNodeData(providerPath);
            String urlStr = URL.buildUrlStr(url);
            originUrlData = originUrlData.replace(urlStr, "");
            if(StringUtil.isNullOrEmpty(originUrlData)){
                zkClient.deleteNode(providerPath);
            } else {
                zkClient.updateNodeData(providerPath, originUrlData);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void doSubscribe(String consumerUrl, String providerServiceName) {
        System.out.println("ZookeeperRegister is begin to doSubscribe");
        if (!zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String originNodeData = zkClient.getNodeData(getConsumerPath(providerServiceName));
        if (!StringUtil.isNullOrEmpty(originNodeData)) {
            zkClient.updateNodeData(getConsumerPath(providerServiceName), originNodeData + "##" + consumerUrl);
        } else {
            zkClient.createTemporaryData(getConsumerPath(providerServiceName), consumerUrl);
        }
        System.out.println("ietty register consumer config is :" + consumerUrl);
    }

    public boolean consumer(URL url, String nodeData) {
        zkClient.createTemporaryData(url.getPath(), nodeData);
        return true;
    }

    public static void main(String[] args) throws InterruptedException {

        Map<String, String> map = new HashMap<>();
        map.put("serviceName", "com.sise.demo.FinanceService");
        map.put("methods", "test&&get&&list&&insert&&update");
        map.put("weight", "180");
        map.put("host", "127.0.0.1");
        map.put("port", "8999");

        URL url11 = new URL("ietty", "idea", "root", "test-application", map, "test-path");

        Map<String, String> map2 = new HashMap<>();
        map2.put("serviceName", "com.sise.demo.UserService");
        map2.put("methods", "test&&get&&list&&insert&&update");
        map2.put("weight", "180");
        map2.put("host", "127.0.0.1");
        map2.put("port", "8999");
        URL url12 = new URL("ietty", "idea", "root", "test-application", map2, "test-path2");


        ZookeeperRegister testRegister = new ZookeeperRegister(url11);
        testRegister.register(url11);
        testRegister.register(url11);

        while (true) {
            Thread.sleep(5000);
            System.out.println("===");
        }
//        ZkClient zkClient = new ZkClient(zookeeperAddress, TIMEOUT);
//        zkClient.createEphemeral("/ietty/com.sise.test");
//        while (true) {
//
//        }
//        boolean exists = zkClient.exists(url.getPath());
//        if (!exists) {
//            System.out.println("节点数据不存在");
//            zkClient.create(url.getPath(), "test".getBytes(), CreateMode.PERSISTENT);
////            zkClient.create(url.getPath(),"test".getBytes(),null, CreateMode.PERSISTENT);
//        }
//        System.out.println(zkClient.exists(url.getPath()));
//        //节点数据需要一层一层地写
//        zkClient.create(url.getPath() + "/node-1", "test-1".getBytes(), CreateMode.EPHEMERAL);
//        zkClient.create(url.getPath() + "/node-1/node-2", "test-2".getBytes(), CreateMode.EPHEMERAL);
//        //zk 连接异常中断后写入的临时节点会消失
//        while (true) {
//
//        }
//        zkClient.subscribeChildChanges(url.getPath(), new IZkChildListener() {
//            @Override
//            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
//                System.out.println(parentPath + " 's child changed, currentChilds:" + currentChilds);
//            }
//        });
//        zkClient.createPersistent(url.getPath());
//        Thread.sleep(1000);
//        System.out.println(zkClient.getChildren(url.getPath()));
//        Thread.sleep(1000);
//        //设置子节点
//        zkClient.createPersistent(url.getPath() + "/c1");
//        Thread.sleep(1000);
//
//        //删除子节点
//        zkClient.delete(url.getPath() + "/c1");
//        Thread.sleep(1000);
//        zkClient.delete(url.getPath());
//        Thread.sleep(Integer.MAX_VALUE);

    }

    @Override
    public URL getUrl() {
        return null;
    }

    @Override
    public void doSubscribeAfterUpdate(URL url) {
        System.out.println("doSubscribeAfterUpdate, url is " + url.toString());
        String key = url.getInterfacePath();
        url.getParameters().put("serviceName", key);
        ReferenceConfig referenceConfig = referenceMaps.get(key);
        String currentUrlStr = zkClient.getNodeData(getProviderPath(url));
        //需要考虑调用方可能并没有相关的引用
        if (referenceConfig == null) {
            System.out.println("【doSubscribeAfterUpdate】消费方没有调用" + key + "服务，不需要处理");
            return;
        }
        if (StringUtil.isNullOrEmpty(currentUrlStr)) {
            referenceConfig.setUrls(null);
            return;
        } else {
            List<String> urlList = Arrays.asList(currentUrlStr.split("##"));
            List<URL> newUrlList = new ArrayList<>();
            URL currentMatchUrlItem = null;
            for (String urlItem : urlList) {
                URL currentUrl = URL.convertFromUrlStr(urlItem);
                //更新的情况
                if (currentUrl.compareUrlIsSame(url)) {
                    currentMatchUrlItem = url;
                    newUrlList.add(currentMatchUrlItem);
                } else {
                    newUrlList.add(currentUrl);
                }
            }
            //新增
            if (currentMatchUrlItem == null) {
                System.out.println("新增节点：" + url);
            } else {
                System.out.println("更新节点：" + url);
            }
            randomWeightMap.remove(url.getPath());
            lastIndexVisitMap.remove(url.getPath());
            referenceConfig.setUrls(newUrlList);
        }
        referenceMaps.put(key, referenceConfig);
    }

    @Override
    public void doSubscribeAfterAdd(URL url) {
        System.out.println("doSubscribeAfterAdd, url is " + url.toString());
        String interfacePath = (String) url.getParameter("interfacePath", "");
        String key = interfacePath.substring(ROOT_PATH.length() + 1).replace("/provider", "");
        ReferenceConfig referenceConfig = referenceMaps.get(key);
        if (referenceConfig == null) {
            return;
        }
        List<URL> urlList = referenceConfig.getUrls();
        for (URL urlItem : urlList) {
            if (!urlItem.compareUrlIsSame(url)) {
                urlList.add(url);
                referenceConfig.setUrls(urlList);
                break;
            }
        }
        referenceMaps.put(key, referenceConfig);
    }

    @Override
    public void doUnSubscribe(URL url) {
        System.out.println("doUnSubscribe, url is " + url.toString());
    }


    /**
     * 后台监听节点数据的线程
     */
    private static class ClientListenerTask implements Runnable {

        private ZookeeperRegister zookeeperRegister;

        private CuratorFramework curatorFramework;

        private String rootPath;

        public ClientListenerTask(CuratorFramework curatorFramework, String rootPath, ZookeeperRegister zookeeperRegister) {
            this.curatorFramework = curatorFramework;
            this.rootPath = rootPath;
            this.zookeeperRegister = zookeeperRegister;
        }

        @Override
        public void run() {
            try {
                System.out.println("监听节点数据");
                TreeCache treeCache = new TreeCache(curatorFramework, rootPath);
                TreeCacheListener listener = (client1, event) -> {
                    byte[] data = event.getData().getData();
                    String urlStr = new String(data);
                    String[] urlStrArr = urlStr.split("##");
                    for (String urlItem : urlStrArr) {
                        String interfacePath = event.getData().getPath();
                        URL url = URL.convertFromUrlStr(urlItem);
                        url.getParameters().put("interfacePath", interfacePath);
                        if (TreeCacheEvent.Type.NODE_ADDED.equals(event.getType())) {
                            zookeeperRegister.doSubscribeAfterAdd(url);
                        } else if (TreeCacheEvent.Type.NODE_UPDATED.equals(event.getType())) {
                            zookeeperRegister.doSubscribeAfterUpdate(url);
                        } else if (TreeCacheEvent.Type.NODE_REMOVED.equals(event.getType())) {
                            zookeeperRegister.doUnSubscribe(url);
                        }
                        System.err.println("event type2 ：" + event.getType() +
                                " | path ：" + (null != event.getData() ? event.getData().getPath() : null));
                    }
                };
                treeCache.getListenable().addListener(listener);
                treeCache.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
