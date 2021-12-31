package club.smileboy.dsl.factory;

import club.smileboy.dsl.convention.DSLPair;
import club.smileboy.dsl.convention.DSLResult;
import club.smileboy.dsl.result.impl.DSLList;
import club.smileboy.dsl.result.impl.Empty;
import club.smileboy.dsl.result.impl.Map;
import club.smileboy.dsl.result.impl.Value;

import java.util.List;

/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 20:14
 * @description DSL RESULT 工厂
 **/
public class DSLResultFactory {

    private DSLResultFactory() {

    }

    /**
     * 根据一个目标对象创建DSLResult
     * @param target 目标对象.
     * @return DSLResult
     */
    public static DSLResult createDslResult(Object target) {
        if(target != null) {
            if(target.getClass().isPrimitive()) {
                return Value.of(target);
            }

            if(target.getClass().isArray() || target instanceof List) {
                return DSLList.of(target);
            }

            if(target  instanceof Map || target instanceof DSLPair) {
                return Map.of(target);
            }
            // 一个 普通数组
            return Value.of(target);
        }
        // empty
        return Empty.of();
    }
}
