package org.idea.netty.framework.server.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author linhao
 * @date created in 4:40 下午 2020/10/2
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements Serializable {

    private static final long serialVersionUID = -8386951545383872362L;

    private int id;

    private String username;
}
