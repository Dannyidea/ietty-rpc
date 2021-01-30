package org.idea.netty.framework.server.rpc.filter;

import org.idea.netty.framework.server.config.Invocation;
import org.idea.netty.framework.server.spi.filter.ConsumerFilter;

import java.util.Set;

/**
 * @Author linhao
 * @Date created in 8:57 下午 2021/1/27
 */
public class IettyConsumerInitFilter implements ConsumerFilter {

    @Override
    public void doFilter(Invocation invocation) {
        invocation.setReferUrl(invocation.getUrls().get(0));
        System.out.println("this is IettyConsumerInitFilter");
    }
}
