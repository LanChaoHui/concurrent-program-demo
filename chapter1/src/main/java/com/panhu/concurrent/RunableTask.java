package com.panhu.concurrent;

public class RunableTask implements Runnable {
    public void run() {
        System.out.println("I am a child thread");
    }

    public static void main(String[] args) {
        RunableTask runableTask = new RunableTask();
        new Thread(runableTask).start();
        new Thread(runableTask).start();
    }
}
