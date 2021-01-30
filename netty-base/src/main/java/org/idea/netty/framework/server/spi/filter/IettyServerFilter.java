package org.idea.netty.framework.server.spi.filter;

import org.idea.netty.framework.server.config.Invocation;

/**
 * ietty服务端过滤器
 *
 * @author linhao
 * @date created in 4:19 下午 2020/10/11
 */
public class IettyServerFilter implements ProviderFilter{

    /**
     * 执行过滤器函数
     */
    @Override
    public void doFilter(Invocation invocation){
        System.out.println("this is do filter");
    }
}
