package dsl.materials.engine;

import dsl.convention.DSLMaterial;
import dsl.convention.DSLQuery;
import dsl.convention.DSLResult;
import dsl.convention.QueryEngine;

/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 21:44
 * @description 查询引擎默认实现
 **/
public abstract class DefaultQueryEngine implements QueryEngine {

    @Override
    public Object handle(Object query) {
        DSLMaterial dslMaterial = resolveQuery(query);
        DSLQuery dslQuery = buildQuery(dslMaterial);
        DSLResult dslResult = internalHandle(dslQuery);
        return returnResult(dslResult);
    }

    // 解析输入
    protected abstract DSLMaterial resolveQuery(Object query);
    // 构建SQL
    protected abstract DSLQuery buildQuery(DSLMaterial material);

    // 查询结果
    protected abstract DSLResult internalHandle(DSLQuery query);
    // 分析结果
    protected abstract Object returnResult(DSLResult result);
}
