package core.lang.asset;
/**
 * @author FLJ
 * @dateTime 2022/1/21 13:16
 * @description 断言
 */
public class AssetTests {
    public static void main(String[] args) {
//        assert 1 > 2; // 断言
        // 断言
        assert 1 > 2 : new RuntimeException(" 1 不可能大于 2!"); // 表达式后面的值将会传递给 AssetError
        // 断言使用的场景在于  明知它不可能是这个条件,仅仅只是抹去表达式IDE检测问题.

        // 使用需要添加vm 参数 -enableassertions 或 -ea
        // 还有一些断言细节 就不再多说,一般来说断言只存在开发测试阶段,生产环境不可能存在,仅仅是为了排除程序内部错误.. 例如Spring 直接启用条件判断ASSET断言工具类..
    }
}
