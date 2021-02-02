package org.idea.netty.framework.server.spi;

import org.idea.netty.framework.server.common.Invoker;
import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.config.Invocation;

import java.util.List;

/**
 * @author linhao
 * @date created in 5:48 下午 2020/10/17
 */
public interface LoadBalance {

    /**
     * 选择相应的服务模块
     *
     * @param invocation
     * @return
     */
    void doSelect(Invocation invocation);
}
