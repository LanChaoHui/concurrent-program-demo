package com.panhu.concurrent;

public class WaitNotifyInterupt {
    static Object object = new Object();

    public static void main(String[] args) throws InterruptedException {
        // 创建线程
        Thread threadA=new Thread(new Runnable() {
            public void run() {
                try {
                    System.out.println("---begin---");
                    // 阻塞当前线程
                    synchronized (object){
                        object.wait();
                    }
                    System.out.println("---end---");
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        threadA.start();
        Thread.sleep(1000);
        System.out.println("--- begin interrupt threadA ---");
        threadA.interrupt();
        System.out.println("--- end interrupt threadA ---");
    }
}
