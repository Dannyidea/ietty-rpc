package org.idea.netty.framework.server.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.idea.netty.framework.server.bean.SubscribeResp;

public class SubReqClientHandler extends ChannelInboundHandlerAdapter {

    public SubReqClientHandler() {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i< 10000; i++) {
            ctx.write(subReq(i));
        }
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Service accept client subsribe resp:[" + msg+"]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private Object subReq(int i) {
        SubscribeResp subscribeResp = new SubscribeResp();
        subscribeResp.setSubReqId(i);
        subscribeResp.setDesc("wcs");
        return subscribeResp;
    }
}