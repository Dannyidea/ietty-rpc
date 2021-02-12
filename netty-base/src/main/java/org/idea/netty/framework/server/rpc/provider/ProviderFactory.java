package org.idea.netty.framework.server.rpc.provider;

import org.idea.netty.framework.server.rpc.RpcData;

/**
 * 服务提供者生产工厂
 *
 * @Author linhao
 * @Date created in 3:35 下午 2021/2/10
 */
public interface ProviderFactory {

    /**
     * 接收数据并且塞入队列中
     *
     * @param rpcData
     */
    void receive(RpcData rpcData);


    /**
     * 逐个处理队列中的数据
     */
    void receiveHandler();
}
