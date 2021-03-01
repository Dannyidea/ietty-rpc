package org.idea.netty.framework.server.config;

import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;

/**
 * @author linhao
 * @date created in 9:15 下午 2020/10/7
 */
public class IettyProtocol implements Serializable {

    private static final long serialVersionUID = -7523782352702351753L;
    /**
     * 魔数
     */
    protected long MAGIC = 0;

    private String requestId;

    private long clientSessionId;

    private ChannelHandlerContext channelHandlerContext;

    /**
     * 0请求 1响应
     */
    protected byte reqOrResp = 0;

    /**
     * 0需要从服务端返回数据 1不需要从服务端响应数据
     */
    protected final byte way = 0;

    /**
     * 0是心跳时间，1不是心跳事件
     */
    private byte event = 0;

    /**
     * 序列化类型
     */
    private String serializationType;

    /**
     * 状态
     */
    private short status;

    /**
     * 返回的数据类型格式
     */
    private Type type;

    /**
     * 消息体 请求方发送的函数类型，参数信息都存在这里， 接收方响应的信息也都存在这里
     */
    private byte[] body;

    public long getMAGIC() {
        return MAGIC;
    }

    public void setMAGIC(long MAGIC) {
        this.MAGIC = MAGIC;
    }

    public byte getReqOrResp() {
        return reqOrResp;
    }

    public void setReqOrResp(byte reqOrResp) {
        this.reqOrResp = reqOrResp;
    }

    public byte getWay() {
        return way;
    }

    public byte getEvent() {
        return event;
    }

    public void setEvent(byte event) {
        this.event = event;
    }

    public String getSerializationType() {
        return serializationType;
    }

    public void setSerializationType(String serializationType) {
        this.serializationType = serializationType;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public long getClientSessionId() {
        return clientSessionId;
    }

    public void setClientSessionId(long clientSessionId) {
        this.clientSessionId = clientSessionId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    @Override
    public String toString() {
        return "IettyProtocol{" +
                "MAGIC=" + MAGIC +
                ", requestId='" + requestId + '\'' +
                ", clientSessionId=" + clientSessionId +
                ", reqOrResp=" + reqOrResp +
                ", way=" + way +
                ", event=" + event +
                ", serializationType='" + serializationType + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
