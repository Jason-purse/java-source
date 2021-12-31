package club.smileboy.dsl.convention.impl;

import club.smileboy.dsl.convention.DSLPair;
/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 20:05
 * @description 默认的DSL Pair
 **/
public class DefaultDSLPair implements DSLPair {
    private Object key;

    private Object value;
    @Override
    public Object getKey() {
        return key;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public DefaultDSLPair(Object key,Object value) {
        this.key = key;
        this.value = value;
    }

    public DefaultDSLPair() {

    }
}
