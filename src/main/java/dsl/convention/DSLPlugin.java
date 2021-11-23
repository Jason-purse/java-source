package dsl.convention;
/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 22:09
 * @description DSL插件
 **/
public interface DSLPlugin<T> {
    /**
     * 增强
     * @param target 目标对象
     * @return  返回增强对象
     */
    T plugin(T target);
}
