package com.panhu.concurrent;


import sun.misc.Unsafe;
import sun.misc.VM;
import sun.reflect.Reflection;

import java.lang.reflect.Field;

/**
 * JDK的rt.jar包中的Unsafe 类提供了硬件级别的原子性操作，Unsafe类中的方法都是native
 * 方法，它们使用JNI的方式访问本地C++实现库。Unsafe 提供的几个主要方法。
 * <p>
 * long objectField(Field field) 方法：返回指定的变量在所属类中的内存偏移量地址。
 * int arrayBaseOffset(Class arrayClass) 方法：获取数组中第一个元素的地址。
 * int arrayIndexScale(Class arrayClass) 方法：获取数组中一个元素占用的字节。
 * boolean compareAndSwapLong(Object obj,long offset,long expect，long update) 方法：
 * 比较对象obj中偏移量为offset的变量是否与expect相等，相等则使用update值更新，然后返回值true，否者返回false
 * public native long getLongvolatile(Object obj,long offset) 方法：获取对象obj 对象中
 * offset偏移量为offset的变量对应volatile语义的值。
 * void putLongvolatile(Object obj,long offset,long value)方法：设置obj对象中offset偏移类型为 long field的值为value
 * void putOrderedLong(Object obj,long offset,long value) 方法：设置对象中offset偏移地址对应long型field的值为value。这是一个有延迟的putLongvolatile方法
 * 并且不保证值修改对其他线程立刻可见。只有在变量使用volatile修饰并且预计会被以为修改时才使用该方法。
 * void park(boolean isAbsolute,long time) 方法：阻塞当前线程，其中参数isAbsolute等于false且等于0表示一直阻。time大于0表示等待指定的time后阻塞线程会唤醒，这里time是个绝对时间，是将
 * 某个时间点换算为ms后的值。另外，当其他线程调用了当前阻塞线程的interrupt方法而中断了当前线程时，当前线程也会返回，而当其他线程调用了，unPark方法并且当前线程作为参数事当前线程也会返回。
 * void unpark(Object thread) 方法：唤醒park阻塞的线程
 * long getAndSetLong(Object obj,long offset,long update) 方法：获取对象obj中偏移量为offset变量volatile语义的当前值，并设置变量volatile语义为update。
 * long getAndAddLong(Object obj,long offset,long addValue)方法：获取对象obj偏移量为offset的变量volatile语义的当前值，并设置变量值为原始值+addValue
 */
public class TestUnSafe {
    // 获取Unsafe 的实例
    /**
     * public static Unsafe getUnsafe() {
     * 为什么会有这个判断？
     * 我们知道Unsafe类是rt.jar包提供的，rt.jar包里面的类是使用Bootstrap类加载器加载的，
     * 而我们的启动main函数所在的类是使用AppClassLoader加载，所以在main函数里面加载Unsafe类时，根据委托机制，会委托
     * 给Bootstrap去加载Unsafe类
     * Class var0 = Reflection.getCallerClass();
     * if (!VM.isSystemDomainLoader(var0.getClassLoader())) {
     * throw new SecurityException("Unsafe");
     * } else {
     * return theUnsafe;
     * }
     * }
     */
    static Unsafe unsafe;

    // 记录变量state在类TestUnSafe中的偏移值
    static long stateOffset = 0;

    // 变量
    private volatile long state = 0;

    static {
        try {
            // 获取state变量在类TestUnSafe中偏移量
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            // 设置为可存取
            field.setAccessible(true);
            // 获取该变量的值
            unsafe = (Unsafe) field.get(null);
            // 获取state在TestUnSafe中的偏移量
            stateOffset = unsafe.objectFieldOffset(TestUnSafe.class.getDeclaredField("state"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        // 创建实例，并且设置state值为1
        TestUnSafe testUnSafe = new TestUnSafe();
        // 创建实例，并设置state值为1
        boolean isSuccess = unsafe.compareAndSwapInt(testUnSafe, stateOffset, 0, 1);
        System.out.println(isSuccess);
    }

}
