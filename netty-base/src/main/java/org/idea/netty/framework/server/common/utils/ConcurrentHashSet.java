package org.idea.netty.framework.server.common.utils;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author linhao
 * @Date created in 12:26 下午 2021/1/6
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> implements Set<E>, Serializable {
    private static final long serialVersionUID = 6134304861315059381L;

    private static final Object PRESENT = new Object();

    private final ConcurrentHashMap<E, Object> map;

    public ConcurrentHashSet(){
        this.map = new ConcurrentHashMap<>();
    }

    public ConcurrentHashSet(int initialCapacity) {
        this.map = new ConcurrentHashMap<>(initialCapacity);
    }

    @Override
    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }

    @Override
    public Iterator iterator() {
        return map.keySet().iterator();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }
}
