package org.idea.netty.framework.server.channel;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.idea.netty.framework.server.bean.User;
import org.idea.netty.framework.server.config.IettyProtocol;
import org.idea.netty.framework.server.config.Invocation;


/**
 * 共享通道，意味着每次请求进来都会使用同一个handler
 *
 * @author linhao
 * @date created in 5:13 下午 2020/10/1
 */
@ChannelHandler.Sharable
public class BaseInitServerChannelHandler extends ChannelInboundHandlerAdapter {

    private static int i = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        i++;
        IettyProtocol data = (IettyProtocol) msg;
        Invocation invocation = toInvocationFromByte(data.getBody());
        AllChannelHandler.channelRead(new Runnable() {
            @Override
            public void run() {
                Class clazz = invocation.getServiceClass();

            }
        });

        ctx.writeAndFlush(data);
    }

    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("出现了异常问题");
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }

    private Invocation toInvocationFromByte(byte[] bytes) {
        String json = new String(bytes);
        return JSON.parseObject(json,Invocation.class);
    }
}
