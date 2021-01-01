package org.idea.netty.framework.server.register.zookeeper;

import org.idea.netty.framework.server.register.Register;
import org.idea.netty.framework.server.register.RegisterFactory;

/**
 * @Author linhao
 * @Date created in 5:39 下午 2021/1/1
 */
public class MyRegisterFactory implements RegisterFactory {

    @Override
    public Register createRegister() {
        return new MyRegister();
    }
}
