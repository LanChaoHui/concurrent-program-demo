package com.panhu.concurrent;

public class ReadThread extends Thread {
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (ready) {
                System.out.println(num + num);
            }
            System.out.println("read thread....");
        }
    }

    public static class Writethread extends Thread {
        @Override
        public void run() {
            num = 3;
            ready = true;
            System.out.println("writeThread set over...");
        }
    }

    private volatile static int num = 0;
    private volatile static boolean ready = false;

    public static void main(String[] args) {
        try {
            ReadThread readThread=new ReadThread();
            readThread.start();

            Writethread writethread=new Writethread();
            writethread.start();

            Thread.sleep(10);
            readThread.interrupt();
            System.out.println("main exit");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
