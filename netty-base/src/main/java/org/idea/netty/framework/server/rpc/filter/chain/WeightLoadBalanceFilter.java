package org.idea.netty.framework.server.rpc.filter.chain;

import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.config.Invocation;
import org.idea.netty.framework.server.spi.filter.ConsumerFilter;

import java.util.List;

/**
 * @Author linhao
 * @Date created in 5:52 下午 2021/2/1
 */
public class WeightLoadBalanceFilter implements ConsumerFilter {


    @Override
    public void doFilter(Invocation invocation) {
        List<URL> urls = invocation.getUrls();
        short[] weightArr = new short[urls.size()];
        for (URL url : urls) {

        }
        for (URL url : urls) {
            String weight = url.getParameters().get("weight");
        }
    }
}
