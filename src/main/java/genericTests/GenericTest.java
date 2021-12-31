package genericTests;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericTest {

    public static <T> T getMiddle(T ... args) {
        return args[args.length / 2];
    }

    public interface TT<S> {

    }

    public static void main(String[] args) {
        // 泛型方法和泛型类中的方法有所不同,泛型方法会尝试寻找泛型参数的公共目标-相同类型..
        Q1<?> middle = GenericTest.getMiddle(new A1(), new A2());

        // raw type
        class Test extends Query<Test> implements Comparable<Test>{

            @Override
            public int compareTo(Test o) {
                return 0;
            }
        }

        Type genericSuperclass = Test.class.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType genericSuperclass1 = (ParameterizedType) genericSuperclass;
            // 仅仅这个类型是一个闭包类型(内嵌类型时),才存在所属类型.. 并且所属类型为顶层public 类型
            System.out.println(genericSuperclass1.getOwnerType());
            //rawType 表示类型参数被擦除之后的类型..
            System.out.println(genericSuperclass1.getRawType());
        }

        class TTT implements TT<TTT> {

        }

        Type genericSuperclass1 = TTT.class.getGenericInterfaces()[0];
        // 例如这个参数化类型 属于GenericTest
        if (genericSuperclass1 instanceof ParameterizedType) {
            System.out.println(((ParameterizedType) genericSuperclass1).getOwnerType());
        }


        // 切换限定..
        // 泛型表达式的翻译..(强制加入转换)
        // 这其实就是一个泛型表达式的翻译
        List<String> list = new ArrayList<>();
        List listed = list;
        listed.add(123);
        listed.add(456);

        // 没有问题
        System.out.println(listed.get(1));
        System.out.println(list.size());
        // 泛型表达式的翻译,这会导致报错..
//        String s = list.get(1);

        DefaultPair pair = new DefaultPair();
        Pair<String,Integer> pair1 = pair;
        // 并没有覆盖父类的方法》。。
        pair1.setObject("123");
        // 那么不会生成桥方法
        // 同样,进行类型擦除之后,如果存在方法重写,那么必然存在两个方法,例如set(Object),set(T),这就违背了类型擦除和多态性规则
        // 所以 java 编译器会给子类型生成一个桥方法set(Object),记住它是一个桥方法...

        // 所以在虚拟机上才存在
        // 在虚拟机中，用参数类型和返回类型确定一个方法

        // 协变,是一个对象自下而上的变换..
        // 例如子类重写从写父方法就是一种协变..
        // 非泛型方法的重写,也会产生桥方法,桥方法会调用新定义的具有更多限制的方法..
//        public class Employee implements Cloneable
//        {
//            public Employee clone() throws CloneNotSupportedException { . . . }
//        }
//        Objectxlone 和 Employee.clone 方法被说成具有协变的返回类型 （covariant return types。)
//        实际上，Employee 类有两个克隆方法：
//        Employee cloneO // defined above
//        Object clone() // synthesized bridge method, overrides Object,clone

        // 泛型的限制
        // 1. 不能够创建包含基础类型的泛型类实例
        // 2. 不能够实例化参数化数组..
        // 为什么不能够创建泛型数组,思考一下
        // 首先一个数组能够记住类型,在设置其他类型的时候,能够进行检查..
        // 但是如果一个泛型数组,那么类型擦除会导致类型检查失效,因为运行时类型检查只支持原始类型,于是平衡性被破坏,所以不允许创建泛型数组..
        // 3. 运行时类型查询只适合原始类型匹配》。
        // 4. 虽然不能够创建泛型数组,但是可以创建通配符数组.,可以进行强转..

        // vargs 警告
        // 由于vargs 其实是一个泛型数组,于是不可能让编译器创建泛型数组,这违反约定
        // 于是 java 减轻了限制,只是会得到一个警告..

        // 由于限制,你不能够new T (类型变量), 这是违法的..
        // 泛型数组也是不可能创建的.

        ABC abc = new ABC();
        // 泛型表达式翻译..
//        System.out.println(abc.getTarget());
        // 通过泛型欺骗编译器...
        // 本来这是一个受检查异常,通过将它使用泛型的方式欺骗编译器,让它认为这不是一个受检查异常...
        GenericTest.<RuntimeException>throwAs(new IllegalAccessException("123"));

        // 通配符捕获
        // 什么情况下,通配符才会完成捕捉
//        通配符捕获只有在有许多限制的情况下才是合法的。编译器必须能够确信通配符表达的
//        是单个、 确定的类型。
        // 例如Pair<T> 能够捕捉 Pair<?> 因为? 再怎么变,都是一个单一、确定的类型
        // 但是ArrayList<Pair<T>> 不能够捕捉 ArrayList<Pair<?>> 因为 ? 可能是可能是两种类型,而T是一种类型。。。。
        // 这个时候,T无法捕捉...


    }

    /**
     * 为什么这个可以,因为类型擦除之后,它的限定类型是Throwable ,就算你填一个RuntimeException,也不可能强转为Exception...
     * @param e
     * @param <T>
     * @throws T
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> void throwAs(Throwable e) throws T
    {
        throw (T)e;
    }

    // 为什么 T[]  t = (T[])Object[];
    // 类型擦除之后,就是Object ,强转,访问没有任何问题...
    // 但是如果是一个泛型方法的返回值(T[]) : Object[] -> T[] ,, 则会发生类型转换,是会出现问题的...
    static class AList<T> {
        protected T[] target;
        public AList() {
            this.target = (T[])new Object[0];
        }
    }

    static class ABC extends AList<ABC> {
        public ABC() {
            super();
        }

        public ABC[] getTarget() {
            return target;
        }
    }


}
interface Q1<T> {

}
interface Q2<T> {

}
class A1 implements Q1<A> ,Q2<A> {
 // 例如 A 是一个泛型,这里的target 是一个类型变量
    private A target;

    // A本身是一个类型参数..
}

class A2 implements Q1<A2>,Q2<A2> {

}
// 这个在类型擦除之后的T 的raw type 其实是Comparable
class Query<T extends Comparable<T>>  {
 private List<T> list;
}

//切换限定：
//class Interval<T extends Serializable & Comparable>
//会发生什么。如果这样做， 原始类型用 Serializable 替换 T, 而编译器在必要时要向
//        Comparable 插入强制类型转换。为了提高效率，应该将标签（tagging) 接口（即没有方
//        法的接口）放在边界列表的末尾

//类型擦除之后:
// class Iterval .... {
// private Comparable .....
//}
// 那么想变成Serializable 编译器就可能需要加入额外的强转... 所以建议没有方法的接口放在后面...


// 翻译泛型表达式..
// 其实泛型方法的泛型返回值  对应泛型类型的推断就是一个表达式...
// 意味着这样的一条代码其实在字节码中加入了强转的类型转换...


// 翻译泛型方法..
class Pair<K,V> {
    private K k;
    public K getKey() {
        return null;
    }

    public void setObject(K k) {
        this.k = k;
    }
    // 不允许
    // 为什么,java 不根据类型返回值来区分一个方法
    // 猜测可能是为了桥方法留下后路...
//    public Object getKey() {
//        return null;
//    }
}


class DefaultPair extends Pair<String,Integer> {

    public void setObject(Comparable comparable) {
        System.out.println("object");
    }


}

// 假设 DefaultPair 对象出生,通过Pair<String> 引用此对象
//
