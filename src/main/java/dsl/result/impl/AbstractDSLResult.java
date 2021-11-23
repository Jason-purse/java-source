package dsl.result.impl;

import dsl.convention.DSLResult;

/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 19:30
 * @description 抽象DSL数据结果
 **/
public abstract class AbstractDSLResult implements DSLResult {
    /**
     * 原始数据结果
     */
    private final Object rawResult;

    protected AbstractDSLResult(Object rawResult) {
        this.rawResult = rawResult;
    }

    protected AbstractDSLResult() {
        this.rawResult = null;
    }

    @Override
    public Object getRawResult() {
        return rawResult;
    }

    @Override
    public Object getLogicResult() {
        return rawResult;
    }
}
