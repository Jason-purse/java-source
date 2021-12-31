package club.smileboy.dsl.materials.query;

import club.smileboy.dsl.constant.DSLResultType;
import club.smileboy.dsl.convention.DSLQuery;

/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 20:30
 * @description 对应的条件查询产生对应的DSL结果
 **/
public abstract class AbstractQuery implements DSLQuery {
    /**
     * club.smileboy.dsl 返回类型..
     */
    private DSLResultType resultType;

    public AbstractQuery(DSLResultType dslResultType) {
        this.resultType = dslResultType;
    }

    public AbstractQuery() {

    }
    /**
     * 获取返回类型
     */
    public  DSLResultType getResultType() {
        return resultType;
    }

    public void setResultType(DSLResultType resultType) {
        this.resultType = resultType;
    }
}
