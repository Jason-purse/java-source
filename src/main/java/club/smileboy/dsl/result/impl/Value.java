package club.smileboy.dsl.result.impl;

import club.smileboy.dsl.convention.ResultHandler;
/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 19:45
 * @description 返回单个值..
 **/
public class Value extends AbstractDSLResult implements ResultHandler<Value> {
    public Value(Object rawResult) {
        super(rawResult);
    }
    /**
     * 返回它自己...
     * @return
     */
    @Override
    public Value resolveResult() {
       return this;
    }

    public static Value of(Object rawResult) {
        return new Value(rawResult);
    }

}
