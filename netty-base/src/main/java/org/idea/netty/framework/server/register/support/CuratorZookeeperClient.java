package org.idea.netty.framework.server.register.support;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;

/**
 * 基于CuratorZookeeper操作zk的节点
 *
 * @Author linhao
 * @Date created in 2:36 下午 2021/1/7
 */
public class CuratorZookeeperClient extends AbstractZookeeperClient {

    private CuratorFramework curatorFramework;
    private String address;
    private CuratorFramework client;

    public CuratorZookeeperClient(String zkAddress, Integer port) {
        this(zkAddress, port, null, null);
    }

    public CuratorZookeeperClient(String zkAddress, Integer port, Integer baseSleepTimes, Integer maxRetryTimes) {
        super(zkAddress, port, baseSleepTimes, maxRetryTimes);
        this.address = zkAddress + ":" + port;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(super.getBaseSleepTimes(), super.getMaxRetryTimes());
        if (client == null) {
            client = CuratorFrameworkFactory.newClient(this.address, retryPolicy);
            client.start();
        }
    }


    @Override
    public void createPersistentData(String address, String data) {
        try {
            client.create().withMode(CreateMode.PERSISTENT).forPath(address, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createPersistentWithSeqData(String address, String data) {
        try {
            client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(address, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTemporarySeqData(String address, String data) {
        try {
            client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(address, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTemporaryData(String address, String data) {
        try {
            client.create().withMode(CreateMode.EPHEMERAL).forPath(address, data.getBytes());
        } catch (KeeperException.NoChildrenForEphemeralsException e) {
            try {
                client.setData().forPath(address,data.getBytes());
            } catch (Exception ex) {
                throw new IllegalStateException(ex.getMessage(), ex);
            }
        } catch (Exception ex){
            throw new IllegalStateException(ex.getMessage(), ex);
        }
    }

    @Override
    public void setTemporaryData(String address, String data) {
        try {
            client.setData().forPath(address,data.getBytes());
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }
    }

    @Override
    public void destroy() {
        client.close();
    }

    @Override
    public List<String> listNode(String address) {
        try {
            return client.getChildren().forPath(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public boolean deleteNode(String address) {
        try {
            client.delete().forPath(address);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean existNode(String address) {
        try {
            Stat stat = client.checkExists().forPath(address);
            return stat != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        AbstractZookeeperClient abstractZookeeperClient = new CuratorZookeeperClient("127.0.0.1",2181);
//        abstractZookeeperClient.createPersistentData("/test","");
        abstractZookeeperClient.createTemporaryData("/test/provider","");
        abstractZookeeperClient.createTemporaryData("/test/provider/children","url2");
//        abstractZookeeperClient.createPersistentData("/test","");
//        abstractZookeeperClient.createPersistentData("/test/node-persistent","test-node-value");
//        abstractZookeeperClient.createPersistentWithSeqData("/test/node-persistent-seq","test-node-value");
//        abstractZookeeperClient.createTemporaryData("/test/node-temporary","test-node-value");
//        abstractZookeeperClient.createTemporarySeqData("/test/node-temporary-seq","test-node-value");
//        boolean status = abstractZookeeperClient.existNode("/test/node-persistent");
//        System.out.println(status);
        List<String> nodes = abstractZookeeperClient.listNode("/test");
        for (String node : nodes) {
            List<String> childrenNode = abstractZookeeperClient.listNode("/test/"+node+"/children");
            for (String childNode : childrenNode) {
                System.out.println(childNode);
            }
//            System.out.println(node);
        }
//        Thread.sleep(900000);
//        abstractZookeeperClient.deleteNode("/test/node-persistent-seq0000000001");
//        List<String> nodes2 = abstractZookeeperClient.listNode("/test");
//        for (String node : nodes2) {
//            System.out.println(node);
//        }
    }

}
