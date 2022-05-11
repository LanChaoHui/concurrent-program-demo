package com.panhu.concurrent;

public class TestForContent {
    static final int LINE_NUM = 1024;
    static final int COL_UMN = 1024;

    public static void main(String[] args) {
        long[][] array = new long[LINE_NUM][COL_UMN];
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < LINE_NUM; i++) {
            for (int j = 0; j < COL_UMN; j++) {
                array[i][j] = i * 2 + j;
            }
        }
        long endTime = System.currentTimeMillis();
        long cacheTime = endTime - startTime;
        System.out.println("cache time:" + cacheTime);//4
    }
}
