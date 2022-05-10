package nio;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * @author JASONJ
 * @date 2022/5/9
 * @time 23:09
 * @description channel的聚合和 分离
 **/
public class ScatterAndGatherTests {

    @Test
    public void test() throws FileNotFoundException {

        final val randomAccessFile = new RandomAccessFile("","rw");

        final val channel = randomAccessFile.getChannel();


        // buffer gather ...
//        channel.write(new ByteBuffer[]{});

        // channel to separate buffer
        // scatter buffer
//        channel.read(new ByteBuffer[]{})
    }
}
