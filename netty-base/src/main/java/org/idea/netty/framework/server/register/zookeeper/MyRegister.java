package org.idea.netty.framework.server.register.zookeeper;

import org.idea.netty.framework.server.common.URL;

/**
 * @Author linhao
 * @Date created in 5:38 下午 2021/1/1
 */
public class MyRegister extends ZookeeperRegister {

    public MyRegister(URL url) {
        super(url);
    }

    @Override
    public void register(URL url) {
        System.out.println("this is test");
    }
}
