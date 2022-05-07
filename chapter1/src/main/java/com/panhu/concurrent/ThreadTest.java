package com.panhu.concurrent;

public class ThreadTest {
    public static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("I am a child thread");
        }
    }

    public static void main(String[] args) {
        // 创建线程
        MyThread myThread = new MyThread();
        // 启动线程
        myThread.start();
    }
}
