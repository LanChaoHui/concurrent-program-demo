package com.panhu.concurrent;

import java.util.concurrent.atomic.AtomicLong;

public class Atomic {
    // 1. 创建Long型原子计数器
    private static AtomicLong atomicLong = new AtomicLong();
    // 2. 创建数据源
    private static Integer[] arrayOne = new Integer[]{0, 1, 2, 3, 0, 5, 6, 0, 56, 0};
    private static Integer[] arrayTwo = new Integer[]{10, 1, 2, 3, 0, 5, 6, 0, 56, 0};

    public static void main(String[] args) throws InterruptedException {
        // 3. 线程One 统计arrayOne 中0的个数
        Thread threadOne = new Thread(new Runnable() {
            int size = arrayOne.length;

            @Override
            public void run() {
                for (int i = 0; i < size; i++) {
                    if (arrayOne[i].intValue() == 0) {
                        atomicLong.incrementAndGet();
                    }
                }
            }
        });
        // 4.线程Two统计数组arrayTwo中0的个数
        Thread threadTwo = new Thread(new Runnable() {
            @Override
            public void run() {
                int size = arrayTwo.length;
                for (int i = 0; i < size; i++) {
                    if (arrayTwo[i].intValue() == 0) {
                        atomicLong.incrementAndGet();
                    }
                }
            }
        });
        // 4.启动线程
        threadOne.start();
        threadTwo.start();
        // 5. 等待线程执行完毕
        threadOne.join();
        threadTwo.join();

        System.out.println("count 0:"+atomicLong.get());
    }
}
