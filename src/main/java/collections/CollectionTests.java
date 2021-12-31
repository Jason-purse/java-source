package collections;

import java.util.*;
import java.util.stream.Stream;

public class CollectionTests {
    public static void main(String[] args) {
        List<Integer> generate = generate(1, 2, 3, 4);
        List<Integer> generate1 = generate(4, 5, 6, 7, 8);
        // 交集
        generate1.retainAll(generate);
        System.out.println(generate1);

        // 可以删除迭代器的元素
        //但是必须越过这个元素,否则无法删除..
        // iterator
        // void remove( )
        //删除上次访问的对象。这个方法必须紧跟在访问一个元素之后执行。如果上次访问之
        //后，集合已经发生了变化， 这个方法将抛出一个 IllegalStateException。


        // LIST-ITERATOR
        LinkedList<Integer> integers = new LinkedList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        integers.add(5);
        ListIterator<Integer> integerListIterator = integers.listIterator();
        Integer next = integerListIterator.next();
        integerListIterator.remove();
        integerListIterator.next();
        //
        System.out.println(integerListIterator.previous());

        // 针对链表来说,它就是一个有序集合
        // 所以对于这种对于修改方便的数据结构来说,使用迭代器增加或者删减元素效率更高..,它不需要移动元素.

        //treeSet 红黑树
        // hastSet 哈希表
        // priorityQueue 堆排序

    }

    @SafeVarargs
    public static <T> List<T> generate(T ...args) {
        return new ArrayList<>(Arrays.asList(args));
    }
}

enum s {

}
