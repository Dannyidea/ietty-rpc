package org.idea.netty.framework.server.register;

import org.idea.netty.framework.server.common.URL;

/**
 * @Author linhao
 * @Date created in 5:32 下午 2021/1/1
 */
public interface RegisterFactory {

    Register createRegister(URL ur);

    void startListenTask();
}
