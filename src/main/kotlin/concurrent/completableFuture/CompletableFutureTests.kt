package concurrent.completableFuture

import kotlinx.coroutines.delay
import org.junit.jupiter.api.Test
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.function.BiFunction

class CompletableFutureTests {
    @Test
    fun test() {

        // test
        CompletableFuture.runAsync {
            TimeUnit.SECONDS.sleep(1)
            println("async")
        }
            .thenCombineAsync(
                CompletableFuture.runAsync {
                    TimeUnit.SECONDS.sleep(2)
                    println("第二个任务")
                },
                BiFunction {
                        t: Void?, u: Void? ->
                    println("over")
                    "success"
                }
            )
            .get() // 阻塞
    }
}