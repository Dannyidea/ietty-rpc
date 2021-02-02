package org.idea.netty.framework.server.register.zookeeper;

import io.netty.util.internal.StringUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.common.event.EventObject;
import org.idea.netty.framework.server.common.event.EventTypeEnum;
import org.idea.netty.framework.server.common.event.ZookeeperRegistryEventHandler;
import org.idea.netty.framework.server.register.support.AbstractZookeeperClient;
import org.idea.netty.framework.server.register.support.CuratorZookeeperClient;
import org.idea.netty.framework.server.register.support.FailBackRegistry;
import org.idea.netty.framework.server.util.PropertiesUtils;

import java.util.HashMap;
import java.util.Map;

import static org.idea.netty.framework.server.common.ConfigPropertiesKey.*;
import static org.idea.netty.framework.server.common.URL.buildUrlStr;


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

    public void startListenTask(){
        if (zkClient == null || StringUtil.isNullOrEmpty(PropertiesUtils.getPropertiesStr(REGISTER_ADDRESS_KEY))) {
            System.err.println("节点数据或者客户端对象数据异常，请校验配置");
            return;
        }
        Thread nodeTask = new Thread(new ClientListenerTask((CuratorFramework) zkClient.getClient(), ROOT_PATH,this));
        nodeTask.start();
    }

    @Override
    public void doRegister(URL url) {
        System.out.println("ZookeeperRegister is begin to doRegister");
        if (!zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String providerPath = ROOT + "/" + url.getParameters().get("serviceName") + "/provider";
        String urlDataStr = buildUrlStr(url);
        String originNodeData = zkClient.getNodeData(providerPath);
        if (!StringUtil.isNullOrEmpty(originNodeData)) {
            System.out.println("原节点数据：" + originNodeData);
            zkClient.updateNodeData(providerPath, originNodeData + "##" + urlDataStr);
        } else {
            zkClient.createTemporaryData(providerPath, urlDataStr);
        }
        System.out.println("ietty register config is :" + urlDataStr);
    }

    @Override
    public void unRegister(URL url) {
        super.unRegister(url);
        String servicePath = ROOT + "/" + url.getParameters().get("serviceName");
        String providerPath = servicePath + "/provider";
        try {
            zkClient.deleteNode(providerPath);
        } catch (Exception e) {
        }
        try {
            zkClient.deleteNode(servicePath);
        } catch (Exception e) {

        }
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

        URL url11 = new URL("ietty", "idea", "root", "test-application", 9000, map, "test-path");

        Map<String, String> map2 = new HashMap<>();
        map2.put("serviceName", "com.sise.demo.UserService");
        map2.put("methods", "test&&get&&list&&insert&&update");
        map2.put("weight", "180");
        map2.put("host", "127.0.0.1");
        map2.put("port", "8999");
        URL url12 = new URL("ietty", "idea", "root", "test-application", 9000, map2, "test-path2");


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
    public void doSubscribe(URL url) {
        System.out.println("doSubscribe, url is "+url.toString());
    }

    @Override
    public void doUnSubscribe(URL url) {
        System.out.println("doUnSubscribe, url is "+url.toString());
    }


    /**
     * 后台监听节点数据的线程
     */
    private static class ClientListenerTask implements Runnable {

        private  ZookeeperRegister zookeeperRegister;

        private CuratorFramework curatorFramework;

        private String rootPath;

        public ClientListenerTask(CuratorFramework curatorFramework, String rootPath,ZookeeperRegister zookeeperRegister) {
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
                    URL url = URL.convertFromUrlStr(urlStr);
                    ZookeeperRegistryEventHandler zookeeperRegistryEventHandler = new ZookeeperRegistryEventHandler(zookeeperRegister);
                    if (TreeCacheEvent.Type.NODE_ADDED.equals(event.getType())) {
                        zookeeperRegister.doSubscribe(url);
                    } else if (TreeCacheEvent.Type.NODE_UPDATED.equals(event.getType())) {
                        zookeeperRegister.doSubscribe(url);
                    } else if (TreeCacheEvent.Type.NODE_REMOVED.equals(event.getType())) {
                        zookeeperRegister.doUnSubscribe(url);
                    }
                    System.err.println("event type2 ：" + event.getType() +
                            " | path ：" + (null != event.getData() ? event.getData().getPath() : null));
                };
                treeCache.getListenable().addListener(listener);
                treeCache.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
