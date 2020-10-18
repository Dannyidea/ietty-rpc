package org.idea.netty.framework.server.register.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.ACL;
import org.idea.netty.framework.server.common.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.idea.netty.framework.server.common.URL.buildUrlStr;


/**
 * @author linhao
 * @date created in 11:08 下午 2020/10/13
 */
public class ZookeeperRegister implements Register {

    private String ROOT = "/ietty";

    private static String zookeeperAddress = "localhost:2181";

    private ZkClient zkClient;

    private final static Integer TIMEOUT = 3000;

    public ZookeeperRegister() {
        zkClient = new ZkClient(zookeeperAddress, TIMEOUT);
    }

    @Override
    public boolean register(URL url) {
        if (!zkClient.exists(ROOT)) {
            zkClient.createPersistent(ROOT);
        }
        String serviceName = url.getParameters().get("serviceName");
        String firstChildPath = ROOT + "/" + serviceName;
        String secondChildPath = firstChildPath + "/provider";
        if (!zkClient.exists(secondChildPath)) {
            zkClient.createPersistent(secondChildPath, true);
        }
        String urlDataStr = buildUrlStr(url);
        System.out.println("【ietty register config】" + urlDataStr);
        zkClient.writeData(secondChildPath, urlDataStr.getBytes());
        return true;
    }

    public boolean consumer(URL url, String nodeData) {
        zkClient.writeData(url.getPath(), nodeData);
        return true;
    }

    public static void main(String[] args) throws InterruptedException {

        Map<String, String> map = new HashMap<>();
        map.put("serviceName", "com.sise.demo.FinanceService");
        map.put("methods", "test&&get&&list&&insert&&update");
        map.put("weight", "180");
        map.put("host", "127.0.0.1");
        map.put("port", "8999");

        URL url = new URL("ietty", "username", "password", 9000, map, "/test-path");
        ZookeeperRegister zookeeperRegister = new ZookeeperRegister();
        zookeeperRegister.register(url);
        while (true) {

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
}
