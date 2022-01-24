package concurrent.atomic

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger
public class AtomicCasTests () {
    @Test
    fun test() {
        cas()

        // 比如说我们现在有一个需求,就是所有的请求  只能够开启三个任务,否则无法开启..
        val count = AtomicInteger(0)
        count.updateAndGet(){
                v1 ->
            if(v1 < 3) {
                v1 + 1
                // 这里我们可以开启一个任务,将atomic 在任务结束的时候,乐观锁释放..
                // 这种情况下  如果我们的任务过多,那么多个任务同时访问一个atomic 那必然消耗过大..所以我们可以使用LongAdder 来将累加或者累减的动作分化,提升性能..
            }
            else
              v1
        }
    }
}
// cas 自旋锁,也能保证线程同步...
fun cas() = runBlocking {
    // cas 本质上就是 compare And Set
    // 有些时候  我们需要比较条件  然后修改值...,这个时候我们如果用锁效率较低,竞争激烈的情况下,锁会导致线程阻塞..

    // 此时 如果我们是想要不断更新最正确的值(例如,我们一直想要设置多个线程产生出来的最大值,中间可能会出现很多次更新..)
    val atomicInteger = AtomicInteger(0)
    coroutineScope {
        repeat(3) {
            launch {
                withContext(Dispatchers.Default) {
                    var d = Math.random().toDouble() * 100
                    repeat(3) {
                        d *= 3
                    }
                    println("$it count: max value: $d")
                    cas(atomicInteger,d.toInt())
                }
            }
        }
    }

    println(atomicInteger.get())


    // 那么为什么 这一种不可以呢
    val atomicInteger1 = AtomicInteger(0)
    coroutineScope {
        repeat(3) {
            launch {
                var d = Math.random().toDouble() * 100
                var get = atomicInteger.get() // 后面两步随时可能会断掉...
                atomicInteger.compareAndSet(get, Math.max(get,d.toInt())) // 所以我们需要使用cas  不断循环判断是否相等,然后设置新值.. ,正因如此  可能导致正确的最大值没有设置上去...
            }
        }
    }

}
// 拿到我之前的值比较,最后设置为想要的value
fun cas(atomicInteger: AtomicInteger, value: Int) {
    var max = 0
    var newValue = value;
    do {
        max = atomicInteger.get()
        // 由于基础值是 0 无法修改
        newValue = Math.max(max, value)
    }while (!atomicInteger.compareAndSet(max,newValue))   // 这样的一个整体才算是 原子性的...(cas)
}