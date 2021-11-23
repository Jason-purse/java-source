package blocking;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class BlockQueueTests {
    @Test
    public void test() throws InterruptedException {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(2000);
        new Thread(() -> {
            for (int i = 0; i< 100; i ++) {
                try {
                    queue.put(String.valueOf(i));
                }catch (InterruptedException e) {
                    // pass
                    // come on
                }catch (NullPointerException e) {
                    // come on
                }
            }
        }).start();
        Object target = this;
        new Thread(() -> {
            for(int i = 0; i< 100; i ++) {
                try {
                    String take = queue.take();
                    System.out.printf("consume data: %s%n", take);
                }catch (InterruptedException e) {
                    // come on
                }
            }

            synchronized (target) {
                target.notify();
            }
        }).start();
        synchronized (this) {
            this.wait();
        }
    }


    @Test
    public void test2() {
        // prority...queue
        // 优先级队列..
//        PriorityBlockingQueue...
    }

    @Test
    public void test3() {
        /*DelayQueue<ScheduledFuture<String>> queue = new DelayQueue<ScheduledFuture<String>>();*/
        // javaSe 7 引入了Transfer 接口,能够阻塞生产者直到消费者能够接受一个元素才取消阻塞...

        LinkedTransferQueue<String> queue = new LinkedTransferQueue<>();
        new Thread(() -> {
           try {
               queue.transfer("123");
           }catch (InterruptedException e) {
               // pass
               // come on
           }
        }).start();
        Object target = this;
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                String take = queue.take();
                System.out.println(String.format("take data: %s", take));
                synchronized (target) {
                    target.notify();
                }
            }catch (InterruptedException e) {
                // pass
                // come on
            }
        }).start();

        synchronized (this) {
            try {
                this.wait();
            }catch (InterruptedException e) {
                // pass come on
                System.out.println("等待被打断,结束!");
            }
        }
    }
}
