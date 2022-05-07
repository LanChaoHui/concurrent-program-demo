package com.panhu.concurrent;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class WaitTest {
    /**
     * 创建资源
     */
    static Queue<String> queue = new LinkedList<String>();
    public static final Integer MAX_SIZE = 10;

    public static void main(String[] args) {
        Runable1 runable1 = new Runable1();
        Runable2 runable2 = new Runable2();
        new Thread(runable2).start();
        new Thread(runable1).start();
    }

    public static class Runable1 implements Runnable {


        public void run() {
            // 生产线程
            synchronized (queue) {
                // 消费队列满，则等待队列空闲
                while (queue.size() == MAX_SIZE) {
                    try {
                        // 挂起当前线程，并释放通过同步块获取的queue上的锁，让消费者线程可以获取该锁，然后获取队列里面的元素
                        queue.wait();
                        System.out.println("Runable1挂起");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                // 空闲则生成元素，并通知消费者线程
                System.out.println("add");
                queue.add(UUID.randomUUID().toString());
                queue.notifyAll();
            }
        }
    }

    public static class Runable2 implements Runnable {


        public void run() {
            synchronized (queue) {
                try {
                    while (queue.size() == 0) {
                        queue.wait();
                        System.out.println("Runable2挂起");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // 消费元素，并通知唤醒生产者线程
                System.out.println("remove");
                queue.remove(0);
                queue.notifyAll();
            }
        }
    }
}
