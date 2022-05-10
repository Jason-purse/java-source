package nio;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class NioTests {

    /**
     * socketChannel
     * @throws IOException
     */
    @Test
    public void test() throws IOException {

        // open 并连接远程socket
        SocketChannel clientChannel = SocketChannel.open(new InetSocketAddress("www.baidu.com", 80));
        // 设置一些选项
//        clientChannel.setOption()

        clientChannel.configureBlocking(false);
        System.out.println(String.format("connection status %s", clientChannel.isConnected()));
        val allocate = ByteBuffer.allocate(1024);
        final val byteArrayOutputStream = new ByteArrayOutputStream();
        while(true) {
            final val read = clientChannel.read(allocate);
            if(read > 0) {
                byteArrayOutputStream.write(allocate.flip().array());
                allocate.clear();
                continue;
            }
            break;
        }

        System.out.println(String.format("read bytes numbers %s", byteArrayOutputStream.size()));

        System.out.println(new String(byteArrayOutputStream.toByteArray()));
        // 关闭
        clientChannel.close();
        System.out.println("over");
    }

    /**
     * ServerSocketChannel
     */
    @Test
    public void serverSocketChannel() throws IOException, InterruptedException {
        // 开启一个channel
        final ServerSocketChannel open = ServerSocketChannel.open();
        // 需要绑定一个端口
        open.bind(new InetSocketAddress(63333));
        // 设置非阻塞状态
        open.configureBlocking(false);
        System.out.println("等待连接");
        while(true) {
            // 获取一个socketChannel
            final val accept = open.accept();

            // 有可能为空
            if (accept != null) {
                System.out.println("has a connection connected !!!!");
                final ByteBuffer allocate = ByteBuffer.allocate(1024);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                // 永不停止
                while(true) {
                    TimeUnit.MILLISECONDS.sleep(30);
                    // 防止阻塞
                    accept.configureBlocking(false);
                    final val read = accept.read(allocate);
                    System.out.println(read);
                    if(read > 0) {
                        outputStream.write(allocate.flip().array());
                        allocate.clear();
                        System.out.println("reading ....");
                        continue;
                    }
                    break;
                }
                System.out.println(String.format("receive data : %s", new String(outputStream.toByteArray())));
                outputStream.close();

                // 应答一句话 ...
                final val wrap = ByteBuffer.wrap("hello,i am is server !!!".getBytes());
                accept.write(wrap);
                accept.close();
            }
            else {
                System.out.println("waiting connection .....");
            }
            TimeUnit.MILLISECONDS.sleep(300);
        }
    }

    /**
     * receive side
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void udpSocketChannel1() throws IOException, InterruptedException {
        // 基于udp的形式,所以
        // 没有连接的概念 ..

        final val open = DatagramChannel.open();
        open.configureBlocking(false);
        open.bind(new InetSocketAddress(63333));

        // 特点就是  数据包 是一次性的, 所以 数据包的大小一般是固定的...

        while(true) {
            final val allocate = ByteBuffer.allocate(1024);
            final SocketAddress receive = open.receive(allocate);
            if(receive != null) {
                System.out.println("receive address connection: " + receive);

                System.out.println("receive data: " + new String(allocate.flip().array()));
                allocate.clear();

                // 发送回复,否则客户端无法释放 ...
                open.send(ByteBuffer.wrap("bye,client".getBytes()),receive);
                // 这个连接不能关,因为datagram 没有真正连接的概念 ...
//                open.close();
            }
            else {
                System.out.println("waiting connection ....");
            }

            TimeUnit.MILLISECONDS.sleep(300);
        }

    }

    @Test
    public void datagramSocketChannel2() throws IOException {

        // 这里我们简单使用 阻塞模式
        final val bind = DatagramChannel.open().bind(new InetSocketAddress(63335));
        // 需要连接到目标 进行数据发送
        final val connect = bind.connect(new InetSocketAddress("localhost",63333));
        final val wrap = ByteBuffer.wrap("hello,server".getBytes());
        final val write = bind.write(wrap);
        final val allocate = ByteBuffer.allocate(1024);
        final val read = bind.read(allocate);
        // 这里的flip 不管用 .. 并不是,是因为array 返回的是 映射的地址 .. 所以没有加以限制,所以显示了空数据. ..
        // 这个时候,我们通过compact(将limit 设置到未读数据的最后一位)
        System.out.println(allocate.position());
        // 一个字节一个字节读 可以 ..
//        while(allocate.hasRemaining()) {
//
//            final val b = allocate.get();
//        }
        allocate.flip();
        System.out.println(allocate.limit());
        System.out.println(allocate.capacity());
        System.out.println(new String(allocate.array(),0,read));
        // 关闭
        bind.close();
    }
}
