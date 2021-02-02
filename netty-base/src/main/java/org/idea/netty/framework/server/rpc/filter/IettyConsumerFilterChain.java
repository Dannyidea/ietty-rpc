package org.idea.netty.framework.server.rpc.filter;

import org.idea.netty.framework.server.config.Invocation;
import org.idea.netty.framework.server.spi.filter.ConsumerFilter;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author linhao
 * @Date created in 5:36 下午 2021/1/27
 */
public class IettyConsumerFilterChain {

    private static List<ConsumerFilter> filterList = new LinkedList<>();

    static {
//        filterList.add(new IettyConsumerInitFilter());
//        filterList.add(new IettyClusterFilter());
        filterList.add(new LoadBalanceFilter());
    }

    public IettyConsumerFilterChain(){

    }

    public IettyConsumerFilterChain addFilter(ConsumerFilter consumerFilter){
        filterList.add(consumerFilter);
        return this;
    }

    public static void doFilter(Invocation invocation){
        for (ConsumerFilter consumerFilter : filterList) {
            consumerFilter.doFilter(invocation);
        }
    }
}
