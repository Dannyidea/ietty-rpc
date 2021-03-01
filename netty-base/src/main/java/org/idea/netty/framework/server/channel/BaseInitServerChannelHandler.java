package org.idea.netty.framework.server.channel;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.idea.netty.framework.server.config.IettyProtocol;
import org.idea.netty.framework.server.rpc.RpcData;

import java.util.concurrent.*;

import static org.idea.netty.framework.server.common.IettyProviderCache.*;


/**
 * 服务端
 * 共享通道，意味着每次请求进来都会使用同一个handler
 *
 * @author linhao
 * @date created in 5:13 下午 2020/10/1
 */

@ChannelHandler.Sharable
public class BaseInitServerChannelHandler extends ChannelInboundHandlerAdapter {


    public BaseInitServerChannelHandler(int size) {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        IettyProtocol data = (IettyProtocol) msg;
        RpcData rpcData = new RpcData(data, (int) data.getClientSessionId());
        rpcData.setIettyProtocol(data);
        providerHandler.receive(rpcData);
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("提交任务");
                FutureTask<IettyProtocol> futureTask = PROVIDER_RESP_DATA.get(data.getClientSessionId());
                long beginTime = System.currentTimeMillis();
                while (futureTask == null) {
                    futureTask = PROVIDER_RESP_DATA.get(data.getClientSessionId());
                    if (countTimeInterval(beginTime) >= 3000) {
                        System.out.println("请求超时");
                        return;
                    }
                }
                try {
                    PROVIDER_RESP_DATA.remove(data.getClientSessionId());
                    IettyProtocol iettyProtocol = futureTask.get();
                    ctx.writeAndFlush(iettyProtocol);
                    System.out.println("任务回写:" + data.getClientSessionId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public long countTimeInterval(long beginTime) {
        return (System.currentTimeMillis() - beginTime);
    }

    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) {
        System.out.println("通道刷新");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("出现了异常问题" + cause);
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }

}
