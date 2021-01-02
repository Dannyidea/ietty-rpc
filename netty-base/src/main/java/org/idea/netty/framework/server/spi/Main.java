package org.idea.netty.framework.server.spi;

import java.util.ServiceLoader;

/**
 * @author linhao
 * @date created in 11:07 上午 2020/10/7
 */
public class Main {

    public static void main(String[] args) {
        ServiceLoader<PersonAction> serviceLoader = ServiceLoader.load(PersonAction.class);
        serviceLoader.forEach(PersonAction::say);
        System.out.println("this is test");
    }
}
