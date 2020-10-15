package org.idea.netty.framework.server.register.zookeeper;

/**
 * @author linhao
 * @date created in 9:23 下午 2020/10/14
 */
public class ZookeeperRegisterFactory {

    private static  ZookeeperRegister zookeeperRegister;

    private ZookeeperRegisterFactory(){

    }

    /**
     * 初始化注册中心连接器
     *
     * @return
     */
    public static ZookeeperRegister buildZookeeperRegister(){
        if(zookeeperRegister==null){
            synchronized (ZookeeperRegister.class){
                if(zookeeperRegister==null){
                    //这里面初始化了zookeeper
                    zookeeperRegister = new ZookeeperRegister();
                }
            }
        }
        return zookeeperRegister;
    }
}
