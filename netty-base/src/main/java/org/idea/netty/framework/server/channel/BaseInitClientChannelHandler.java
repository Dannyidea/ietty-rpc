package org.idea.netty.framework.server.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.idea.netty.framework.server.config.IettyProtocol;
import org.idea.netty.framework.server.rpc.provider.RpcRespData;

import static org.idea.netty.framework.server.common.IettyConsumerCache.CLIENT_RESP_MAP;


/**
 * 客户端
 *
 * @author linhao
 * @date created in 9:04 上午 2020/10/2
 */
public class BaseInitClientChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        IettyProtocol iettyProtocol = (IettyProtocol) msg;
        if (CLIENT_RESP_MAP.containsKey(iettyProtocol.getClientSessionId())) {
            RpcRespData rpcRespData = CLIENT_RESP_MAP.get(iettyProtocol.getClientSessionId());
            rpcRespData.setIettyProtocol(iettyProtocol);
            System.out.println("响应数据为:"+rpcRespData);
        }
        //释放内存信息
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }
}
