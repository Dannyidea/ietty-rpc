package org.idea.netty.framework.server.spi.loadbalance;

import org.idea.netty.framework.server.common.Invoker;
import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.config.Invocation;
import org.idea.netty.framework.server.spi.LoadBalance;
import org.idea.netty.framework.server.util.StringUtils;

import java.util.List;

/**
 * 随机负载均衡组件
 *
 * @author linhao
 * @date created in 5:36 下午 2020/10/17
 */
public class RandomLoadBalance implements LoadBalance {


    @Override
    public Invoker doSelect(List<Invoker> invokerList, URL url, Invocation invocation) {

        return null;
    }
}
