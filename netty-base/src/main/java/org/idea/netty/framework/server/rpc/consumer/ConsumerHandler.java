package org.idea.netty.framework.server.rpc.consumer;

import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.idea.netty.framework.server.ClientApplication;
import org.idea.netty.framework.server.common.IettyConsumerCache;
import org.idea.netty.framework.server.config.IettyProtocol;
import org.idea.netty.framework.server.config.Invocation;
import org.idea.netty.framework.server.rpc.RpcData;
import org.idea.netty.framework.server.rpc.provider.RpcRespData;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import static org.idea.netty.framework.server.common.IettyConsumerCache.CLIENT_RESP_MAP;


/**
 * @Author linhao
 * @Date created in 3:42 下午 2021/2/10
 */
@Slf4j
public class ConsumerHandler implements ConsumerFactory {

    private Queue<RpcReqData> queue;

    private ChannelFuture channelFuture = null;

    public ConsumerHandler(int size) {
        this.queue = new ArrayBlockingQueue<>(size);
    }

    private long send() {
        RpcReqData rpcReqData = queue.poll();
        if (rpcReqData == null) {
            return -1;
        }
        log.info("[消费方投递消息] rpcReqData is {}", rpcReqData);
        Invocation invocation = rpcReqData.getInvocation();
        try {
            channelFuture = ClientApplication.initClient(invocation.getReferUrl());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        IettyProtocol iettyProtocol = buildIettyProtocol(invocation);
        //这里面发送请求到服务端
        channelFuture.channel().writeAndFlush(iettyProtocol);
        //暂时为null
        RpcRespData rpcRespData = new RpcRespData();
        CLIENT_RESP_MAP.put(iettyProtocol.getClientSessionId(), rpcRespData);
        return iettyProtocol.getClientSessionId();
    }

    @Override
    public long saveInQueue(RpcReqData rpcReqData) {
        queue.add(rpcReqData);
        return this.send();
    }

    @Override
    public void respHandle(RpcData rpcData) {

    }


    /**
     * 构建请求协议数据
     *
     * @param invocation
     * @return IettyProtocol对象
     */
    public IettyProtocol buildIettyProtocol(Invocation invocation) {
        IettyProtocol iettyProtocol = new IettyProtocol();
        iettyProtocol.setBody(invocation.toByteArray());
        iettyProtocol.setEvent((byte) 1);
        iettyProtocol.setReqOrResp((byte) 0);
        iettyProtocol.setSerializationType("jdk");
        iettyProtocol.setClientSessionId((int) IettyConsumerCache.SESSION_ID.getAndIncrement());
        //0正常请求状态
        iettyProtocol.setStatus((short) 0);
        //随机数字
        iettyProtocol.setMAGIC(new Random().nextInt(10000));
        return iettyProtocol;
    }


    public static void main(String[] args) {

    }
}
