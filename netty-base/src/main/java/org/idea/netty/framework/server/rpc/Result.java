package org.idea.netty.framework.server.rpc;

/**
 * 远程请求的响应值
 *
 * @Author linhao
 * @Date created in 4:32 下午 2021/1/27
 */
public interface Result {

    Object getValue();

    void setValue(Object value);
}
