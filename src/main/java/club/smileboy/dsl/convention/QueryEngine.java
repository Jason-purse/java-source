package club.smileboy.dsl.convention;
/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 19:23
 * @description 查询引擎
 **/
public interface QueryEngine {

    /**
     * 根据DSL 查询
     * @param query 查询条件
     * @return DSLResult
     */
    Object handle(Object query);
}
