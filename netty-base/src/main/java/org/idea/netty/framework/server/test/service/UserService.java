package org.idea.netty.framework.server.test.service;

import java.util.List;

/**
 * @Author linhao
 * @Date created in 5:50 下午 2021/1/15
 */
public interface UserService {

    void addUser();

    List<String> findAll();
}
