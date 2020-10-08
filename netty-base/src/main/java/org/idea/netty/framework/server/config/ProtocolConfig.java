package org.idea.netty.framework.server.config;

import lombok.Data;

/**
 * @author linhao
 * @date created in 3:45 下午 2020/10/7
 */
@Data
public class ProtocolConfig {

    /**
     * 域名
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 协议名称 例如说 ietty,jvm,http
     */
    private String name;
}
