package dsl.convention;
/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 20:02
 * @description dsl 的 键值对 (被{@link dsl.result.impl.Map}可能使用的数据结果)
 **/
public interface DSLPair {
    /**
     * 获取key
     */
    Object getKey();

    /**
     * 获取value
     */
    Object getValue();
}
