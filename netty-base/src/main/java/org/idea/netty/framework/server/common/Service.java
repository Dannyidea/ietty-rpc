package org.idea.netty.framework.server.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author linhao
 * @date created in 3:35 下午 2020/10/7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Service {

    String interfaceName() default "";

    String filter() default "";

    int delay() default 0;
}
