package org.idea.netty.framework.server.test.service.impl;

import org.idea.netty.framework.server.common.Service;
import org.idea.netty.framework.server.test.service.UserService;

import java.util.Arrays;
import java.util.List;

/**
 * @Author linhao
 * @Date created in 5:51 下午 2021/1/15
 */
@Service(interfaceName = "userService")
public class UserServiceImpl implements UserService {

    @Override
    public void addUser() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> findAll() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("findAll");
        return Arrays.asList("idea1","idea2");
    }
}
