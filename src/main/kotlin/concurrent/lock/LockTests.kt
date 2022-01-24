package concurrentTests.lockTests

import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

fun main() = kotlinx.coroutines.runBlocking {
    val lock = java.util.concurrent.locks.ReentrantLock()

    launch {
        // 先尝试获取一个锁
        withContext(Dispatchers.Default) {
            lock.lock() //
            println("lock first lock!!!")
            println("first lock count: ${lock.holdCount},current Thread is ${Thread.currentThread().name}")
            // 临界区
            delay(4000)
            lock.unlock()
        }
    }
    delay(500)
    launch {

        withContext(Dispatchers.Unconfined) {
            try {
                if (lock.tryLock(3,TimeUnit.SECONDS)) {
                    kotlin.io.println("acquire two lock success !")
                    println("two lock count: ${lock.holdCount},current Thread is ${Thread.currentThread().name}")

                    lock.unlock()
                }else {
                    println("acquire two lock failure!!!")
                }
            }catch (ex: Exception) {
                // pass
                ex.printStackTrace()
                kotlin.io.println("acquire two lock failure!!!")
            }
        }
    }
    kotlin.Unit
}


// 读锁写锁...
// 读锁 共享读操作,排斥所有写操作...
// 写锁 排斥所有操作..