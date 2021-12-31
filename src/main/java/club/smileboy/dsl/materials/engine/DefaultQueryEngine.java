package club.smileboy.dsl.materials.engine;

import club.smileboy.dsl.convention.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 21:44
 * @description 查询引擎默认实现
 **/
@Slf4j
public abstract class DefaultQueryEngine implements QueryEngine {

    public DefaultQueryEngine() {

    }
    @Override
    public Object handle(Object query) {
        DSLMaterial dslMaterial = parseMaterial(query);
        DSLQuery dslQuery = buildQuery(dslMaterial);
        DSLResult dslResult = internalHandle(dslQuery);
        return returnResult(dslResult);
    }


    // 解析输入
    protected abstract DSLMaterial parseMaterial(Object query);
    // 构建SQL
    protected abstract DSLQuery buildQuery(DSLMaterial material);

    // 查询结果
    protected abstract DSLResult internalHandle(DSLQuery query);
    // 分析结果
    protected abstract Object returnResult(DSLResult result);
}
