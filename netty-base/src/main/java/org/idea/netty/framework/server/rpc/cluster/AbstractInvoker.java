package org.idea.netty.framework.server.rpc.cluster;

import org.idea.netty.framework.server.config.Invocation;
import org.idea.netty.framework.server.rpc.Result;

/**
 * @Author linhao
 * @Date created in 4:35 下午 2021/1/27
 */
public abstract class AbstractInvoker<T> implements Invoker<T> {


    @Override
    public abstract Result doResult(Invocation invocation);
}
