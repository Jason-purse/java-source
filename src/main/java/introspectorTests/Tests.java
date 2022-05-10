package introspectorTests;

import org.junit.jupiter.api.Test;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Tests implements Serializable {


    @Test
    public void test() throws IntrospectionException {
        // 不会抓取私有属性
        // 因为它通过java bean的方式进行属性描述归类
        BeanInfo beanInfo = Introspector.getBeanInfo(A.class, Introspector.USE_ALL_BEANINFO);
        System.out.println(Arrays.toString(beanInfo.getPropertyDescriptors()));

        // redis hook
        //
        // 1 天积分   7天积分
        // 上百万的数据.... for...
        // status = false..
        // 11:30...
        //expire 1天 ... ...(每一条的...清除) 原子性..
//        redis ... 集群... -> 程序保证健壮性...

        // 1 省级 2 校级 3.县区级
        // 没有维度..  s dao
    }
}

class A {
    private String c;

    public static void main(String[] args) throws InterruptedException {

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                // 出现异常
                System.out.println("error: " + e.getMessage());
            }
        });
        // 用户态 内核态
        new Thread(() -> {
            throw new RuntimeException("123");
        }).start();
        TimeUnit.SECONDS.sleep(2);
        System.out.println("123123");
    }
}
