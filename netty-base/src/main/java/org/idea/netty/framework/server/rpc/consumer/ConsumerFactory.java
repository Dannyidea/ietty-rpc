package org.idea.netty.framework.server.rpc.consumer;

import org.idea.netty.framework.server.rpc.RpcData;

/**
 * @Author linhao
 * @Date created in 3:42 下午 2021/2/10
 */
public interface ConsumerFactory {

    /**
     * 将队列塞入发送队列中
     *
     * @param rpcReqData
     * @return sessionId;
     */
    long saveInQueue(RpcReqData rpcReqData);


    /**
     * 响应数据
     *
     * @param rpcData
     */
    void respHandle(RpcData rpcData);
}
