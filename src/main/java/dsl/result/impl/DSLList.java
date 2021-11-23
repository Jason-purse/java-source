package dsl.result.impl;

import dsl.convention.ResultHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 19:31
 * @description 基于原始DSL结果的数组DSL结果
 **/
public class DSLList extends AbstractDSLResult implements ResultHandler<DSLList> {
    /**
     * 目标真实结果
     */
    private List<?> result;
    /**
     * 表示没有解析过
     */
    private final AtomicBoolean resolved = new AtomicBoolean(false);

    public DSLList(Object rawResult) {
        super(rawResult);
    }

    public DSLList() {
        super();
    }

    @Override
    public DSLList resolveResult() {
        // 可能没有解析过,尝试解析
        if(resolved.compareAndSet(false,true)) {
            Object rawResult = getRawResult();
            if(rawResult != null) {
                // 如果是数组,直接解析
                if(rawResult instanceof List<?>) {
                    result = (List<?>) rawResult;
                }
                // 表示数组
                else if(rawResult.getClass().isArray()) {
                    Object[] rawResult1 = (Object[]) rawResult;
                    result = Arrays.asList(rawResult1);
                }
                // 单个值...
                else {
                    result = List.of(rawResult);
                }
            }
        }
        // 返回一个新的!!!!
        return new DSLList(Collections.unmodifiableList(result == null ? Collections.emptyList() : result));
    }

    /**
     * 根据index 拿取其中的一个值
     * 如果没有解析,那么默认值是null
     * @param index 索引 只能为正索引
     * @return 值
     * 如果拿取不到,数组越界,返回null,如果修改值,可能会对原始数据造成影响...
     */
    public Object get(int index) {
        // 如果解析过后..
        if(resolved.compareAndSet(true,true)) {
            // 表示能过获取
            if (result.size() > 0 && result.size() > index && index >= 0) {
                return result.get(index);
            }
        }

        return null;
    }

    @Override
    public Object getLogicResult() {
        return result == null ? Collections.emptyList() : result;
    }

    public static DSLList of(Object rawResult) {
        return new DSLList(rawResult);
    }
}
