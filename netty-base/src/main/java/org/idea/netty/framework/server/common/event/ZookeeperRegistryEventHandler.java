package org.idea.netty.framework.server.common.event;

import org.idea.netty.framework.server.register.zookeeper.ZookeeperRegister;


/**
 * zk注册中心事件处理器
 *
 * @Author linhao
 * @Date created in 4:34 下午 2021/2/2
 */
public class ZookeeperRegistryEventHandler {

    private ZookeeperRegister zookeeperRegister;

    public ZookeeperRegistryEventHandler(ZookeeperRegister zookeeperRegister){
        this.zookeeperRegister = zookeeperRegister;
    }

    public void bind(EventObject eventObject) {

    }
}
