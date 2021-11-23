package dsl.convention;

import dsl.materials.query.impl.MongoQuery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

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

    DSLQuery is(String field, Object value);

    DSLQuery exists(String field, boolean exists) ;

    DSLQuery notIs(String field, Object value);

    /**
     * 大于
     * @param field 字段名
     * @param value 条件
     * @return this
     */
    DSLQuery ge(String field, Object value);

    DSLQuery gte(String field, Object value);

    DSLQuery lt(String field, Object value);

    DSLQuery lte(String field, Object value);

    DSLQuery like(String field, String value);

    DSLQuery page(int page, int size, Sort sort);

    DSLQuery page(int page, int size);

    DSLQuery page(int page, int size, Sort.Direction direction, String... fields);

    DSLQuery and();

    DSLQuery or();

    /**
     * 另一个条件取反...
     */
    DSLQuery nor();

    /**
     * 构建....
     * @param args 例如有些构建需要其余参数
     */
    DSLQuery build(Object args);
}
