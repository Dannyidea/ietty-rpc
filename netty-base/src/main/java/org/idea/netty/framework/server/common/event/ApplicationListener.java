package org.idea.netty.framework.server.common.event;

/**
 * 事件监听器
 *
 * @Author linhao
 * @Date created in 4:26 下午 2021/2/2
 */
public interface ApplicationListener {

    void bind(EventObject eventObject);
}
