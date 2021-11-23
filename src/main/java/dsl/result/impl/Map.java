package dsl.result.impl;

import dsl.convention.DSLPair;
import dsl.convention.DSLResult;
import dsl.convention.ResultHandler;
import dsl.convention.impl.DefaultDSLPair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 19:51
 * @description 基于DSL结果的DSL map结果
 * map 天生可重复性处理
 **/
public class Map extends AbstractDSLResult implements ResultHandler<DSLResult> {
    /**
     * 数组才会用到的KEY
     */
    private static final String KEY ="LABEL";
    /**
     * k,v 键值对
     */
    private List<DSLPair> pairs;

    private final AtomicBoolean resolved = new AtomicBoolean(false);

    protected Map(Object rawResult) {
        super(rawResult);
    }

    protected Map() {
        super();
    }

    @Override
    public DSLResult resolveResult() {
        Object rawResult = getRawResult();
        if(rawResult != null) {
            pairs = new ArrayList<>();
            // 如果本身就是map,那么直接映射即可!!!
            if(rawResult instanceof java.util.Map<?,?>) {
                ((java.util.Map<?, ?>) rawResult).forEach((key,value) -> {
                    pairs.add(new DefaultDSLPair(key,value));
                });
            }
            // 表示一个数组类型
            else if(rawResult.getClass().isArray()) {
                // 除非它是Pair,否则直接包装(数组,扁平化)
                if(DSLPair.class.isAssignableFrom(rawResult.getClass().getComponentType())) {
                    DSLPair[] dslPairs = (DSLPair[]) rawResult;
                    pairs.addAll(Arrays.asList(dslPairs));
                }
                else {
                  // 作为普通类型
                    Object[] result = (Object[]) rawResult;
                    for (int i = 0; i < result.length; i++) {
                        pairs.add(new DefaultDSLPair(i,result[i]));
                    }
                }
            }
            // 单例
            else {
                pairs.add(new DefaultDSLPair(KEY,rawResult));
            }
        }
        return this;
    }

    @Override
    public Object getLogicResult() {
        // 如果还没有处理... 尝试处理
        if(resolved.compareAndSet(false,true)) {
            return resolveResult().getLogicResult();
        }
        // 返回一个空的...或者 pairs
        return  pairs != null ? pairs :  Empty.of().getLogicResult();
    }

    public static Map of(Object rawResult) {
        return new Map(rawResult);
    }
}
