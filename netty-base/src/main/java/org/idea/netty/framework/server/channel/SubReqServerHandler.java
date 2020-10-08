package org.idea.netty.framework.server.channel;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.idea.netty.framework.server.bean.SubscribeResp;

@ChannelHandler.Sharable
public class SubReqServerHandler  extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeResp resp = (SubscribeResp) msg;
        if ("wcs".equals(resp.getDesc())) {
            System.out.println("Service accept client subsribe resp:[" + resp.toString()+"]");
            ctx.writeAndFlush(buildResponse(resp.getSubReqId()));
        }
    }

    private SubscribeResp buildResponse(int subReqId) {
        SubscribeResp resp = new SubscribeResp();
        resp.setSubReqId(subReqId);
        resp.setRespCode(0);
        resp.setDesc("receive success");
        return resp;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}