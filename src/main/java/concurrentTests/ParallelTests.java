package concurrentTests;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParallelTests {
    @Test
    public void test() {

        int[] ints = {1, 2, 3, 3 * 4, 5, 5 * 6, 7, 7 * 8};
        // 前两个值作为当前值的目标值...
        Arrays.parallelPrefix(ints,(x,y) -> {
            System.out.printf("%s * %s%n", x,y);
            return x * y;
        });
        System.out.println(Arrays.toString(ints));
    }


    @Test
    public void test1() {
        CopyOnWriteArrayList<String> strings = new CopyOnWriteArrayList<>();
        // 对修改的集合进行复制
        //由于迭代器是获取原数组的引用
        // 有可能原数组被丢弃 或者仅仅修改数组中的一些元素
        // 那么迭代器的访问没有同步的必要(不需要消耗同步性能)
        // 因为它的引用本身就具有原子性..
    }
}
