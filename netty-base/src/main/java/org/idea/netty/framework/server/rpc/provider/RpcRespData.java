package org.idea.netty.framework.server.rpc.provider;

import org.idea.netty.framework.server.config.IettyProtocol;

import java.io.Serializable;

/**
 * @Author linhao
 * @Date created in 8:35 上午 2021/2/12
 */
public class RpcRespData implements Serializable {

    private static final long serialVersionUID = -8718946445490613228L;

    private IettyProtocol iettyProtocol;

    public IettyProtocol getIettyProtocol() {
        return iettyProtocol;
    }

    public void setIettyProtocol(IettyProtocol iettyProtocol) {
        this.iettyProtocol = iettyProtocol;
    }

    @Override
    public String toString() {
        return "RpcRespData{" +
                "iettyProtocol=" + iettyProtocol +
                '}';
    }
}
