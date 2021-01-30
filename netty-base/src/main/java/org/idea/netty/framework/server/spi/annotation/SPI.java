package org.idea.netty.framework.server.spi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author linhao
 * @Date created in 5:45 下午 2021/1/27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPI {

    boolean isConsumer() default false;

    boolean isProvider() default false;
}
