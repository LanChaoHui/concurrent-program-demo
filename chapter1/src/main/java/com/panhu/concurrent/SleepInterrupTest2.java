package com.panhu.concurrent;

public class SleepInterrupTest2 {
    public static void main(String[] args) throws InterruptedException {
        Thread threadOne = new Thread(new Runnable() {
            @Override
            public void run() {
                // interrupted 检测当前线程是否被中断，如果是返回true，否者返回false ，同时清除中断暖标志
                while (!Thread.currentThread().interrupted()) {

                }
                // isInterrupted 当前的线程是否被中断，true 是中断 false 没有中断
                System.out.println("threadOne is Interrupted:" + Thread.currentThread().isInterrupted());
            }
        });
        // 启动线程
        threadOne.start();

        // 设置中断标志
        threadOne.interrupt();

        threadOne.join();

        System.out.println("main thread is over");
    }
}
