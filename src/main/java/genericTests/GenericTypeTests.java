package genericTests;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author FLJ
 * @date 2021/12/1
 * @time 20:45
 * @description 泛型复习
 */
public class GenericTypeTests {

    // java 之所以开发泛型,就是为了解决模板类型的需求
    // 所以引入了类型参数 -> 形式例如 <T>   List<T>
    // 那么类型变量是:   <T> 所对应的 T 变量;

    @Test
    public void test() throws NoSuchFieldException {
        Class<Clazz> clazzClass = Clazz.class;
        // 获取类型参数
        TypeVariable<Class<Clazz>>[] typeParameters = clazzClass.getTypeParameters();
        for (TypeVariable<Class<Clazz>> typeParameter : typeParameters) {
            System.out.println(typeParameter);
        }
        Field a = clazzClass.getDeclaredField("a");
        Type genericType = a.getGenericType();
        System.out.println(genericType);

        // genericType is TypeParameter
        if (genericType instanceof TypeVariable) {
            System.out.println("genericType is TypeVariable!");
        }

        // 编译器进行泛型推断,尝试获取所有元素的超类型,如果超类型有多个且歧义,那么会尝试抛出警告;
        // 这里并没有尝试抛出警告,因为它的超类型都可以是 Number
        Number middle = Array.getMiddle(1, 20.00, 3.14);

        // 这种也不会抛出警告,可以序列化类型
        Serializable middle1 = Array.getMiddle("1", 1, 1.24);

        System.out.println(middle1);

        Serializable middle2 = Array.getMiddle("1", Clazz.class, 2.34);

        System.out.println(middle2);

    }

    /**
     * 类型擦除
     */
    @Test
    public void erasedType() {
        STst sTst = new STst();
        Tst sTst1 = sTst;
        // 语法没有错误,但是java jvm层面进行类型强转(因为发生了类型擦除,所以导致子类使用类型限定进行类型擦除,强转失败,无法设置);
        sTst1.setValue(123);
        System.out.println(sTst1.getValue());

        // 强制类型转换,并不知道它到底属不属于这一类型(因为比较仅仅只是比较raw type)
        // 所以确切类型转换可能会出现Cast exception;
        Tst sTst11 = (Tst<String>) sTst1;

        // 所以可知,所有的泛型类擦除之后都同属一个raw type
        // class 也就相等;

//        Tst<String>.class == Tst<Integer>.class
        // 不能创建参数化类型数组
        // 由于编译器有足够的信息推断类型,于是擦除之后,a 类型为Tst[]
        // 但是可以赋值给Object[] ...
        // 然后设置其他的值,会报错,但是如果是泛型对象赋值 会导致这种检查无效,因为它属于这种raw type => Tst...
        // 所以无法创建泛型类型数组;
        // 并且就算通过类型检查,也会报错,例如
        // Object[] aa = a;
        // aa[0] = new Tst<Integer>()
        // 那么Tst<Integer>也是叛徒... 也会存在类型错误
        // 这里跟java的协变什么概念有关....
//        Tst<String>[] a = new Tst<String>[10];

//        可以声明通配类型的数组， 然后进行类型转换：
//        Pair<String>[] table = (Pair<String>[]) new Pair<?>[10]
        // 但是还是那句话, 看似强转了,但实则有警告,例如调用String的方法则会出现ClassCastException;
        //  因为获取的类型为String,你给了一个Employee 有问题...

    }

// 这里说明了泛型欺骗编译器的手段!
    @Test
    public void test3() {
        AA<String> stringAA = new AA<>();
        stringAA.setFirstProperty("123");


        String[] aa = stringAA.getAa();

        Integer[] integers = asArray(1, 2, 3, 4);

        throw new RuntimeException("1231");
    }

    @Test
    public void test5() {
        List<? super Object> list = new ArrayList<>();
        // 下界代表了它能够装什么...
        list.add("1231");

        list.add(1);
        list.add(5);
        System.out.println(list);

        String[] a = {"1","2","3"};

        System.out.println(((Object[]) a));
        System.out.println((CharSequence[])a);

        // 泛型也决定了它是否能够向上转型
        ArrayList<String> arrayList = new ArrayList<>();

        List<String> listed = arrayList;

        List<? extends String> ed = arrayList;

        // 这种情况就是 list 并不明白里面装的是什么,所以无法加入Comparable..以及子类...
        // ? 无法判断
        List<? extends Comparable<? extends A>> ss = new ArrayList<>();
//        ss.add(new A());

    }

    /**
     * 虽然根据泛型擦除的原因,我们可以绕过编译器类型检查,但是运行时类型强转必然存在问题！
     * @param a
     * @param <T>
     * @return
     */
    public static <T> T[] asArray(T ...a){
        return (T[])new Object[2];
    }

    /**
     * 但是这种如果在实际获取E[]的时候,依旧会出现类型转换异常
     * @param <E>
     */
    class AA<E> {
        private E[] aa;

        public AA() {
            aa = (E[]) new Object[2];
        }

        public void setFirstProperty(E e) {
            aa[0] = e;
        }
        public E[] getAa() {
            return aa;
        }
    }


    @Test
    public void test4() {

        AA<CharSequence> a = new AA<>();
        a.setFirstProperty("123");
        AA<? extends CharSequence> aa = new AA<>();
        // ? 无法进行匹配,无法确定它是什么类型,仅仅知道它可能是CharSequence的一个子类型
//        aa.setFirstProperty("123");
        CharSequence[] aa1 = aa.getAa();
    }

    public void body() throws Exception {
        Scanner in = new Scanner(new File("ququx") , "UTF-8");
        while (in.hasNext())
            System.out.println(in.next());
    }

    public Thread toThread() {
        return new Thread() {
            public void run() {
                try {
                    body();
                } catch (Throwable t) {
                    GenericTypeTests.<RuntimeException>throwAs(t);
                }
            }
        };
    }
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> void throwAs(Throwable e) throws T
    {
        throw (T) e;
    }


}

// 类型擦除和raw type(原始类型)之间的关系
// 每一个泛型类都存在一个原始类型,它就是一个基本的简单的java class,如果类型参数(变量)没有进行限定,最后都erased之后使用Object替换
// 否则使用限定类型进行设定!
//就这点而言， Java 泛型与 C++ 模板有很大的区别。C++ 中每个模板的实例化
//产生不同的类型，这一现象称为“模板代码膨账”。Java 不存在这个问题的困扰

// 原始类型用第一个限定的类型变量来替换， 如果没有给定限定就用 Object 替换

/**
 * <pre>
 *     public class Interval <T extends Comparable & Serializable〉implements Serializable
 * {
 * private T lower;
 * private T upper;
 * public Interval (T first, T second)
 * {
 * if (first.compareTo(second) <= 0) { lower = first; upper = second; }
 * else { lower = second; upper = first; }
 * }
 * }
 * </pre>
 *
 * 擦除之后
 * <pre>
 * public class Interval implements Serializable
 * {
 * private Comparable lower;
 * private Coiparable upper;
 * public Interval (Comparable first, Comparable second) { . . . }
 * }
 * </pre>
 */

// 因为这个原因所以我们需要注意:
// 切换限定： class Interval<T extends Serializable & Comparable>
//会发生什么。如果这样做， 原始类型用 Serializable 替换 T, 而编译器在必要时要向
//Comparable 插入强制类型转换。为了提高效率，应该将标签（tagging) 接口（即没有方
//法的接口）放在边界列表的末尾

@Data
class Clazz<A> {
    private A a;
}


class Array<T> {
    public static <T> T  getMiddle(T ...a) {
        return a[a.length / 2];
    }
}
class ArrayAIg
{
    // 由于T可以是任何类型,无法判断它是否拥有可信任的CompareTo,所以我们可以给它设置类型限定符
    // 这种行为叫做类型限定;
    // 并且一个类型的类型限定可以是多个
//    在 Java 的继承中， 可以根据需要拥有多个接口超类型， 但限定中至多有一个类。如果用
//    一个类作为限定，它必须是限定列表中的第一个
        public static <T extends Comparable<T> & Serializable> T iin(T[] a) // almost correct
        {
            if (a == null || a.length == 0) return null ;
            T smallest = a[0];
            for (int i = 1; i < a.length; i ++)
                if (smallest.compareTo(a[i]) > 0) smallest = a[i];
            return smallest;
        }
}


class Tst<T> {
    private T value;
    public void setValue(T value) {
        this.value = value;
    }

    // 当程序调用泛型方法时，如果擦除返回类型， 编译器插入强制类型转换(这一种叫做 翻译泛型表达式)
    // 其实还有另一种情况 {@ code STst 泛型继承也会出现这样的问题} - 叫做泛型方法擦除! - 这种限制来源于 要多态;
    // 泛型擦除之后,STst.setValue(String) 和 STst.setValue(Object)不是相同的类型的方法这存在问题,于是java 解决这种冲突,引入了桥接方法;
    // 为了满足泛型擦除之后,没有冲突;
    // 例如 STst实例赋值给超类,Tst<String> = instance
    // 但是此类仅有一个擦除方法set.(Object),但是实现多态只能够使用桥接方法强转对象并调用STst的set...(String)方法;
    // 否则类型不一致...

    // 桥方法不仅仅应用在泛型方法擦除
    // 方法覆写可以指定一个类型更加严格的类型,这个时候子类在编译阶段会自动生成一个桥方法,调用对应的严格类型方法...
    public T getValue() {
        return value;
    }
}

class STst extends Tst<String> {
    @Override
    public void setValue(String value) {
        super.setValue(value);
    }

    @Override
    public String getValue() {
        return super.getValue();
    }
}

// 泛型的约束和局限性
// 由于泛型擦除之后是Object类型,所以不存在基本类型限定..
// instanceof 仅仅只适合比较raw type(原始类型),相反泛型类型无法比较..

//注意擦除后的冲突


// 这里存在一个规则
// 要想支持擦除的转换， 就需要强行限制一个类或类型变量不能同时成为两个接口类型的子类，
// 而这两个接口是同一接口的不同参数化。
//class Pair<T> {
//    private T first;
//    private T second;

// 这里存在同名方法... 冲突;
//    public boolean equals(T value) {
//        return first.equals(value) && second.equals(value);
//    }
//}

class Employee implements Comparable<String> {
    @Override
    public int compareTo(String o) {
        return 0;
    }
}
// error
//class Boss extends Employee implements Comparable<Integer> {
//    @Override
//    public int compareTo(Integer o) {
//        return 0;
//    }
//
//    @Override
//    public int compareTo(String o) {
//        return 0;
//    }
//}

// 上面的这一种 有可能是桥方法冲突,由于泛型表达式擦除....
//public int compareTo(Object other) { return compareTo((X) other); }
//    对于不同类型的 X 不能有两个这样的方法



class A implements Comparable<A> {
    @Override
    public int compareTo(A o) {
        return 0;
    }
}

class B extends A  {

}