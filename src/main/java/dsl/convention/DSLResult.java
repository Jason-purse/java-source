package dsl.convention;
/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 19:24
 * @description dsl 查询结果
 **/
public interface DSLResult {
    /**
     * 原始结果
     */
    Object getRawResult();

    /**
     * 获取逻辑结果(通过结果解析器转换的结果)
     */
    Object getLogicResult();
}
