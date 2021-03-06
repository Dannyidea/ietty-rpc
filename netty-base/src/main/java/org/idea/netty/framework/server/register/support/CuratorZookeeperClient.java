package org.idea.netty.framework.server.register.support;


import com.sun.security.ntlm.Client;
import io.netty.util.internal.StringUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.idea.netty.framework.server.common.event.EventObject;
import org.idea.netty.framework.server.common.event.EventTypeEnum;
import org.idea.netty.framework.server.common.event.ZookeeperRegistryEventHandler;
import org.idea.netty.framework.server.util.PropertiesUtils;

import java.util.Collections;
import java.util.List;

import static org.idea.netty.framework.server.common.ConfigPropertiesKey.REGISTER_ADDRESS_KEY;
import static org.idea.netty.framework.server.common.ConfigPropertiesKey.ROOT_PATH;

/**
 * 基于CuratorZookeeper操作zk的节点
 *
 * @Author linhao
 * @Date created in 2:36 下午 2021/1/7
 */
public class CuratorZookeeperClient extends AbstractZookeeperClient {

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
    public CuratorFramework getClient(){
        return client;
    }

    @Override
    public void updateNodeData(String address, String data) {
        try {
            client.setData().forPath(address, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNodeData(String address) {
        try {
            byte[] result = client.getData().forPath(address);
            if (result != null) {
                return new String(result);
            }
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void createPersistentData(String address, String data) {
        try {
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(address, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createPersistentWithSeqData(String address, String data) {
        try {
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(address, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTemporarySeqData(String address, String data) {
        try {
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(address, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTemporaryData(String address, String data) {
        try {
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(address, data.getBytes());
        } catch (KeeperException.NoChildrenForEphemeralsException e) {
            try {
                client.setData().forPath(address, data.getBytes());
            } catch (Exception ex) {
                throw new IllegalStateException(ex.getMessage(), ex);
            }
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }
    }

    @Override
    public void setTemporaryData(String address, String data) {
        try {
            client.setData().forPath(address, data.getBytes());
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
        AbstractZookeeperClient abstractZookeeperClient = new CuratorZookeeperClient("127.0.0.1", 2181);
        abstractZookeeperClient.createTemporaryData("/test/provider", "");
        abstractZookeeperClient.createTemporaryData("/test/provider/children", "url2");
        List<String> nodes = abstractZookeeperClient.listNode("/test");
        for (String node : nodes) {
            List<String> childrenNode = abstractZookeeperClient.listNode("/test/" + node + "/children");
            for (String childNode : childrenNode) {
                System.out.println(childNode);
            }
        }
    }

}
