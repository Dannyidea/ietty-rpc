package org.idea.netty.framework.server.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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
    protected short MAGIC = 0;

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
    private byte event=0;

    /**
     * 序列化类型
     */
    private String serializationType;

    /**
     * 状态
     */
    private short status;

    /**
     * 消息体
     */
    private byte[] body;

    public short getMAGIC() {
        return MAGIC;
    }

    public void setMAGIC(short MAGIC) {
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

    @Override
    public String toString() {
        return "IettyProtocol{" +
                "MAGIC=" + MAGIC +
                ", reqOrResp=" + reqOrResp +
                ", way=" + way +
                ", event=" + event +
                ", serializationType='" + serializationType + '\'' +
                ", status=" + status +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
