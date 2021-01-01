package org.idea.netty.framework.server.config;


/**
 * 注册中心的相关配置
 *
 * @Author linhao
 * @Date created in 4:33 下午 2021/1/1
 */
public class RegisterConfig {

    private String address;

    private Integer port;

    private String protocol;

    private String username;

    private String password;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
