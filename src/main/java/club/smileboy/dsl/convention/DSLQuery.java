package club.smileboy.dsl.convention;

import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 20:21
 * @description DSL 查询抽象
 *
 * 由一系列规则组成.
 **/
public interface DSLQuery {
    /**
     * 获取表达式对象
     */
    Object getExpression();

    DSLQuery in(String field, List<?> values);
    DSLQuery in(String field,Object[] values);

    DSLQuery is(String field, Object value);

    DSLQuery exists(String field, boolean exists) ;

    DSLQuery notIs(String field, Object value);

    DSLQuery gt(String fieldName, Object value);

    DSLQuery gte(String field, Object value);

    DSLQuery lt(String field, Object value);

    DSLQuery lte(String field, Object value);

    DSLQuery like(String field, String value);

    DSLQuery page(int page, int size, Sort sort);

    DSLQuery page(int page, int size);

    DSLQuery page(int page, int size, Sort.Direction direction, String... fields);

    DSLQuery betweenAnd(String field,Object parameter1,Object parameter2);

    DSLQuery and();

    DSLQuery or();

    /**
     * 另一个条件取反...
     */
    DSLQuery nor();

    /**
     * 构建....
     * @param index 例如有些构建需要其余参数
     */
    DSLQuery build(Object index);

    /**
     * 设置index
     * @param index 指标
     * @return dslQuery
     */
    DSLQuery setIndex(String index);

}
