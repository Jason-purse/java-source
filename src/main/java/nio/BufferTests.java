package nio;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class BufferTests {

    @Test
    public void test() {

        // flip 扁平 limit 到当前位置
        // 这样不会读取空数据

        // clear 清空缓存区
        // compact 紧凑的意思  就是清理读过的缓存区域,然后将未读数据进行移动,将后续数据添加在后 ...



    }

    @Test
    public void directBuffer() {

        // 直接buffer 其含义就是堆外内存 ...
        // 减少一次拷贝,效率较高 ...
        final val byteBuffer = ByteBuffer.allocateDirect(1024);

    }

    @Test
    public void bufferSlice() {

        final val allocate = ByteBuffer.allocate(1024);
        // 根据当前buffer的 position / limit  分隔出一个 指定范围共享的Buffer,旧的buffer 对应范围内的数据更改能够在新的buffer中引起改变 ...
        allocate.position(10);
        allocate.limit(20);
        // 而且新的buffer position 是 0(它们之间的各种参数是独立的)...
        final val slice = allocate.slice();

        for (int i = 0; i < slice.capacity(); i++) {
            slice.put(((byte) i));
        }
        // 由于新的limit / capacity 受 父buffer限制,也成为子缓冲区 ...
        allocate.position(0);
        allocate.put(((byte) 123));

        System.out.println(Arrays.toString(allocate.array()));
        System.out.println("------------new buffer ---------------------");
        System.out.println(Arrays.toString(slice.array()));
    }

    @Test
    public void memeryMapBuffer() {
        // 内存映射 .. buffer 只是将需要读或者写的数据  拉入内存,效率比channel / file 快得多
//        MappedByteBuffer.allocateDirect()
//        final val rw = new RandomAccessFile("", "rw");

        // 将这一块放入内存 ....
//        final val map = rw.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, 200);
    }
}
