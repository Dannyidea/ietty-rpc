package org.idea.netty.framework.server.test.service.impl;

import org.idea.netty.framework.server.common.Service;
import org.idea.netty.framework.server.test.service.GoodsService;

import java.util.Arrays;
import java.util.List;

/**
 * @Author linhao
 * @Date created in 5:54 下午 2021/1/15
 */
@Service(interfaceName = "goodsServiceImpl")
public class GoodsServiceImpl implements GoodsService {

    @Override
    public void addGoods() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> findAll() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList("t1","t2");
    }
}
