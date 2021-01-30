package org.idea.netty.framework.server.rpc.cluster;

import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.config.Invocation;
import org.idea.netty.framework.server.rpc.Result;

/**
 * @Author linhao
 * @Date created in 4:37 下午 2021/1/27
 */
public class IettyInvoker extends AbstractInvoker {

    @Override
    public Result doResult(Invocation invocation) {
        invocation.getServiceName();
        return null;
    }

    @Override
    public URL getUrl() {
        return null;
    }

}
