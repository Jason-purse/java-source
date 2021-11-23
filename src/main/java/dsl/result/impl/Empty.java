package dsl.result.impl;
/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 22:06
 * @description 空对象
 **/
public class Empty extends AbstractDSLResult {
    protected Empty(Object rawResult) {
        super( new Object());
    }
    protected Empty() {
        super(new Object());
    }

    public static Empty of() {
        return new Empty();
    }

    public static Empty of(Object target) {
        return new Empty(target);
    }
}
