package club.smileboy.dsl.convention;
/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 20:20
 * @description 查询上下文...
 **/
public interface QueryContext {
    /**
     * 查询结果
     * @param query 查询条件
     * @return 查询结果
     */
    DSLResult invoke(DSLQuery query);
}
