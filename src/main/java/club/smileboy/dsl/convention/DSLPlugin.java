package club.smileboy.dsl.convention;
/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 22:09
 * @description DSL插件
 * 可以使用在 某些查询引擎中的.{@link QueryEngine#handle(Object)}
 * 例如 {@link club.smileboy.dsl.materials.engine.DefaultQueryEngine}
 *
 * 前提调用Support判断是否支持处理,调用plugin,只要返回非空值,后面的插件不再使用
 **/
public interface DSLPlugin<T> {
    /**
     * 增强(一阶段)
     * @param target 目标对象
     * @return  返回增强对象
     */
    T plugin(Object target);

    /**
     * 是否支持
     * @param target target
     * @return true/ false
     */
    boolean support(Object target);
}
