package genericTests;

import org.junit.jupiter.api.Test;

import java.beans.*;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

public class BridgeMethodTests {
    @Test
    public void test() throws IntrospectionException {

        // 桥接方法
        BeanInfo beanInfo = Introspector.getBeanInfo(A.class);
        System.out.println("Property: ");
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            System.out.println(propertyDescriptor.getPropertyType());
            System.out.println(propertyDescriptor.getPropertyEditorClass());
            System.out.println(propertyDescriptor.getReadMethod());
            System.out.println(propertyDescriptor.getWriteMethod());
            System.out.println("----------------------");
        }
        System.out.println("Method: ");
        for (MethodDescriptor methodDescriptor : beanInfo.getMethodDescriptors()) {
            System.out.println("parameters: " + Arrays.toString(methodDescriptor.getParameterDescriptors()));
            System.out.println(methodDescriptor.getMethod());
            Method method = methodDescriptor.getMethod();
            // 获取泛型方法上的泛型声明 - 使用类型变量表示
            TypeVariable<Method>[] typeParameters = method.getTypeParameters();
            System.out.println("---------------" + Arrays.toString(typeParameters));
        }
    }

        @Test
        public void test1() {
            Method[] declaredMethods = BridgeMethodTests.class.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                System.out.println("type declared: " + Arrays.toString(declaredMethod.getTypeParameters()));
                System.out.println(declaredMethod.getGenericReturnType());

                System.out.println("type parameter resolve: ");
                if(declaredMethod.getTypeParameters().length >0) {
                    TypeVariable<Method>[] typeParameters = declaredMethod.getTypeParameters();
                    for (TypeVariable<Method> typeParameter : typeParameters) {
                        if (typeParameter.getBounds().length > 0) {
                            Type bound = typeParameter.getBounds()[0];
                            if(bound instanceof ParameterizedType) {
                                System.out.println("raw type: " + ((ParameterizedType) bound).getRawType());
                                System.out.println("限定" + Arrays.toString(((ParameterizedType) bound).getActualTypeArguments()));
                            }
                        }
                    }
                }
            }
        }

        @Test
        public void test3() throws NoSuchMethodException {
            Method compareTo = String.class.getMethod("compareTo", String.class);
            Method compareTo1 = String.class.getMethod("compareTo", Object.class);
            System.out.println(compareTo.isBridge());
            System.out.println(compareTo1.isBridge());
        }


    public static  <T> T get(T t) {
        return t;
    }

    public static  <T extends Comparable<? super T>> T get(T t) {
        return t;
    }

}



