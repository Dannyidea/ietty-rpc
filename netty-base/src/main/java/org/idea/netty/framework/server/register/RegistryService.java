package org.idea.netty.framework.server.register;

import org.idea.netty.framework.server.common.URL;

/**
 * 注册服务
 * 默认的实现机制为：重试机制
 * 这里只给定了模版设计，具体实现由子类去实现，这里对应了模版方法设计模式的特点
 *
 * @Author linhao
 * @Date created in 11:52 上午 2021/1/6
 */
public interface RegistryService {


    /**
     * 注册url
     *
     * 将dubbo服务写入注册中心节点
     * 当出现网络抖动的时候需要进行适当的重试做法
     * 注册服务url的时候需要写入持久化文件中
     *
     * @param url
     */
    void register(URL url);

    /**
     * 服务下线
     *
     * 持久化节点是无法进行服务下线操作的
     * 下线的服务必须保证url是完整匹配的
     * 移除持久化文件中的一些内容信息
     *
     * @param url
     */
    void unRegister(URL url);


    /**
     * 执行订阅内部逻辑
     *
     * 例如当指定当provider节点上边增加了url，这时候需要去通知到对应到consumer方，具体逻辑写在这里
     * 另外当节点内部当url发生更新当时候，这里也需要被通知到位
     *
     * @param url
     */
    void doSubscribe(URL url);


    /**
     * 执行取消订阅内部的逻辑
     *
     * @param url
     */
    void doUnSubscribe(URL url);
}
