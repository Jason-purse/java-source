package club.smileboy.dsl.convention;

/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 19:26
 * @description {@link DSLResult} 结果处理器
 **/
public interface ResultHandler<T extends DSLResult> {
    /**
     * 解析结果,返回对应的{@link DSLResult}的实现子类
     * 返回可能是全新的,也可以是缓存的...
     */
    T resolveResult();
}
