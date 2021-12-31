package club.smileboy.dsl.result.impl;
/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 22:06
 * @description 空对象
 **/
public class Empty extends AbstractDSLResult {

    protected Empty() {
        super(new Object());
    }

    public static Empty of() {
        return new Empty();
    }

}
