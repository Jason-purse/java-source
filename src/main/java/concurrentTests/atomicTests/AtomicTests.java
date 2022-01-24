package concurrentTests.atomicTests;


import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicTests {
    public static void main(String[] args) {
        int prev = 3;
        System.out.println(prev == (prev = 2)); // 这个语句 为什么不相等..
        // 因为在java 编译中,这是两个指令,且是两个对应的操作数..
        // 那么prev 的值进入一个操作数 , 后续表达式 的结果作为操作符进入第二个操作符..
        // 所以它们不相等..

        // 还有一些其他的原子类..
        //AtomicInteger、AtomicIntegerArray、AtomicIntegerFieldUpdater、AtomicLongArray、
        //AtomicLongFieldUpdater、AtomicReference、AtomicReferenceArray 和 AtomicReference / FieldUpdater

        AtomicIntegerFieldUpdater<A> fieldUpdater = AtomicIntegerFieldUpdater.newUpdater(A.class,"value");
    }
}

class A {
    protected volatile int value;
}
