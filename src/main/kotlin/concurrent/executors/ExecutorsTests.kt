package concurrent.executors

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.kotlin.utils.mapToIndex
import org.junit.jupiter.api.Test
import java.util.concurrent.*

class ExecutorsTests {
    @Test
    fun executor() = runBlocking {

        val executor = ScheduledThreadPoolExecutor(3)

       launch {
           executor.schedule(
               {
                   println("scheduled!!!!")
               },3,TimeUnit.SECONDS)
               .get() // 阻塞
           println("任务调度结束")
           executor.shutdown() // 等待执行完毕
       }

        println("结束!!!")
    }

    @Test
    fun executorResultAcquire() {

        var newFixedThreadPool = Executors.newFixedThreadPool(2)
        var executorCompletionService = ExecutorCompletionService<String>(newFixedThreadPool)
        var arrayOf = arrayListOf<Callable<String>>(
        Callable { "2" }, Callable { TimeUnit.SECONDS.sleep(2); "3" },Callable {
            TimeUnit.SECONDS.sleep(4)
                "4"
            })
        for (callable in arrayOf) {
            executorCompletionService.submit(callable)
        }

       arrayOf.forEachIndexed {
           _,_ ->
           // 这里会拿到最新执行的一个任务的结果...
           println(executorCompletionService.take().get())
       }

        // 摧毁线程池..
        newFixedThreadPool.shutdown()
    }


    @Test
    fun forkJoinPool() {
        val forkJoinPoolVariable = ForkJoinPool()
        // 还未在计算内的计算中,execute 都可以..
        println(forkJoinPoolVariable.submit(MyForkJoinTask(0, 10000)).join()) // 等待它执行完毕.

        println((0 .. 10000).sum())
    }
}

// 比如这里计算offet = 1000个元素的sum, 那么我们可以指定一个范围,然后算范围值..
class MyForkJoinTask(val from: Int,val to: Int): RecursiveTask<String>() {
    val THRE_SHOLD =  1000
    override fun compute(): String {
        if(from  >  to) {
            return "0";
        }
        // 这里算是计算内的计算..
        if(to - from < THRE_SHOLD) {
            // 直接计算..
            return (from .. to).sum().toString()
        }
        else {
            val i = (from + to) / 2
            return (MyForkJoinTask(from,i).invoke().toInt() + MyForkJoinTask(i + 1,to).invoke().toInt()).toString()
        }
    }
}