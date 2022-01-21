package javareflectannotatedtype;

import jdk.swing.interop.SwingInterOpUtils;

import java.lang.annotation.*;
import java.util.Arrays;
/**
 * @author FLJ
 * @dateTime 2022/1/19 14:20
 * @description 复习建议直接查看 present / associate 方法...
 */
public class annotatedTypeTests {
    public static void main(String[] args) {

        // 检测容器注解的直接或者间接出现
        Element[] annotationsByType = Demo.class.getAnnotationsByType(Element.class);
        System.out.println(Arrays.toString(annotationsByType));

        ElementContainer[] annotationsByType1 = Demo1.class.getAnnotationsByType(ElementContainer.class);
        System.out.println(Arrays.toString(annotationsByType1));


        // getAnnotations
        Element annotation = Demo.class.getAnnotation(Element.class);
        System.out.println(annotation);

        Element annotation1 = Demo1.class.getAnnotation(Element.class);
        System.out.println(annotation1);

        Direction annotation2 = Demo.class.getAnnotation(Direction.class);
        System.out.println(annotation2);

        InDirection annotation3 = Demo.class.getAnnotation(InDirection.class);
        System.out.println(annotation3);

        // 存在是可继承的间接出现..
        System.out.println(Demo.class.getAnnotation(InDirectionNoInheritable.class));
        System.out.println(Demo.class.getAnnotation(InDirectionInheritable.class));
        System.out.println(Demo.class.getAnnotation(InDirection.class));

        System.out.println(Demo.class.getAnnotation(SuperElementContainer.class));
        // getAnnotations
        System.out.println(Arrays.toString(Demo.class.getAnnotations()));

        // getAnnotationsByType
        System.out.println(Arrays.toString(Demo.class.getAnnotationsByType(SuperElement.class)));
        SuperElementContainer[] annotationsByType2 = Demo.class.getAnnotationsByType(SuperElementContainer.class);
        System.out.println(Arrays.toString(annotationsByType2));

        // 直接出现,是必须注解到目标上
        // 间接出现,是作为容器注解的出现..,意味着注解是可repeatable...

        // 存在,表示在类体系上存在,且符合java inheritable语义的  直接 / 或者间接出现的注解..
        // 然后通过getAnnotationsByType(... ) 可以获取相关联的注解..


        // 最后我们来总结
//        1. direct present
        // 注解直接出现在另一个Element上
        // 2. 间接present
        // 注解依靠容器注解出现在类上..(这里的类说明的是 被注解的目标元素)

        // 3. present (存在)
        // 满足条件1
        // 它存在于此类的父类..,且可Inheritable

        // 4. 联系
        // 满足条件 1 , 2
        // 且目标元素是类, 注解A可Inheritable, 注解与父类联系.
        present();

        associate();

        relationShip();

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Repeatable(EleContainer.class)
    @Inherited
    @interface  Ele {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    @interface  EleContainer {
        Ele[] value() default {};
    }

    // 根据上述四条定论,我们来测试后两种
    // 本质上直接和间接出现都指的是它们没有必要使用inheritable 就能够直接注解到目标上或者目标类上..(距离仅仅是0层)

    public static void present() {
        System.out.println(" ----------------------------------------------------------------------- ");
        // 1. 直接出现
        // 2. 出现在父类上且具有inheritable..

        // 那么我们举一个反例..


        @InDirection
        @SuperElement("123")
        @SuperElement("123")
        @Ele
        class A {

        }
        class B extends A{

        }
        class C  extends B{

        }

        // getAnnotation(Class)
        // 可以获取present 关系的注解

        // 发现只要出现在父类体系中,它就是可以获取的... ,注意是类体系..
        System.out.println(C.class.getAnnotation(InDirection.class));


        // 那么我们不直接出现,且具有inheritable,能否拿到
        // 这里分为两个条件,容器注解的注解重复出现了.. 那么直接拿去(无论它是否inheritable)的注解无法拿到..
        System.out.println(C.class.getAnnotation(SuperElement.class)); // 事实证明无法拿到
        // 容器注解的注解没有重复出现,且heritable 那么 直接getAnnotation 能够拿到,因为此方法的语义就是拿去present语义的注解,很显然此时它满足...
        // 如果没有,无法拿到..
        System.out.println(C.class.getAnnotation(Ele.class)); // 无法拿到. / 修改@inheritable 就可拿到

        // 所以getAnnotation 照顾 present 语义..

        // 此时容器注解依旧可以拿到
        // 因为容器注解(在子注解repeat的情况下)会自动编译时编译..
        // 或者显式的容器注解定义在类上
        // 那么都符合present
        System.out.println(C.class.getAnnotation(SuperElementContainer.class));

        // 根据associate 的规则 Ele 直接出现在A上(但不是直接出现在C上)(但是没有inheritable)
        System.out.println(Arrays.toString(C.class.getAnnotationsByType(Ele.class)));
        // 此时单个容器注解的注解在没有重复的时候,它不是一个正式注解类型
        // 所以就算EleContainer inheritable 也无法拿到
        System.out.println(Arrays.toString(C.class.getAnnotationsByType(EleContainer.class)));

        // 此时仅有的办法就是 需要inheritable 才可以
        // 那么上述getAnnotationsByType 即可查到..
        // 这时候 getAnnotation / getAnnotationsByType 是为了兼容,查看AnnotatedElement   有这一部分的说明 ...

    }

    public static  void associate() {
        System.out.println(" ---------------------------------------------------- ");
        // associate
        // 1. 满足条件 1 / 2
        // 2. 和目标类的父类关联(关联... 满足 1和2 那么注解InDirection 和父类A关联没有问题)
        @InDirection
        @SuperElement
        @SuperElement("123")
        class A {

        }
        class B extends A{

        }
        class C  extends B{

        }

        // 可以发现 依旧可以获取...
        System.out.println(Arrays.toString(C.class.getAnnotationsByType(InDirection.class)));
        // getAnnotationsByType
        // 照顾associate语义.
        // 根据associate 语义, SuperElement 出现在A上,且可以inheritable ,所以可以拿到
        SuperElement[] annotationsByType = C.class.getAnnotationsByType(SuperElement.class);
        System.out.println(Arrays.toString(annotationsByType)); // 如果inheritable 去掉,则无法拿到

        // 此时 容器注解 同样也可以通过这个拿到,只要它是inheritable的
        // 我们来分析一下... 它同样直接出现在A上
        System.out.println(Arrays.toString(C.class.getAnnotationsByType(SuperElementContainer.class)));



    }

    public static void relationShip() {

        @SuperElement
        @SuperElement("123")
        @InDirectionInheritable
        class A {

        }
        class B extends A{

        }
        @Element
        @Element
        class C  extends B{

        }


        System.out.println(AnnotatedUtil.findRelationShipBy(C.class, SuperElement.class));

        System.out.println(AnnotatedUtil.findRelationShipBy(C.class,InDirection.class)); // unknown

        System.out.println(AnnotatedUtil.findRelationShipBy(C.class,Element.class));


        System.out.println(AnnotatedUtil.findRelationShipBy(C.class,SuperElementContainer.class));

        System.out.println(C.class.getAnnotation(Element.class));
        System.out.println(Arrays.toString(C.class.getAnnotations()));

        System.out.println(AnnotatedUtil.findRelationShipBy(InDirectionInheritable.class,InDirection.class));
    }

}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(ElementContainer.class)
@interface Element {
    String value() default "";
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface ElementContainer {
    Element[] value() default {};
}

@ElementContainer({@Element("demo"),
        @Element("demo1"),
        @Element("demo2")})
@Direction
class Demo extends DemoSuperClass implements DemoInterface{

}
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface Direction {
    String value() default "";
}
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@interface InDirection {
    String value() default "";
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface InDirectionNoInheritable {
    String value() default "";
}
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@InDirection
@Inherited
@interface InDirectionInheritable {
    String value() default "";
}
@InDirection
interface DemoInterface {

}
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(SuperElementContainer.class)
@Inherited
@interface SuperElement {
   String value() default "";
}


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@interface SuperElementContainer {
    SuperElement[] value() default {};
}



@InDirectionNoInheritable
@InDirectionInheritable
@SuperElement
@SuperElement("123")
class DemoSuperClass {

}

@Element("demo")
@Element("demo1")
@Element("demo2")
class Demo1 {

}