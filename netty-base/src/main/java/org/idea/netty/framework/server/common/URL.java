package org.idea.netty.framework.server.common;

import java.io.Serializable;
import java.util.Map;

/**
 * 配置总线
 *
 * @author linhao
 * @date created in 10:48 下午 2020/10/13
 */
public class URL implements Serializable {

    private  String protocol;

    private  String username;

    private  String password;

    private  int port;

    /**
     * 包含了很多类型信息，包含种类有 方面名称列表，参数列表，权重，服务提供者地址，服务提供者端口号，创建节点的属性（临时节点还是持久化节点）
     */
    private  Map<String, String> parameters;

    /**
     * 方法全称呼 com.sise.idea.test.DemoService
     */
    private  String path;

    @Override
    public String toString() {
        return super.toString();
    }

    public URL(String protocol, String username, String password, Map<String, String> parameters, int port, String path) {
        this.protocol = protocol;
        this.username = username;
        this.password = password;
        this.parameters = parameters;
        this.port = port;
        this.path = path;
    }


    public String getProtocol() {
        return protocol;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getPath() {
        return path;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static String buildUrlStr(URL url) {
        String methods = url.getParameters().get("methods");
        String weight = url.getParameters().get("weight");
        String host = url.getParameters().get("host");
        String port = url.getParameters().get("port");
        return url.getProtocol() + "://" + url.getPath() + ";" + ";" + methods + ";" + weight + ";" + host + ";" + port + ";"
                + url.getUsername() + ";" + url.getPassword();
    }
}
