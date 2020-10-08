package org.idea.netty.framework.server.test;

import org.idea.netty.framework.server.common.Service;

/**
 * @author linhao
 * @date created in 11:28 上午 2020/10/7
 */
@Service(interfaceName = "test",filter = "testFilter")
public class TestImpl implements Test{

    @Override
    public void doTest() {
        System.out.println("this is doTest");
    }
}
