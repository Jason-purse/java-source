package concurrent.synchronusor

import org.junit.jupiter.api.Test
import util.delay
import util.launch
import java.util.concurrent.*

class SynchronousTests {
    @Test
    fun sync() {

        // 同步器..


    }

    // 第一个叫做循环璋栏
    @Test
    fun cycleBarrier() {
        var cyclicBarrier = CyclicBarrier(3)
        // 多少个线程到达了公共的一个障栅.. 然后可以指定一个回调处理障栅..
        var newFixedThreadPool = Executors.newFixedThreadPool(3)
        repeat(2) {
          newFixedThreadPool.execute {
              TimeUnit.SECONDS.sleep((Math.random().toDouble() * 10).toLong())
              println("over")
              cyclicBarrier.await()
          }
       }
        cyclicBarrier.await()
        println("线程执行完毕!")
        newFixedThreadPool.shutdown()

        // 当超时等待失败时, 只要有一个线程离开了璋栏点,所有的await 都会抛出异常.. BrokenBarrierException 方法将会立即终止调用...
    }

    @Test
    fun phaser() {
        //  阶段器..
        var phaser = Phaser(3)
        phaser.parent
    }

    @Test
    fun exchanger() {
        var phaser = Phaser(2)
        var exchanger = Exchanger<Any>()
        Thread {
            phaser.register()
            var exchange = exchanger.exchange(3)
            println(exchange)
            launch {
                delay(3000)
                phaser.arrive()
            }
        }.start()
        Thread {
            var exchange = exchanger.exchange(4)
            println(exchange)
            phaser.arrive()
        }.start()
        phaser.arriveAndAwaitAdvance()
        println("阶段完成了!@!!!!!!!")
    }

    @Test
    fun phaserTest() {
        fun startTasks( tasks: List<Runnable>, iterations: Int) {
            val phaser = object : Phaser() {
                override fun onAdvance(phase: Int, registeredParties: Int): Boolean {
                    // 阶段值 >= 迭代器的值 -1 或者注册的部分为0
//                    println("$phase,$registeredParties,condition: ${phase >= iterations - 1}")
                    return phase >= iterations - 1 || registeredParties == 0;
                }
            };

            phaser.register();
            for (task in tasks) {
            phaser.register();
            Thread {
                do {
                    task.run();
                    phaser.arriveAndAwaitAdvance();
                } while (!phaser.isTerminated());
            }.start();
        }
            // allow threads to proceed; don't wait for them
            phaser.arriveAndDeregister();
        }

        startTasks(arrayListOf(Runnable { println("runnable") }, Runnable { println("runnable1") }),2)
    }
}