package org.idea.netty.framework.server.register.zookeeper;

import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.register.Register;
import org.idea.netty.framework.server.register.RegisterFactory;

/**
 * @author linhao
 * @date created in 9:23 下午 2020/10/14
 */
public class ZookeeperRegisterFactory implements RegisterFactory {

    private static ZookeeperRegister zookeeperRegister;

    private static boolean IS_TASK_START = false;

    public ZookeeperRegisterFactory() {
    }

    public static ZookeeperRegister getZookeeperRegister() {
        return zookeeperRegister;
    }

    @Override
    public Register createRegister(URL url) {
        if (zookeeperRegister == null) {
            synchronized (ZookeeperRegister.class) {
                if (zookeeperRegister == null) {
                    //这里面初始化了zookeeper
                    zookeeperRegister = new ZookeeperRegister(url);
                }
            }
        }
        return zookeeperRegister;
    }

    /**
     * 开启监听注册中心数据变化的任务
     */
    @Override
    public void startListenTask() {
        synchronized (this) {
            if (IS_TASK_START) {
                return;
            }
            zookeeperRegister.startListenTask();
            IS_TASK_START = false;
        }
    }
}
