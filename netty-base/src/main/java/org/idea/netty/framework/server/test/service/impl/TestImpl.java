package org.idea.netty.framework.server.test.service.impl;

import org.idea.netty.framework.server.common.Service;
import org.idea.netty.framework.server.test.service.Test;

/**
 * @author linhao
 * @date created in 11:28 上午 2020/10/7
 */
@Service(interfaceName = "testImpl")
public class TestImpl implements Test {

    @Override
    public void doTest(String arg) {
        System.out.println("this is doTest: arg is :" + arg);
    }

    @Override
    public String testStr(String arg) {
        System.out.println("test");
        return "success-" + arg;
    }


}
