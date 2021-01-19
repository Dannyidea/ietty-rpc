package org.idea.netty.framework.server.rpc;

/**
 * @Author linhao
 * @Date created in 11:32 上午 2021/1/17
 */
public class RpcContext {

    private boolean isFinish;

    private Object responseData;

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public Object getResponseData() {
        return responseData;
    }

    public void setResponseData(Object responseData) {
        this.responseData = responseData;
    }

    public RpcContext(boolean isFinish, Object responseData) {
        this.isFinish = isFinish;
        this.responseData = responseData;
    }
}
