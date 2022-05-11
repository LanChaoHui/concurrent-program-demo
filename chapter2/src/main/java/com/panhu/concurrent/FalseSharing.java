package com.panhu.concurrent;

/**
 * 1. 伪共享
 * CPU 缓存系统中是以缓存行(cache line) 为单位存储的。目前主流的CPU Cache 的 Cache Line 大小都是64 Bytes。 在多线程情况下,
 * 如果需要修改“共同一个缓存行的变量”，就会无形影响彼此的新能，这就是伪共享(False Sharing)
 * <p>
 * 2. 缓存行
 * 由于共享变量在CPU缓存中的存储是以行为单位，一个缓存行可以存储多个变量（存满当前缓存行的字节数）；而CPU对缓存的修改又是已缓存行为最小
 * 单位的，那么就会出现上诉的伪共享问题。
 * <p>
 * Cache Line 可以简单理解为CPU Cache中最小缓存单位，今天CPU不再是按字节方位内存，而是已64字节为单位块(chunk)拿取，称为一个缓存
 * 行(cache line) 当你读一个特定的内存地址，整个缓存将从主内存换入缓存，并且访问一个缓存行内的其他值的开销是很小的。
 * <p>
 * 3. CPU 的三级缓存
 * 由于CPU的速度远远大于内存速度，所以CPU设计者就给CPU加上缓存(CPU Cache) 。以免运算被内存速度拖累。（就像我们写代码把共享数据做Cache
 * 不想被DB存取速度拖累一样），CPU Cache 分为三个级别:L1,L2,L3.越靠近CPU的缓存越快也越小。所以L1缓存很小但是很快，并且紧靠CPU内核。L2大一些
 * ，也慢一些，并且仍朗只能被一个单独的CPU和使用。L3在现代多核机器中更普遍，仍然更大，更慢，并且被单个查查上的所有CPU核更新，最后你拥有一块主内存
 * ，由全部插槽上的所有CPU核共享。
 * <p>
 * 当CPU 执行运算的时候，它先去L1查找所需的数据，再去L2，然后是L3，最后如果这些内存都没有，所需的数据就要去主内存拿，走得越远，运算耗费的时间
 * 就越长。所以如果你在做一些繁琐的事情，你要确保数据在L1缓存中。
 * <p>
 * 4. 目前常用的缓存设计是N路组关联(N-Way Set Associative Cache),他的原理是把一个缓存按照N个Cache Line 作为一组(Set) ，缓存按组
 * 划分为等分，每个内存块能够被映射到对应的set中的任意缓存行中。比如一个16路缓存，16个Cache line 作为一个Set，每个内存块能够被映射到相对应
 * 的Set中的16个Cache line 中的任意一个。一般地，具有相同低bit位地址的内存块共享同一个Set。
 * <p>
 * 5. 多核CPU都有自己的专有缓存(一般为L1,L2),以及同一个CPU插槽之间的核共享(一般为L3)。不同核心的CPU缓存中难免会加载同样的数据，那么如何保证数据的
 * 一致性呢，就是MESI协议了。
 * <p>
 * M(Modified)：这行数据有效，数据被修改了，和内存中的数据不一致，数据只存在于本Cache中；
 * E(Exclusive)：这行数据有效，和内存中的数据一致，数据只存在于本Cache中；
 * S(Shared)：这行数据有效，数据和内存中的数据一致，数据存在于很多Cache中；
 * I(Invalid)：这行数据无效；
 * <p>
 * 那么，假设一个变量i=3（应该是包含变量i的缓存块，块大小为缓存行大小）；已经加载到多核（a,b,c）的缓存中，此时该缓存行的状态为S；此时
 * 其中的一个核a改变量i的值，那么核a中中的当前缓存行的状态将变为M，b,c核中的当前缓存行状态改变为I。
 * <p>
 * 6. 解决原理
 * <p>
 * 为了避免由于false sharing 导致 CacheLine 从L1,L2,L3到主内存重复载入，我们可以使用数据填充的方式来避免，即单个数据添加满一个
 * CacheLine，这本质就是一种空间换时间的做法。
 * JAVA8中的解决方案
 * Java8已经提供了官方的解决方案，Java8中新增一个注解：@sun.misc.Contended。加上这个注解的类会自动补齐缓存行，需要主注意的是该注解默认
 * 是无效的，需要在jvm启动时设置-XX:-RestrictContended才会生效。
 * 填充默认宽度为128，要自定义宽度则可以设置XX Con nd dPaddingWidth 参数。
 */
@sun.misc.Contended
public class FalseSharing {
    public volatile long value = 0L;
    public long p1, p2, p3, p4, p5, p6;
    /**
     * 这里注解用来修饰类，当然也可以修饰变零 trl:ThreadLocalRandom
     */
    @sun.misc.Contended("trl")
    long threadLocalRandomSeed;
}
