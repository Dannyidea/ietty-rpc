package org.idea.netty.framework.server.common;


import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

    private String applicationName;

    private  int port;

    private boolean syncSaveFile;

    /**
     * 包含了很多类型信息，包含种类有 方面名称列表，参数列表，权重，服务提供者地址，服务提供者端口号，创建节点的属性（临时节点还是持久化节点）
     */
    private  Map<String, String> parameters;

    /**
     * 方法全称呼 com.sise.idea.test.DemoService
     */
    private  String path;

    public URL() {
    }

    public URL(String protocol, String username, String password, String applicationName, int port, Map<String, String> parameters, String path) {
        this.protocol = protocol;
        this.username = username;
        this.password = password;
        this.applicationName = applicationName;
        this.port = port;
        this.parameters = parameters;
        this.path = path;
        this.syncSaveFile = false;
    }

    public URL(String protocol, String username, String password, String applicationName, int port, boolean syncSaveFile, Map<String, String> parameters, String path) {
        this.protocol = protocol;
        this.username = username;
        this.password = password;
        this.applicationName = applicationName;
        this.port = port;
        this.syncSaveFile = syncSaveFile;
        this.parameters = parameters;
        this.path = path;
    }

    public boolean isSyncSaveFile() {
        return syncSaveFile;
    }

    public void setSyncSaveFile(boolean syncSaveFile) {
        this.syncSaveFile = syncSaveFile;
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

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }


    private String toServicePath(){
        return "";
    }

    private String toCategorieaPath(){
        return "";
    }

    public Object getParameter(String key,Object defaultValue){
        Object value =  this.getParameters().get(key);
        return value!=null ? value : defaultValue;
    }

    @Override
    public String toString() {
        return "URL{" +
                "protocol='" + protocol + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", port=" + port +
                ", syncSaveFile=" + syncSaveFile +
                ", parameters=" + parameters +
                ", path='" + path + '\'' +
                '}';
    }

    public static String buildUrlStr(URL url) {
        String methods = url.getParameters().get("methods");
        String weight = url.getParameters().get("weight");
        String host = url.getParameters().get("host");
        String port = url.getParameters().get("port");
        return new String((url.getProtocol() + "://" + url.getPath() + ";" + methods + ";" + weight + ";" + host + ";" + port + ";"
                + url.getUsername() + ";" + url.getPassword()).getBytes(), StandardCharsets.UTF_8);
    }
}
