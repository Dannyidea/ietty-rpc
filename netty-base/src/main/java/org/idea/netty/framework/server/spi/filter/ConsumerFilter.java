package org.idea.netty.framework.server.spi.filter;


import org.idea.netty.framework.server.config.Invocation;

/**
 * @Author linhao
 * @Date created in 5:33 下午 2021/1/27
 */
public interface ConsumerFilter  {

    void doFilter(Invocation invocation);
}
