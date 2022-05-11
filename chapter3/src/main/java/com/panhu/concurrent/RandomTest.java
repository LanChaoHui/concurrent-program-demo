package com.panhu.concurrent;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class RandomTest {
    public static void main(String[] args) {
        // 1. 创建一个默认种子的随机随机数生成器
        Random random=new Random();
        // 2. 输出10个在0-5(包含0，不包含5) 之间的随机数
        for (int i = 0; i < 10; i++) {
            System.out.println(random.nextInt(5));
        }
    }
}
