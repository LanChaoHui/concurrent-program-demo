package com.panhu.concurrent;

public class WaitNotifyTest {
    private static volatile Object resourceA = new Object();
    public static volatile Object resourceB = new Object();

    public static void main(String[] args) throws InterruptedException {
        // 创建线程
        Thread threadA=new Thread(new Runnable() {

            public void run() {
                try {
                    // 获取resourceA共享资源资源的监视锁
                    synchronized (resourceA) {
                        System.out.println("threadA get resourceA lock");
                        // 获取resource共享资源监视器锁
                        synchronized (resourceB) {
                            System.out.println("threadB get resourceB lock");
                            // 线程A阻塞并释放获取到的resourceA的锁
                            System.out.println("threadA release resourceA lock");
                            resourceA.wait();
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // 创建线程
        Thread threadB=new Thread(new Runnable() {
            public void run() {
                try {
                    // 休眠1s
                    Thread.sleep(1000);
                    // 获取resource共享资源监视器
                    synchronized (resourceA){
                        System.out.println("threadB get resourceA lock");
                        System.out.println("threadB try get resourceB lock...");
                        synchronized (resourceB){
                            System.out.println("threadB get resourceB lock");
                            // 线程B阻塞，并释放获取到resourceA的锁
                            System.out.println("threadB release resourceA lock");
                            resourceA.wait();
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // 启动线程
        threadA.start();
        threadB.start();
        // 等待两个线程结束
        threadA.join();
        threadB.join();

        System.out.println("main over");
    }
}
