package com.panhu.concurrent;

import java.util.concurrent.locks.LockSupport;

/**
 * jdk中的rt.jar 包里面的LockSupper是个工具类，它的主要作用是挂起和唤醒线程，
 * 该工具类是创建和其他同步类的基础
 * LockSupport类与每个使用它的线程都会关联一个许可证，在默认情况下调用LockSupport是使用Unsafe类实现的，
 */
public class LockSupportTest {
    /**
     * void park() 方法
     * 如果调用park方法线程已经拿到了与LockSupport关联的许可证，则调用LockSupport.park()时会马上返回
     * ，否则调用线程会被禁止参与线程的调度，也就是会被阻止阻塞挂起。
     * <p>
     * 如下代码直接在main函数里面调用part方法最终只会输出 begin park！，然后当前线程被挂起，这是因为
     * 在默认情况下调用线程是不持有许可证的。
     * <p>
     * 在其他线程调用unpark(Thread thread) 方法并且将当前线程作为参数时，调用park方法而被阻塞的线程
     * 会返回，另外如果其他线程调用了阻塞线线程的，interrrupt() 方法，设置了中断标志或者被虚假唤醒，则
     * 阻塞线程也会返回。所以在调用park方法时最好也使用循环条件判断方式。
     * <p>
     * 需要注意的是，因调用park()方法而被阻塞的线程被其他线程中断而返回时并不会抛异常，InterruptedExceltion异常
     * 。
     */
//    public static void main(String[] args) {
//        System.out.println("begin park!");
//        LockSupport.park();
//        System.out.println("end park");
//    }

    /**
     * 当线程调用unpark时，如果参数thread线程没有持有thread与LockSupport类关联的许可
     * ，则让thread线程持有。如果thread之前因调用park()而被挂起，则调用unpark后，该线程
     * 会被唤醒。如果thread之前没有调用park而被挂起，则调用unpark后，该线程会被唤起，如果
     * thread之前没有调用park，则调用unpark方法后，再调用park方法，其会立刻返回。
     */
//    public static void main(String[] args) {
//        System.out.println("begin park！");
//        // 使当前线程获取到许可证
//        LockSupport.unpark(Thread.currentThread());
//        // 再次调用park方法
//        LockSupport.park();
//        System.out.println("end park！");
//    }
//    public static void main(String[] args) throws InterruptedException {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("child thread begin park！");
//                // 调用park方法挂起自己
//                LockSupport.park();
//                System.out.println("child thread unpark!");
//            }
//        });
//        // 启动子线程
//        thread.start();
//        Thread.sleep(1000);
//        // 主线程休眠1s
//        System.out.println("main thread begin unpark!");
//        // 调用unpark方法让thread 线程持有徐可能，然后park方法返回
//        LockSupport.unpark(thread);
//        // 上面代码首先创建一个子线程thread，子线程启动后调用park方法，由于在默认情况下子线程没有持有许可证
//        // 因而他会把自己挂起。
//        // 主线程休眠1s是为了让主线程调用unpark方法前让子线程输出child thread begin park!
//        // 主线程然后执行unpark方法，参数为子线程，这样做的目的是让子线程持有许可证，然后子线程调用park方法就返回了
//        //
//    }

    /**
     * park 方法返回时不会告诉你何种原因返回，所以调用者需要根据调用park方法的原因，再次检查条件是否是否满足，如果不
     * 满足还需要再次调整park方法。
     * 例如，根据调用前后中断状态对比就可以判断是不是因为被中断才返回的。
     * 为了说明调用park方法后的线程被中断后会返回
     */
//    public static void main(String[] args) throws InterruptedException {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("child thread begin park!");
//                while (!Thread.currentThread().isInterrupted()) {
//                    LockSupport.park();
//                }
//                System.out.println("child thread unpark!");
//            }
//        });
//        // 启动线程
//        thread.start();
//        // 主线程休眠1s
//        Thread.sleep(1000);
//
//        System.out.println("main thread begin unpark!");
//        thread.interrupt();
//        // 在如上代码中，只有中断子线程，子线程才会运行结束，如果子线程不被中断，及时你调用unpark(thread)方法子线程也不会结束
//    }

    /**
     * park 方法还支持带blocker 参数的方法void park(Object blocker)方法，当线程在没有持有许可的情况下，调用
     * park方法而被阻塞挂起时，这个blocker对象会被记录到该线程的内存。
     * <p>
     * 使用诊断工具可以观察线程被阻塞的原因，诊断工具是通过调用，getBlocker(Thread)方法来获取blocker对象，所以JDK
     * 推荐我们使用带blocker参数的park方法，并且blocker被设置为this，这样当在打印线程堆栈排查问题时就能知道是哪个类被阻塞
     */
    public static void main(String[] args) throws InterruptedException {
       TestPark testPark=new TestPark();
       testPark.testPark();
    }
}
