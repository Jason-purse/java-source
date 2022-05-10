package io;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author JASONJ
 * @date 2022/3/29
 * @time 21:51
 * @description ZIP 文件学习
 * Zip文件都有一个头,包含了文件的名字 以及使用到的压缩方法 .
 *
 **/
public class ZipFileTests {
    /**
     * 这个例子描述了如何处理一个zip文件
     */
    @Test
    public void test() throws IOException {
        ZipInputStream zipInputStream = null;
        try {
            zipInputStream = new ZipInputStream(new FileInputStream("src/main/java/io/txt.zip"));
            ZipEntry nextEntry = zipInputStream.getNextEntry(); // 获取下一项
            // 然后调用getInputStream 就可以获得该Entry 的输入流
            System.out.println(nextEntry);
            System.out.println(nextEntry.getComment());
            System.out.println(nextEntry.getCompressedSize());
            System.out.println(nextEntry.getCreationTime());
            System.out.println(nextEntry.getSize());
            System.out.println("读取当前entry 内容");
            byte[] bytes = zipInputStream.readAllBytes();// 这里是二进制内容 .. 可能会出现一定的乱码
            byte[] target = new byte[18];
            byte[] remains = new byte[bytes.length - 18];
             System.arraycopy(bytes,0,target,0,18);
             System.arraycopy(bytes,18,remains,0,remains.length);
             System.out.println(new String(target));

             // 剩下的Char 怎么读取
            // 使用DataInputStream 解决它..
            //java 天然的数据流 加上各种过滤类型的中间类 装饰 使得流的能力异常强大 ...
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(remains));
            while (dataInputStream.available()  > 0) {
                System.out.println(dataInputStream.readChar());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(zipInputStream != null) {
                zipInputStream.close();
            }
        }
    }
}
