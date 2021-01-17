package org.idea.netty.framework.server.test.service;

import java.util.List;

/**
 * @Author linhao
 * @Date created in 5:53 下午 2021/1/15
 */
public interface GoodsService {

    void addGoods();

    List<String> findAll();
}
