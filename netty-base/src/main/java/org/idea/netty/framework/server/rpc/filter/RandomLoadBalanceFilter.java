package org.idea.netty.framework.server.rpc.filter;

import lombok.extern.slf4j.Slf4j;
import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.config.Invocation;
import org.idea.netty.framework.server.spi.filter.ConsumerFilter;

import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡过滤器
 *
 * @Author linhao
 * @Date created in 4:29 下午 2021/2/1
 */
public class RandomLoadBalanceFilter implements ConsumerFilter {


    @Override
    public void doFilter(Invocation invocation) {
        List<URL> urls = invocation.getUrls();
        Random random = new Random();
        int id = random.nextInt(urls.size());
        URL url = urls.get(id);
        System.out.println("选中的服务器url为：" + url);
        invocation.setReferUrl(url);
    }

}
