package dsl.result.impl;

import dsl.convention.ResultHandler;
/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 19:45
 * @description 返回单个值..
 **/
public class Value extends DSLList implements ResultHandler<Value> {
    /**
     * 返回它自己...
     * @return
     */
    @Override
    public Value resolveResult() {
        DSLList dslList = super.resolveResult();
        //直接拿取第一个...
        return get(0);
    }
}
