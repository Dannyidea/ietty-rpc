package org.idea.netty.framework.server.rpc;

import org.idea.netty.framework.server.config.IettyProtocol;

import java.io.Serializable;

/**
 * @Author linhao
 * @Date created in 3:39 下午 2021/2/10
 */
public class RpcData implements Serializable {

    private static final long serialVersionUID = -1773073906814186640L;

    private IettyProtocol iettyProtocol;

    private int clientSessionId;


    public IettyProtocol getIettyProtocol() {
        return iettyProtocol;
    }

    public void setIettyProtocol(IettyProtocol iettyProtocol) {
        this.iettyProtocol = iettyProtocol;
    }

    public int getClientSessionId() {
        return clientSessionId;
    }

    public void setClientSessionId(int clientSessionId) {
        this.clientSessionId = clientSessionId;
    }

    @Override
    public String toString() {
        return "RpcData{" +
                "iettyProtocol=" + iettyProtocol +
                ", clientSessionId=" + clientSessionId +
                '}';
    }

    public RpcData(IettyProtocol iettyProtocol, int clientSessionId) {
        this.iettyProtocol = iettyProtocol;
        this.clientSessionId = clientSessionId;
    }
}
