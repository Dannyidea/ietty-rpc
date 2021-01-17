package org.idea.netty.framework.server.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.idea.netty.framework.server.config.IettyProtocol;
import org.idea.netty.framework.server.proxy.JdkProxyFactory;


/**
 * @author linhao
 * @date created in 9:04 上午 2020/10/2
 */
public class BaseInitClientChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        IettyProtocol iettyProtocol = (IettyProtocol) msg;
        byte[] bytes = iettyProtocol.getBody();
        String resultMsg = new String(bytes);
        //接收请求并且处理响应信息
        System.out.println(resultMsg);
        JdkProxyFactory.returnValue = resultMsg;
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
