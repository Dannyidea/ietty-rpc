package org.idea.netty.framework.server.common;


import io.netty.util.internal.StringUtil;
import org.idea.netty.framework.server.config.ReferenceConfig;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static org.idea.netty.framework.server.common.ConfigPropertiesKey.ROOT_PATH;

/**
 * 配置总线
 *
 * @author linhao
 * @date created in 10:48 下午 2020/10/13
 */
public class URL implements Serializable {

    private String protocol;

    private String username;

    private String password;

    private String applicationName;

    private boolean syncSaveFile;
    /**
     * 包含了很多类型信息，包含种类有 方面名称列表，参数列表，权重，服务提供者地址，服务提供者端口号，创建节点的属性（临时节点还是持久化节点）
     */
    private Map<String, String> parameters = new ConcurrentHashMap<>();

    /**
     * 方法全称呼 com.sise.idea.test.DemoService
     */
    private String path;

    public URL() {
    }

    public URL(String protocol, String username, String password, String applicationName, Map<String, String> parameters, String path) {
        this.protocol = protocol;
        this.username = username;
        this.password = password;
        this.applicationName = applicationName;
        this.parameters = parameters;
        this.path = path;
        this.syncSaveFile = false;
    }

    public URL(String protocol, String username, String password, String applicationName, boolean syncSaveFile, Map<String, String> parameters, String path) {
        this.protocol = protocol;
        this.username = username;
        this.password = password;
        this.applicationName = applicationName;
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

    private String toServicePath() {
        return "";
    }

    private String toCategorieaPath() {
        return "";
    }

    public Object getParameter(String key, Object defaultValue) {
        Object value = this.getParameters().get(key);
        return value != null ? value : defaultValue;
    }

    public Object getParameter(String key) {
        Object value = this.getParameters().get(key);
        return value != null ? value : null;
    }


    @Override
    public String toString() {
        return "URL{" +
                "protocol='" + protocol + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", applicationName='" + applicationName + '\'' +
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

    public static String buildConsumerUrlStr(ReferenceConfig referenceConfig) {
        return new String((referenceConfig.getProtocol() + "://" + referenceConfig.getApplication() + ";" + referenceConfig.getAddress()).getBytes(), StandardCharsets.UTF_8);
    }

    public static URL convertFromUrlStr(String urlStr) {
        String protocol = urlStr.substring(0, urlStr.indexOf("://"));
        String path = urlStr.substring(protocol.length() + 3, urlStr.indexOf(";"));
        String[] items = urlStr.split(";");
        String method = items[1];
        String weight = items[2];
        String host = items[3];
        String port = items[4];
        String username = items[5];
        String password = items[6];
        URL url = new URL();
        url.setPath(path);
        url.setProtocol(protocol);
        Map<String, String> parameterMap = new HashMap<>(4);
        parameterMap.put("methods", method);
        parameterMap.put("port", port);
        parameterMap.put("weight", weight);
        parameterMap.put("host", host);
        url.setParameters(parameterMap);
        url.setUsername(username);
        url.setPassword(password);
        return url;
    }


    /**
     * 比较两个url是否一致相同
     *
     * @param url
     * @return
     */
    public boolean compareUrlIsSame(URL url) {
        boolean portSame = this.getParameter("port").equals(url.getParameter("port"));
        boolean hostSame = this.getParameter("host").equals(url.getParameter("host"));
        return portSame && hostSame;
    }

    public String getInterfacePath(){
        String interfacePath = (String) this.getParameter("interfacePath");
        if(!StringUtil.isNullOrEmpty(interfacePath)){
            return interfacePath.substring(ROOT_PATH.length() + 1).replace("/provider", "");
        }
        return null;
    }
}
