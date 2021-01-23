package org.idea.netty.framework.server.register.zookeeper;

import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.register.Register;
import org.idea.netty.framework.server.register.RegisterFactory;

/**
 * @author linhao
 * @date created in 9:23 下午 2020/10/14
 */
public class ZookeeperRegisterFactory implements RegisterFactory {

    private static  ZookeeperRegister zookeeperRegister;

    public ZookeeperRegisterFactory(){
    }

    public static ZookeeperRegister getZookeeperRegister(){
        return zookeeperRegister;
    }


    @Override
    public Register createRegister(URL url) {
        if(zookeeperRegister==null){
            synchronized (ZookeeperRegister.class){
                if(zookeeperRegister==null){
                    //这里面初始化了zookeeper
                    zookeeperRegister = new ZookeeperRegister(url);
                }
            }
        }
        return zookeeperRegister;
    }
}
