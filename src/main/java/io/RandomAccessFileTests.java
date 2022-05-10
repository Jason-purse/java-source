package io;

import org.junit.jupiter.api.Test;

public class RandomAccessFileTests {
    @Test
    public void test() {
        // 随机访问
        // 如果打开已有文件,则不会删除它 ..
        // 规则的数据块..
        // seek 可以调整文件指针
        //getFilePointer 可以获取文件指针位置 ..
        // writeFixedString 可以写入固定数量的字符串 . (如果字符串过少,补充0)
        // readFixedString 同样读取,如果遇到0字符  跳过 ...

        // 此文件还有同步模式
//        rws 对数据和元数据进行同步
        // rwd 只对数据进行同步
    }
}
