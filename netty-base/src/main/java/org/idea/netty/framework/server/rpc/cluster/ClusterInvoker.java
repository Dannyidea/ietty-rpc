package org.idea.netty.framework.server.rpc.cluster;

/**
 * @Author linhao
 * @Date created in 2:11 下午 2021/1/30
 */
public class ClusterInvoker {

    private String host;

    private Integer port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public ClusterInvoker(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public ClusterInvoker() {
    }
}
