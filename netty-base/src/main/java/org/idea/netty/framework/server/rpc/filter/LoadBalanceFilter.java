package org.idea.netty.framework.server.rpc.filter;

import org.idea.netty.framework.server.config.Invocation;
import org.idea.netty.framework.server.spi.LoadBalance;
import org.idea.netty.framework.server.spi.filter.ConsumerFilter;
import org.idea.netty.framework.server.spi.loadbalance.WeightLoadBalance;


/**
 * 随机负载均衡过滤器
 *
 * @Author linhao
 * @Date created in 4:29 下午 2021/2/1
 */
public class LoadBalanceFilter implements ConsumerFilter {

    @Override
    public void doFilter(Invocation invocation) {
        LoadBalance loadBalance = new WeightLoadBalance();
        loadBalance.doSelect(invocation);
    }

}
