package org.idea.netty.framework.server.rpc.consumer;

import org.idea.netty.framework.server.config.Invocation;

import java.io.Serializable;

/**
 * @Author linhao
 * @Date created in 3:47 下午 2021/2/11
 */
public class RpcReqData implements Serializable {
    private static final long serialVersionUID = 1350855935040168183L;

    private Invocation invocation;

    public Invocation getInvocation() {
        return invocation;
    }

    public void setInvocation(Invocation invocation) {
        this.invocation = invocation;
    }

    public RpcReqData(Invocation invocation) {
        this.invocation = invocation;
    }
}
