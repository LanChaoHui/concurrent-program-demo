package com.panhu.concurrent;

public class DaemonThreadTest {
    public static void main(String[] args) {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;){}
            }
        });
        // 设置为守护线程
        thread.setDaemon(true);
        // 启动子线程
        thread.start();
        System.out.println("main thread is over");
    }
}
