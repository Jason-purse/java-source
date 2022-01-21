package javareflectannotatedtype;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.AnnotatedElement;

/**
 * @author FLJ
 * @dateTime 2022/1/19 14:45
 * @description 用来判断 注解和 AnnotatedElement 的关系
 * 让关系更加趋向清晰化...
 */
public class AnnotatedUtil {
    // 关系
    enum RelationShip {
        // 注解直接 出现在Element上
        DIRECT_PRESENT("direct_present"),
        // 基于容器注解直接出现在Element上
        INDIRECT_PRESENT("indirect_present"),
        // 满足DIRECT_PRESENT ,且 注解可以出现在类体系上拥有inheritable注解..
        PRESENT("present"),
        // 满足 DIRECT/INDIRECT_PRESENT , Element为类,且注解与Element的父类所关联 ..
        ASSOCIATE("associate"),
        // 未知(可能没有Inheritable注解,可能确实没有出现)
        UNKNOWN("unknown(no inheritable in class hierarchy)");

        private final String relationShip;
        RelationShip(String relationShip) {
            this.relationShip = relationShip;
        }

        public String getRelationShip() {
            return relationShip;
        }

        @Override
        public String toString() {
            return String.format("%s[%s]", name(),getRelationShip());
        }
    }


    public static <T extends AnnotatedElement> RelationShip findRelationShipBy(AnnotatedElement annotatedElement, Class<? extends Annotation> annotationClass) {
        // 首先判断是否为直接出现
        // 1. no repeatable
        Annotation declaredAnnotation = annotatedElement.getDeclaredAnnotation(annotationClass);

        // 2. repeatable
        Annotation[] declaredAnnotationsByType = annotatedElement.getDeclaredAnnotationsByType(annotationClass);
        if(declaredAnnotationsByType.length > 0) {
            // 它只要为空,那么declaredAnnotationsByType 长度必然大于1
            if(declaredAnnotation == null) {
                return RelationShip.INDIRECT_PRESENT;
            }
            // 大家都有,且长度只可能为1
            return RelationShip.DIRECT_PRESENT;
        }

        if(declaredAnnotation != null) {
            return RelationShip.DIRECT_PRESENT;
        }

        // present
        // 出现在类的体系上
        Annotation annotation = annotatedElement.getAnnotation(annotationClass);
        // 判断是否为repeatable
        // repeatable 是普通注解,只需要present 那么子注解就可以repeatable
        Repeatable repeatable = annotationClass.getAnnotation(Repeatable.class);
        if(repeatable == null) {
            if(annotation != null) {
                return RelationShip.PRESENT;
            }
        }
        else {
            Annotation[] annotationsByType = annotatedElement.getAnnotationsByType(annotationClass);
            if(annotationsByType.length > 0) {
                if(annotation != null) {
                    return RelationShip.PRESENT;
                }
                return RelationShip.ASSOCIATE;
            }
        }
        return RelationShip.UNKNOWN;
    }

}
