package org.idea.netty.framework.server.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class SubscribeResp  implements Serializable {
    private static final long serialVersionUID = -4261173283103510587L;

    private int subReqId;

    private int respCode;

    private String desc;
    @Override
    public String toString() {
        return "subReqId="+subReqId+" respCode="+respCode+" desc="+desc;
    }
}