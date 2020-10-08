package org.idea.netty.framework.server.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author linhao
 * @date created in 3:33 下午 2020/10/7
 */
@NoArgsConstructor
@Data
public class ApplicationConfig {

    /**
     * 服务名称
     */
    private String name;

    /**
     * 版本号码
     */
    private String version;

    /**
     * 应用负责人
     */
    private String owner;

}
