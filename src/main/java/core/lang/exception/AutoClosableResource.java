package core.lang.exception;

import java.io.Closeable;
import java.io.IOException;
/**
 * @author FLJ
 * @dateTime 2022/1/21 13:00
 * @description 自动关闭资源
 */
public class AutoClosableResource implements Closeable {
    @Override
    public void close() throws IOException {
        // close
        System.out.println("close");
    }
    // printf
    public void printf() {
        System.out.println("printf");
    }
}
