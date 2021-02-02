package org.idea.netty.framework.server.spi.loadbalance;

import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.config.Invocation;
import org.idea.netty.framework.server.spi.LoadBalance;

import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡组件
 *
 * @author linhao
 * @date created in 5:36 下午 2020/10/17
 */
public class RandomLoadBalance implements LoadBalance {


    @Override
    public void doSelect(Invocation invocation) {
        List<URL> urls = invocation.getUrls();
        Random random = new Random();
        int id = random.nextInt(urls.size());
        URL url = urls.get(id);
        System.out.println("选中的服务器url为：" + url);
        invocation.setReferUrl(url);
    }
}
