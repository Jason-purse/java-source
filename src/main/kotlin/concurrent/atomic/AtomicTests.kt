package concurrent.atomic

import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.LongAdder

fun main() = runBlocking {

    // atomic 仅仅是一个原子性的机器指令代码执行...
    val atomicInteger = AtomicInteger(0)
    sum(atomicInteger) // 单个原子量并发修改 压力很大,我们可以分成多个Adders 最终sum
    println(atomicInteger.get())
    val adder = LongAdder()
    accumulate(adder)

}

suspend fun accumulate(adder: LongAdder) = coroutineScope {
    val startTime = System.currentTimeMillis();
    // 执行多个累加  最终求和
     coroutineScope {
        repeat(1000) {
            withContext(Dispatchers.Default) { // 使用不同的线程进行累加..
                adder.add(1)
            }
        }
    }

    println(System.currentTimeMillis() - startTime)

}

// 为什么要使用协程scope, 每一个协程拥有一个协程scope,并且scope包含一个协程上下文,在协程中,每一个suspend方法都会依次执行...
suspend fun sum(atomicInteger: AtomicInteger) = coroutineScope {
    val startTime = System.currentTimeMillis();
    coroutineScope {
        for(index in 1 .. 1000) {
            withContext(Dispatchers.Default) { // 改变在修改atomicInteger 时 对同一个原子变量进行修改的耗时操作,对比来说LongAdder 更加快速...
                launch {
                    repeat(3) {
                        atomicInteger.incrementAndGet()
                        if(index == 1000) {
                            println("sum end!")
                        }
                    }
                }
            }
        }
    }

    println(System.currentTimeMillis() - startTime)
}