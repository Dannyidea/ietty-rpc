package org.idea.netty.framework.server.rpc.cluster;

import org.idea.netty.framework.server.config.Invocation;
import org.idea.netty.framework.server.register.Node;
import org.idea.netty.framework.server.rpc.Result;

/**
 * @Author linhao
 * @Date created in 4:31 下午 2021/1/27
 */
public interface Invoker<T> extends Node {



    /**
     * 获取响应数据
     *
     * @return
     */
    Result doResult(Invocation invocation);
}
