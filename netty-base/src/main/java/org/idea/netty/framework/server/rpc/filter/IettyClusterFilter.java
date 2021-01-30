package org.idea.netty.framework.server.rpc.filter;

import org.idea.netty.framework.server.config.Invocation;
import org.idea.netty.framework.server.rpc.cluster.ClusterInvoker;
import org.idea.netty.framework.server.spi.filter.ConsumerFilter;

/**
 * @Author linhao
 * @Date created in 8:51 下午 2021/1/27
 */
public class IettyClusterFilter implements ConsumerFilter {

    @Override
    public void doFilter(Invocation invocation) {
        System.out.println("this is IettyClusterFilter");
    }
}
