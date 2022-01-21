/*
 * Copyright (c) 2003, 2017, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.lang.reflect;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.lang.annotation.Repeatable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import sun.reflect.annotation.AnnotationSupport;
import sun.reflect.annotation.AnnotationType;

/**
 * Represents an annotated element of the program currently running in this
 * VM.  This interface allows annotations to be read reflectively.  All
 * annotations returned by methods in this interface are immutable and
 * serializable. The arrays returned by methods of this interface may be modified
 * by callers without affecting the arrays returned to other callers.
 *
 * 表示当前运行在vm上的程序上的一个被注解的元素..
 * 此接口允许注解能够反射读取..
 * 此接口中通过方法返回的注解 是不可变的且序列化的..
 * 但是此接口返回的数组能够被调用者修改 而不会影响其他调用者返回的数组 ..
 *
 * <p>The {@link #getAnnotationsByType(Class)} and {@link
 * #getDeclaredAnnotationsByType(Class)} methods support multiple
 * annotations of the same type on an element. If the argument to
 * either method is a repeatable annotation type (JLS 9.6), then the
 * method will "look through" a container annotation (JLS 9.7), if
 * present, and return any annotations inside the container. Container
 * annotations may be generated at compile-time to wrap multiple
 * annotations of the argument type.
 *
 *  注意getAnnotationsByType(Class) 以及 ... get..Declared ... 方法支持多个相同类型的注解 放置在同一个元素上..
 *  如果参数是一个可重复注解类型(JLS 9.6),那么此方法会通过容器注解查询(JLS 9.7),如果出现,那么将返回在此容器内的注解...
 *  从其注解也许在编译时就被生成来包装多个相同参数类型的注解...
 *
 * <p>The terms <em>directly present</em>, <em>indirectly present</em>,
 * <em>present</em>, and <em>associated</em> are used throughout this
 * interface to describe precisely which annotations are returned by
 * methods:
 *
 * <ul>
 * // 注解的关系有四种情况:
 * 1. 直接出现(注解,但是如果此注解可repeatable,那么它呈现形式就是不直接出现) / 2. 不直接出现(容器注解) 3. 存在(存在于class 类体系中) / 4. 关联.(接口上.)
 * <li> An annotation <i>A</i> is <em>directly present</em> on an
 * element <i>E</i> if <i>E</i> has a {@code
 * RuntimeVisibleAnnotations} or {@code
 * RuntimeVisibleParameterAnnotations} or {@code
 * RuntimeVisibleTypeAnnotations} attribute, and the attribute
 * contains <i>A</i>.
 *  A注解直接出现在E上, 且E 有一个运行时可见注解 / 参数注解 / 类型注解属性 - 是包含A的属性.. E是class Type / or 注解.
 * <li>An annotation <i>A</i> is <em>indirectly present</em> on an
 * element <i>E</i> if <i>E</i> has a {@code RuntimeVisibleAnnotations} or
 * {@code RuntimeVisibleParameterAnnotations} or {@code RuntimeVisibleTypeAnnotations}
 * attribute, and <i>A</i> 's type is repeatable, and the attribute contains
 * exactly one annotation whose value element contains <i>A</i> and whose
 * type is the containing annotation type of <i>A</i> 's type.
 *
 * 如果A 不直接出现在E上(如果它有一个注解属性... 并且A是可重复类型,并且此属性包含了一个注解(包含A注解的注解, 此类型是A的容器注解)
 *
 * <li>An annotation <i>A</i> is present on an element <i>E</i> if either:
 *  如果A注解出现在E上,分为两种情况:
 * <ul>
 *
 * <li><i>A</i> is directly present on <i>E</i>; or
 *   1. A直接出现 E
 * <li>No annotation of <i>A</i> 's type is directly present on
 * <i>E</i>, and <i>E</i> is a class, and <i>A</i> 's type is
 * inheritable, and <i>A</i> is present on the superclass of <i>E</i>.
 * A不直接出现在E上,并且E是class,且A类型是可继承的,那么A可能出现在 E的父类上...
 * </ul>
 *
 * <li>An annotation <i>A</i> is <em>associated</em> with an element <i>E</i>
 * if either:
 *  // A注解和一个元素关联:
 * <ul>
 * A直接出现在或者间接出现在E上..
 * <li><i>A</i> is directly or indirectly present on <i>E</i>; or
 * 或者A注解没有直接 / 间接的出现在E上,且E是一个class,并且A是一个类型是可继承的,并且A和E的父类关联..
 * <li>No annotation of <i>A</i> 's type is directly or indirectly
 * present on <i>E</i>, and <i>E</i> is a class, and <i>A</i>'s type
 * is inheritable, and <i>A</i> is associated with the superclass of
 * <i>E</i>.
 *
 * </ul>
 *
 * </ul>
 *
 * <p>The table below summarizes which kind of annotation presence
 * different methods in this interface examine.
 *
 * <table class="plain">
 * <caption>Overview of kind of presence detected by different AnnotatedElement methods</caption>
 * <thead>
 * <tr><th colspan=2 scope="col">Method</th>
 *     <th colspan=4 scope="col">Kind of Presence</th>
 * <tr><th scope="col">Return Type</th>
 *     <th scope="col">Signature</th>
 *     <th scope="col">Directly Present</th>
 *     <th scope="col">Indirectly Present</th>
 *     <th scope="col">Present</th>
 *     <th scope="col">Associated</th>
 * </thead>
 * <tbody>
 * <tr><td style="text-align:right">{@code T}</td>
 * <th scope="row" style="font-weight:normal; text-align:left">{@link #getAnnotation(Class) getAnnotation(Class&lt;T&gt;)}
 * <td></td><td></td><td style="text-align:center">X</td><td></td>
 * </tr>
 * <tr><td style="text-align:right">{@code Annotation[]}</td>
 * <th scope="row" style="font-weight:normal; text-align:left">{@link #getAnnotations getAnnotations()}
 * <td></td><td></td><td style="text-align:center">X</td><td></td>
 * </tr>
 * <tr><td style="text-align:right">{@code T[]}</td>
 * <th scope="row" style="font-weight:normal; text-align:left">{@link #getAnnotationsByType(Class) getAnnotationsByType(Class&lt;T&gt;)}
 * <td></td><td></td><td></td><td style="text-align:center">X</td>
 * </tr>
 * <tr><td style="text-align:right">{@code T}</td>
 * <th scope="row" style="font-weight:normal; text-align:left">{@link #getDeclaredAnnotation(Class) getDeclaredAnnotation(Class&lt;T&gt;)}
 * <td style="text-align:center">X</td><td></td><td></td><td></td>
 * </tr>
 * <tr><td style="text-align:right">{@code Annotation[]}</td>
 * <th scope="row" style="font-weight:normal; text-align:left">{@link #getDeclaredAnnotations getDeclaredAnnotations()}
 * <td style="text-align:center">X</td><td></td><td></td><td></td>
 * </tr>
 * <tr><td style="text-align:right">{@code T[]}</td>
 * <th scope="row" style="font-weight:normal; text-align:left">{@link #getDeclaredAnnotationsByType(Class) getDeclaredAnnotationsByType(Class&lt;T&gt;)}
 * <td style="text-align:center">X</td><td style="text-align:center">X</td><td></td><td></td>
 * </tr>
 * </tbody>
 * </table>
 *
 * <p>For an invocation of {@code get[Declared]AnnotationsByType( Class <
 * T >)}, the order of annotations which are directly or indirectly
 * present on an element <i>E</i> is computed as if indirectly present
 * annotations on <i>E</i> are directly present on <i>E</i> in place
 * of their container annotation, in the order in which they appear in
 * the value element of the container annotation.
 *
 * 对于get[Declared]AnnotationsByType的执行来说,注解的顺序 - 这些直接或者不直接出现在元素E上计算 是通过 ->
 * 注解的容器注解是否直接出现在E上的...  根据它们出现在容器注解的value 元素中的顺序...
 * <p>There are several compatibility concerns to keep in mind if an
 * annotation type <i>T</i> is originally <em>not</em> repeatable and
 * later modified to be repeatable.
 *
 * 这是一种兼容性的考虑,保持如果一个注解类型原本是不可重复的 后续修改为可重复的...
 *
 * The containing annotation type for <i>T</i> is <i>TC</i>.
 * 假如说 T的容器注解是 TC
 *
 * <ul>
 * 修改T 为可重复的 使得源代码且二进制兼容存在的T使用以及 TC的使用...
 * <li>Modifying <i>T</i> to be repeatable is source and binary
 * compatible with existing uses of <i>T</i> and with existing uses
 * of <i>TC</i>.
 *
 * // 那就是 对于源兼容性,源代码上T注解s的批注以及TC的注解批注都可以编译,为了二进制兼容,T类型的注解s 类文件或者Tc类型的注解批注(或者说T的另一种使用方式或者说TC的使用方式)
 * 将会链接到T的修改版本(如果他们链接到之前的版本)
 * That is, for source compatibility, source code with annotations of
 * type <i>T</i> or of type <i>TC</i> will still compile. For binary
 * compatibility, class files with annotations of type <i>T</i> or of
 * type <i>TC</i> (or with other kinds of uses of type <i>T</i> or of
 * type <i>TC</i>) will link against the modified version of <i>T</i>
 * if they linked against the earlier version.
 *
 * // 这就是为什么 T可以单独存在,仅仅是作为一个普通注解,当T重复时 直接演变为TC的注解使用..
 *
 * (An annotation type <i>TC</i> may informally serve as an acting
 * containing annotation type before <i>T</i> is modified to be
 * formally repeatable. Alternatively, when <i>T</i> is made
 * repeatable, <i>TC</i> can be introduced as a new type.)
 *
 *  一个注解元素TC 类型 也许在T修改为正式的可重复之前是一个非正式的容器注解类型...
 *  当使得它可以重复时,TC就是一个新的类型(之前不存在,但是可以假设,现在存在是一个全新的类型)
 *
 *  这就是为什么 getAnnotation / getAnnotationsByType 能够同时抓到非正式容器注解的注解内容的原因...
 *
 * <li>If an annotation type <i>TC</i> is present on an element, and
 * <i>T</i> is modified to be repeatable with <i>TC</i> as its
 * containing annotation type then:
 *  如果一个注解TC出现在element 上,T 修改为重复 能够使用 TC作为T的容器注解
 * <ul>
 *
 *  改变T的行为兼容get[Declared]Annotation方法 和 get[Declared]Annotations 方法结果一致...
 * <li>The change to <i>T</i> is behaviorally compatible with respect
 * to the {@code get[Declared]Annotation(Class<T>)} (called with an
 * argument of <i>T</i> or <i>TC</i>) and {@code
 * get[Declared]Annotations()} methods because the results of the
 * methods will not change due to <i>TC</i> becoming the containing
 * annotation type for <i>T</i>.
 *
 * // 但是这改变了get[Declared]AnnotationsByType的方法调用结果 - 参数为T
 * 因为这些方法识别了TC为T的容器注解,将会查询TC来暴露T的注解...
 * // 于是在单个注解的情况下,抓取容器注解并没有暴露,所以为空...
 * // 但是上面的getAnnotation / get...Annotations 能够正常获取到T注解..
 * <li>The change to <i>T</i> changes the results of the {@code
 * get[Declared]AnnotationsByType(Class<T>)} methods called with an
 * argument of <i>T</i>, because those methods will now recognize an
 * annotation of type <i>TC</i> as a container annotation for <i>T</i>
 * and will "look through" it to expose annotations of type <i>T</i>.
 *
 * </ul>
 *
 * <li>If an annotation of type <i>T</i> is present on an
 * element and <i>T</i> is made repeatable and more annotations of
 * type <i>T</i> are added to the element:
 *  如果T的注解出现在element 上,且T可重复且存在多个..
 * <ul>
 *
 * <li> The addition of the annotations of type <i>T</i> is both
 * source compatible and binary compatible.
 * T的注解列表是为了源代码和二进制兼容...
 *
 * <li>The addition of the annotations of type <i>T</i> changes the results
 * of the {@code get[Declared]Annotation(Class<T>)} methods and {@code
 * get[Declared]Annotations()} methods, because those methods will now
 * only see a container annotation on the element and not see an
 * annotation of type <i>T</i>.
 *  这个注解列表将会改变get[Declared]Annotation 以及 get[Declared]Annotations 方法的结果
 *  // 因为这些方法现在仅仅查看这个元素上的T的容器注解, 不会查看T注解.
 * <li>The addition of the annotations of type <i>T</i> changes the
 * results of the {@code get[Declared]AnnotationsByType(Class<T>)}
 * methods, because their results will expose the additional
 * annotations of type <i>T</i> whereas previously they exposed only a
 * single annotation of type <i>T</i>.
 * 同样也会改变get[Declared]AnnotationsByType的结果,因为现在通过容器注解暴露了额外的T注解,但是之前仅仅是一个普通T注解..
 * </ul>
 *
 * </ul>
 *
 * <p>If an annotation returned by a method in this interface contains
 * (directly or indirectly) a {@link Class}-valued member referring to
 * a class that is not accessible in this VM, attempting to read the class
 * by calling the relevant Class-returning method on the returned annotation
 * will result in a {@link TypeNotPresentException}.
 *
 * 此接口调用返回的注解如果存在此vm 无法直接或者间接访问的Class value 成员那么 会抛出一个TypeNotPresentException异常..
 *
 * <p>Similarly, attempting to read an enum-valued member will result in
 * a {@link EnumConstantNotPresentException} if the enum constant in the
 * annotation is no longer present in the enum type.
 * 相似的,尝试读取一个枚举成员将会导致EnumConstantNotPresentException (如果在注解中的枚举约束没有出现在enum类型上)
 * 也有可能是类未加载...
 *
 * <p>If an annotation type <i>T</i> is (meta-)annotated with an
 * {@code @Repeatable} annotation whose value element indicates a type
 * <i>TC</i>, but <i>TC</i> does not declare a {@code value()} method
 * with a return type of <i>T</i>{@code []}, then an exception of type
 * {@link java.lang.annotation.AnnotationFormatError} is thrown.
 *
 * 如果一个T注解是一个 使用了@Repeatable的元注解 - 他需要指定一个容器注解TC,但是TC 如果没有没有声明[] value() 方法
 * 那么将抛出一个AnnotationFormatError...
 *
 * <p>Finally, attempting to read a member whose definition has evolved
 * incompatibly will result in a {@link
 * java.lang.annotation.AnnotationTypeMismatchException} or an
 * {@link java.lang.annotation.IncompleteAnnotationException}.
 * 最终,尝试读取一个成员(它的定义已经 演变为不兼容的情况下,会抛出一个AnnotationTypeMismatchException ... ..
 * @see java.lang.EnumConstantNotPresentException
 * @see java.lang.TypeNotPresentException
 * @see AnnotationFormatError
 * @see java.lang.annotation.AnnotationTypeMismatchException
 * @see java.lang.annotation.IncompleteAnnotationException
 * @since 1.5
 * @author Josh Bloch
 */
public interface AnnotatedElement {
    /**
     * Returns true if an annotation for the specified type
     * is <em>present</em> on this element, else false.  This method
     * is designed primarily for convenient access to marker annotations.
     *
     * <p>The truth value returned by this method is equivalent to:
     * {@code getAnnotation(annotationClass) != null}
     *
     * <p>The body of the default method is specified to be the code
     * above.
     *
     * @param annotationClass the Class object corresponding to the
     *        annotation type
     * @return true if an annotation for the specified annotation
     *     type is present on this element, else false
     * @throws NullPointerException if the given annotation class is null
     * @since 1.5
     */
    default boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return getAnnotation(annotationClass) != null;
    }

   /**
     * Returns this element's annotation for the specified type if
     * such an annotation is <em>present</em>, else null.
     *
     * @param <T> the type of the annotation to query for and return if present
     * @param annotationClass the Class object corresponding to the
     *        annotation type
     * @return this element's annotation for the specified annotation type if
     *     present on this element, else null
     * @throws NullPointerException if the given annotation class is null
     * @since 1.5
     */
    <T extends Annotation> T getAnnotation(Class<T> annotationClass);

    /**
     * Returns annotations that are <em>present</em> on this element.
     *
     * If there are no annotations <em>present</em> on this element, the return
     * value is an array of length 0.
     *
     * The caller of this method is free to modify the returned array; it will
     * have no effect on the arrays returned to other callers.
     *
     * @return annotations present on this element
     * @since 1.5
     */
    Annotation[] getAnnotations();

    /**
     * Returns annotations that are <em>associated</em> with this element.
     *
     * If there are no annotations <em>associated</em> with this element, the return
     * value is an array of length 0.
     *
     * The difference between this method and {@link #getAnnotation(Class)}
     * is that this method detects if its argument is a <em>repeatable
     * annotation type</em> (JLS 9.6), and if so, attempts to find one or
     * more annotations of that type by "looking through" a container
     * annotation.
     *
     * The caller of this method is free to modify the returned array; it will
     * have no effect on the arrays returned to other callers.
     *
     * @implSpec The default implementation first calls {@link
     * #getDeclaredAnnotationsByType(Class)} passing {@code
     * annotationClass} as the argument. If the returned array has
     * length greater than zero, the array is returned. If the returned
     * array is zero-length and this {@code AnnotatedElement} is a
     * class and the argument type is an inheritable annotation type,
     * and the superclass of this {@code AnnotatedElement} is non-null,
     * then the returned result is the result of calling {@link
     * #getAnnotationsByType(Class)} on the superclass with {@code
     * annotationClass} as the argument. Otherwise, a zero-length
     * array is returned.
     *
     * @param <T> the type of the annotation to query for and return if present
     * @param annotationClass the Class object corresponding to the
     *        annotation type
     * @return all this element's annotations for the specified annotation type if
     *     associated with this element, else an array of length zero
     * @throws NullPointerException if the given annotation class is null
     * @since 1.8
     */
    default <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
         /*
          * Definition of associated: directly or indirectly present OR
          * neither directly nor indirectly present AND the element is
          * a Class, the annotation type is inheritable, and the
          * annotation type is associated with the superclass of the
          * element.
          */
         T[] result = getDeclaredAnnotationsByType(annotationClass);

         if (result.length == 0 && // Neither directly nor indirectly present
             this instanceof Class && // the element is a class
             AnnotationType.getInstance(annotationClass).isInherited()) { // Inheritable
             Class<?> superClass = ((Class<?>) this).getSuperclass();
             if (superClass != null) {
                 // Determine if the annotation is associated with the
                 // superclass
                 result = superClass.getAnnotationsByType(annotationClass);
             }
         }

         return result;
     }

    /**
     * Returns this element's annotation for the specified type if
     * such an annotation is <em>directly present</em>, else null.
     *
     * This method ignores inherited annotations. (Returns null if no
     * annotations are directly present on this element.)
     *
     * @implSpec The default implementation first performs a null check
     * and then loops over the results of {@link
     * #getDeclaredAnnotations} returning the first annotation whose
     * annotation type matches the argument type.
     *
     * @param <T> the type of the annotation to query for and return if directly present
     * @param annotationClass the Class object corresponding to the
     *        annotation type
     * @return this element's annotation for the specified annotation type if
     *     directly present on this element, else null
     * @throws NullPointerException if the given annotation class is null
     * @since 1.8
     */
    default <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass) {
         Objects.requireNonNull(annotationClass);
         // Loop over all directly-present annotations looking for a matching one
         for (Annotation annotation : getDeclaredAnnotations()) {
             if (annotationClass.equals(annotation.annotationType())) {
                 // More robust to do a dynamic cast at runtime instead
                 // of compile-time only.
                 return annotationClass.cast(annotation);
             }
         }
         return null;
     }

    /**
     * Returns this element's annotation(s) for the specified type if
     * such annotations are either <em>directly present</em> or
     * <em>indirectly present</em>. This method ignores inherited
     * annotations.
     *
     * If there are no specified annotations directly or indirectly
     * present on this element, the return value is an array of length
     * 0.
     *
     * The difference between this method and {@link
     * #getDeclaredAnnotation(Class)} is that this method detects if its
     * argument is a <em>repeatable annotation type</em> (JLS 9.6), and if so,
     * attempts to find one or more annotations of that type by "looking
     * through" a container annotation if one is present.
     *
     * The caller of this method is free to modify the returned array; it will
     * have no effect on the arrays returned to other callers.
     *
     * @implSpec The default implementation may call {@link
     * #getDeclaredAnnotation(Class)} one or more times to find a
     * directly present annotation and, if the annotation type is
     * repeatable, to find a container annotation. If annotations of
     * the annotation type {@code annotationClass} are found to be both
     * directly and indirectly present, then {@link
     * #getDeclaredAnnotations()} will get called to determine the
     * order of the elements in the returned array.
     *
     * <p>Alternatively, the default implementation may call {@link
     * #getDeclaredAnnotations()} a single time and the returned array
     * examined for both directly and indirectly present
     * annotations. The results of calling {@link
     * #getDeclaredAnnotations()} are assumed to be consistent with the
     * results of calling {@link #getDeclaredAnnotation(Class)}.
     *
     * @param <T> the type of the annotation to query for and return
     * if directly or indirectly present
     * @param annotationClass the Class object corresponding to the
     *        annotation type
     * @return all this element's annotations for the specified annotation type if
     *     directly or indirectly present on this element, else an array of length zero
     * @throws NullPointerException if the given annotation class is null
     * @since 1.8
     */
    default <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> annotationClass) {
        Objects.requireNonNull(annotationClass);
        return AnnotationSupport.
            getDirectlyAndIndirectlyPresent(Arrays.stream(getDeclaredAnnotations()).
                                            collect(Collectors.toMap(Annotation::annotationType,
                                                                     Function.identity(),
                                                                     ((first,second) -> first),
                                                                     LinkedHashMap::new)),
                                            annotationClass);
    }

    /**
     * Returns annotations that are <em>directly present</em> on this element.
     * This method ignores inherited annotations.
     *
     * If there are no annotations <em>directly present</em> on this element,
     * the return value is an array of length 0.
     *
     * The caller of this method is free to modify the returned array; it will
     * have no effect on the arrays returned to other callers.
     *
     * @return annotations directly present on this element
     * @since 1.5
     */
    Annotation[] getDeclaredAnnotations();
}
