package concurrent.queue

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.*

class QueueTests {
    private val delayQueue = DelayQueue<Delayed>()
    @OptIn(ObsoleteCoroutinesApi::class)
    @Test
    fun test(): Unit = runBlocking {

        // 同步也可以使用阻塞队列来操作..
        // 当然在多线程竞争激烈的情况下,一定要使用阻塞队列的非阻塞方法...

        // 第一种
//        LinkedBlockingDeque  // 双端阻塞队列.. 容量无上限,可以指定..
//        ArrayBlockingQueue 需要指定容量
//        LinkedBlockingQueue // 阻塞队列  容量无上限,可以指定..
//        PriorityBlockingQueue 优先级队列...
//        DelayQueue  延迟队列   通过getDelay 获取延迟元素的残影.  当负数表示延迟结束,元素已经被移除了.. 每一个元素且必须实现compareTo
        delayQueueTests()

        // TransferQueue (也叫做会面管道... 消费者与生产者约定会面..)

        val transferQueue = LinkedTransferQueue<String>()
        val newSingleThreadContext = newSingleThreadContext("producer")
        var launch = launch {
            withContext(newSingleThreadContext) {
                repeat(3) {
                    transferQueue.transfer(it.toString())
                }
                println("producer stop!")
            }
        }

        delay(1000)
        launch {
            val newSingleThreadContext = newSingleThreadContext("consumer")
            withContext(newSingleThreadContext) {
                while (launch.isActive) {
//                    val take = transferQueue.take() // 有几率发生线程无法关闭
                    val take = transferQueue.poll()
                    take?.let {
                        println("acquire take element: $it")
                    }
                }
            }
            println("consumer stop!!!")
        }
    }
    // 延时队列处理..
    private fun CoroutineScope.delayQueueTests() {
        var currentTimeMillis = System.currentTimeMillis()

        launch {
            repeat(3) {
                delayQueue.add(MyDelayed(currentTimeMillis + (Math.random().toDouble() * 1000).toLong()))
            }
        }

        launch {
            do {
                val element = delayQueue.poll()
                if (element != null) {
                    println("poll element is $element")
                }

            } while (delayQueue.size > 0)
        }
    }
}

class MyDelayed(private val longTime: Long, private val timeUnit: TimeUnit = TimeUnit.MINUTES) : Delayed {
    init {
        println("myDelayed[longTime:$longTime,timeUnit:$timeUnit]")
    }
    override fun compareTo(other: Delayed?): Int {
       if(other != null) {
           val myDelayed = other as MyDelayed
           return (timeUnit.toMillis(longTime) - myDelayed.timeUnit.toMillis(myDelayed.longTime)).toInt()
       }
       return 1
    }

    override fun getDelay(unit: TimeUnit) = System.currentTimeMillis() - timeUnit.toMillis(longTime)

}