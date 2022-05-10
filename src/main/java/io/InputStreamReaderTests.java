package io;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

public class InputStreamReaderTests {
//
//    @Test
//    public void test() throws FileNotFoundException {
//        // 将字节流 转换为字符流
//        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream("src/main/java/io/test.txt"), Charset.defaultCharset());
//
//        // 如果文件过大
//        // 我们可以使用
////        Files.lines() 使用惰性流的方式处理..
//    }
//
//    @Test
//    public void charEncoding() throws UnsupportedEncodingException {
//
//    }

    public static void main(String[] args) throws Exception {
        byte[][] bytes = {
// 00110001
                {(byte)0x31},
// 11000000 10110001
                {(byte)0xC0,(byte)0xB1},
// 11100000 10000000 10110001
                {(byte)0xE0,(byte)0x80,(byte)0xB1},
// 11110000 10000000 10000000 10110001
                {(byte)0xF0,(byte)0x80,(byte)0x80,(byte)0xB1},
// 11111000 10000000 10000000 10000000 10110001
                {(byte)0xF8,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0xB1},
// 11111100 10000000 10000000 10000000 10000000 10110001
                {(byte)0xFC,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0xB1},
        };
        for (int i = 0; i < 6; i++) {
            String str = new String(bytes[i], "UTF-8");
            System.out.println("原数组长度：" + bytes[i].length +
                    "/t转换为字符串：" + str +
                    "/t转回后数组长度：" + str.getBytes("UTF-8").length);
        }
    }

    @Test
    public void unicode() throws UnsupportedEncodingException {
        String value = "严";

        System.out.println(value.getBytes(UTF_8).length); // 3个字节
        char[] chars = value.toCharArray();
        System.out.println(chars.length);
        for (char aChar : chars) {
            System.out.print(Integer.toHexString(aChar));
        }
    }

    @Test
    public void dataPoint() throws IOException, URISyntaxException {
        File file = new File("src/main/java/io/txt.dat");
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file,false));
        dataOutputStream.write("严".getBytes(UTF_8));
        dataOutputStream.write("严".getBytes(UTF_8));
        dataOutputStream.write("严".getBytes(UTF_8));
        dataOutputStream.write("严".getBytes(UTF_8));
        dataOutputStream.write("严".getBytes(UTF_8));
        dataOutputStream.write("严".getBytes(UTF_8));
        dataOutputStream.writeChars("完犊子234");

        dataOutputStream.flush();
        dataOutputStream.close();


        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));

        byte[] bytes = dataInputStream.readNBytes(18);
//        byte[] bytes = dataInputStream.readAllBytes();
        System.out.println(bytes.length);

        System.out.println(new String(bytes, UTF_8));
        System.out.println(dataInputStream.readChar());
        dataInputStream.close();
        // 面向jvm 输入字符串可能才需要使用
//        dataOutputStream.writeUTF();
    }
}
