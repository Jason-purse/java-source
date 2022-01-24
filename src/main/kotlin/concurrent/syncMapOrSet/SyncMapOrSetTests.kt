package concurrent.syncMapOrSet

import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class SyncMapOrSetTests {
    @Test
    fun test() {
        // 使用并发线程安全的集合进行数据操作...
        val map  = ConcurrentHashMap<String,String>()

        map.mappingCount() // 这是java 8 提供的一个方法能够返回大容量的count数量..
        // 并发集合的size 通常不可靠,因为需要使用迭代来确定它们的集合数量..

        // 比较宽容的是 弱一致性迭代(weakly consist)
        // 意味着迭代器不一定能够反应构造之后的修改,但是每一种状态只会出现一次. 不会抛出并发修改异常
        // 反之java.util中的迭代器是强一致性迭代器,会抛出并发修改异常...


        // 线程安全的映射批操作..
        // 需要指定阈值.. 表示有多少个(尽可能多个线程)参与计算
        // 其次 并发集合  都将null 作为不存在元素的依据..
        // 映射的视图,可能直接更新到映射上...
        // java 8 还提供了新的keySet 用于在没有key的时候，向映射上添加key所更新的默认值..

        // 映射上可以有三种操作
        // 查询 / 归并 /  forEach..
        // 这些都存在对应的原子操作..
        // 也可以自己写cas操作.. 当然这是不必要的...
    }

@Test
    fun writeArrayCopy() {
        var copyOnWriteArrayList = CopyOnWriteArrayList<Any>()
        // 保证线程安全
        // 主要是通过写线程复制底层的数组,读线程比写线程多的时候,保证弱一致性..
        // 每构建一个迭代器都包含一个指向原来数组的引用,这样当修改了新的元素时,
        // 迭代器仅持有旧的视图的数组信息,本就存在一致的数据(和旧的相比),无需同步开销..


        // Arrays 中的一些并行算法
        var arrayOf = arrayOf(1, 2, 3, 4)
        Arrays.parallelPrefix(arrayOf,Int::times)
        println(arrayOf.contentToString())

        var arrayOf1 = arrayOf(1,2,3,3 * 4,5,5*6,7,7*8)
        Arrays.parallelPrefix(arrayOf1,Int::times)
    println(arrayOf1.contentToString())
    }
}