package org.idea.netty.framework.server.rpc.provider;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author linhao
 * @Date created in 4:31 下午 2021/2/10
 */
public class ProviderQueue<T> {

    private Node<T> head;
    private AtomicInteger size = new AtomicInteger(0);
    private AtomicInteger count = new AtomicInteger(0);

    public ProviderQueue(int size) {
        this.size = new AtomicInteger(size);
    }

    public int getCount() {
        return count.get();
    }

    public int getSize() {
        return size.get();
    }

    public void push(T data) {
        synchronized (this){
            if (size.get() == count.get()) {
                return;
            }
            if (head == null || count.get()==0) {
                head = new Node<>(data, null);
                count.incrementAndGet();
                return;
            }
            Node<T> temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            count.incrementAndGet();
            temp.next = new Node(data, null);
        }
    }

    public T offer() {
        synchronized (this) {
            if (count.get() == 0) {
                head = null;
                return null;
            }
            if (count.get() == 1) {
                count.decrementAndGet();
                return head.data;
            }
            Node<T> result = head;
            Node<T> newHead = head.next;
            head = newHead;
            count.decrementAndGet();
            return result.data;
        }
    }


    private class Node<T> {
        private T data;
        private Node next;

        public Node(T data, Node next) {
            this.data = data;
            this.next = next;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

    public boolean isFull() {
        return this.getCount() == this.getSize();
    }

    public boolean isEmpty() {
        return this.getCount() == 0;
    }

    public static void main(String[] args) {
        ProviderQueue<Integer> providerQueue = new ProviderQueue(10);
        providerQueue.push(1);
        providerQueue.push(2);
        providerQueue.push(3);
        providerQueue.push(4);
        providerQueue.push(5);

        for (int i = 0; i < 15; i++) {
            Integer r = providerQueue.offer();
            if (r == null) {
                providerQueue.push(19);
            }
            System.out.println(r);
        }
    }


}
