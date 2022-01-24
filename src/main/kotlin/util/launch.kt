package util

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.*

/**
 * 自定义的一个协程启动器...
 *
 * 需要一个携程上下文,然后执行代码块是一个suspend ... 关键字修饰的block
 * 然后使用block,开启一个协程处理..
 */
fun launch(context: CoroutineContext = EmptyCoroutineContext, block: suspend () -> Unit) =
    block.startCoroutine(Continuation(context) { result ->
        result.onFailure { exception ->
            val currentThread = Thread.currentThread()
            // 如果出现异常,使用当前线程处理异常..
            currentThread.uncaughtExceptionHandler.uncaughtException(currentThread, exception)
        }
    })
// 这里使用了协程库为我们提供的一个线程池(来执行协程的线程池)
private val executor = Executors.newSingleThreadScheduledExecutor {
    Thread(it, "scheduler").apply { isDaemon = true }
}
// 一个可以暂停的函数..
// 一个是开启协程 // 一个是暂停协程..
suspend fun delay(time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): Unit = suspendCoroutine { cont ->
    // 然后在尝试调度,然后多少秒之后尝试调度..
    executor.schedule({ cont.resume(Unit) }, time, unit)
}