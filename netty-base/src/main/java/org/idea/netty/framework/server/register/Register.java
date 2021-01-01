package org.idea.netty.framework.server.register;

import org.idea.netty.framework.server.common.URL;

/**
 * @author linhao
 * @date created in 10:47 下午 2020/10/13
 */
public interface Register {

    /**
     * 注册服务
     *
     * @param url
     * @return
     */
    boolean register(URL url);

}