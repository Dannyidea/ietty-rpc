package org.idea.netty.framework.server.register.support;

import java.util.List;

/**
 * 抽象的一个zk客户端
 *
 * @Author linhao
 * @Date created in 2:37 下午 2021/1/7
 */
public abstract class AbstractZookeeperClient {

    private String zkAddress;
    private Integer port;
    private int baseSleepTimes;
    private int maxRetryTimes;

    public AbstractZookeeperClient(String zkAddress, Integer port) {
        this.zkAddress = zkAddress;
        this.port = port;
        //默认3000ms
        this.baseSleepTimes = 1000;
        this.maxRetryTimes = 3;
    }

    public AbstractZookeeperClient(String zkAddress, Integer port, Integer baseSleepTimes, Integer maxRetryTimes) {
        this.zkAddress = zkAddress;
        this.port = port;
        if(baseSleepTimes == null){
            this.baseSleepTimes = 1000;
        } else {
            this.baseSleepTimes = baseSleepTimes;
        }
        if(maxRetryTimes == null){
            this.maxRetryTimes = 3;
        } else {
            this.maxRetryTimes = maxRetryTimes;
        }
    }

    public int getBaseSleepTimes() {
        return baseSleepTimes;
    }

    public int getMaxRetryTimes() {
        return maxRetryTimes;
    }

    public void setBaseSleepTimes(int baseSleepTimes) {
        this.baseSleepTimes = baseSleepTimes;
    }

    public void setMaxRetryTimes(int maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
    }

    public abstract void updateNodeData(String address, String data);

    /**
     * 拉取节点的数据
     *
     * @param address
     * @return
     */
    public abstract String getNodeData(String address);

    /**
     * 创建持久化类型节点数据信息
     *
     * @param address
     * @param data
     */
    public abstract void createPersistentData(String address, String data);

    /**
     * @param address
     * @param data
     */
    public abstract void createPersistentWithSeqData(String address, String data);


    /**
     * 创建有序且临时类型节点数据信息
     *
     * @param address
     * @param data
     */
    public abstract void createTemporarySeqData(String address, String data);


    /**
     * 创建临时节点数据类型信息
     *
     * @param address
     * @param data
     */
    public abstract void createTemporaryData(String address, String data);

    /**
     * 设置某个节点的数值
     *
     * @param address
     * @param data
     */
    public abstract void setTemporaryData(String address,String data);

    /**
     * 断开zk的客户端链接
     */
    public abstract void destroy();


    /**
     * 展示节点下边的数据
     *
     * @param address
     */
    public abstract List<String> listNode(String address);


    /**
     * 删除节点下边的数据
     *
     * @param address
     * @return
     */
    public abstract boolean deleteNode(String address);


    /**
     * 判断是否存在其他节点
     *
     * @param address
     * @return
     */
    public abstract boolean existNode(String address);

}
