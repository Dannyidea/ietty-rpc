package org.idea.netty.framework.server.rpc.filter.chain;

import java.util.Arrays;
import java.util.List;

/**
 * @Author linhao
 * @Date created in 2:41 下午 2021/1/30
 */
interface Filter {
    int invoke(Invoker filter);
}

class Filter1 implements Filter {

    @Override
    public int invoke(Invoker invoker) {
        System.out.println("filter--1");
        invoker.invoke();
        return 0;
    }
}

class Filter2 implements Filter {

    @Override
    public int invoke(Invoker invoker) {
        System.out.println("filter--2");
        return invoker.invoke();
    }
}

interface Invoker {
    public int invoke();
}

public class IettyFIlterChain {

    public static void main(String[] args) {
        List<Filter> filters = Arrays.asList(new Filter1(), new Filter2());
        Invoker last = new Invoker() {
            @Override
            public int invoke() {
                System.out.println("invoke");
                return 0;
            }
        };

        for (int i = filters.size() - 1; i >= 0; i--) {
            final Filter filter = filters.get(i);
            final Invoker next = last;
            last = new Invoker() {
                @Override
                public int invoke() {
                    return filter.invoke(next);
                }
            };
        }
        System.out.println("=====");
        last.invoke();
    }
}
