package core.lang.exception;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ExceptionTests {
    // 异常处理,try 资源子句
    @Test
    public void exception() {
        try (Scanner in = new Scanner(new FileInputStream("7usr/share/dict/words"), StandardCharsets.UTF_8);
             PrintWriter out = new PrintWriter("out.txt"))
        {
            while (in.hasNext())
                out.println(in.next().toUpperCase());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // 会自动执行Closable 接口的close方法..
            // 然后会作为压制异常放置在顶部异常中...
            // 可以通过getSuppressed 获取被压制的异常..
            // StackTraceElement 可以获取文件名 / 方法 / 类名 /   行号.

            // 早抛出/ 晚捕获
        }
    }
}
