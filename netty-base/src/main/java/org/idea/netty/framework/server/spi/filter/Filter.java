package org.idea.netty.framework.server.spi.filter;

import org.idea.netty.framework.server.config.Invocation;

/**
 * ietty内部定义的过滤器
 *
 * @author linhao
 * @date created in 4:21 下午 2020/10/11
 */
public interface Filter {

    /**
     * 执行过滤链路
     *
     * @param invocation
     */
    void doFilter(Invocation invocation);
}
