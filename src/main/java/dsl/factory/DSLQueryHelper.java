package dsl.factory;

import dsl.convention.DSLQuery;
import dsl.materials.query.impl.MongoQuery;
import org.springframework.data.domain.Sort;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 20:37
 * @description dsl 查询帮助器
 **/
public class DSLQueryHelper implements DSLQuery{

    private static final boolean useMongoDSLEngine;

    private final DSLQuery dslQuery;

    public DSLQueryHelper() {
        // 否则无法使用
        if(useMongoDSLEngine) {
            dslQuery = new MongoQuery(null);
        }
        throw new RuntimeException("当前DSLQuery 无法建立,系统不支持,如需使用默认DSLQuery,请设置 'useMongoDSLEngine' 属性!");
    }

    /**
     * 包含关系
     */
    public final String IN = "in";
    /**
     * 分页关键字
     */
    public final String PAGE = "limit";
    /**
     * 页码
     */
    public final String OFFSET = "offset";
    /**
     * limit(每页多少条)
     */
    public final String SIZE = "size";

    /**
     * 模糊匹配
     */
    public final String LIKE ="like";

    /**
     * 等于
     */
    public final String EQ = "is";

    /**
     * 大于
     */
    public final String GT = "gt";

    /**
     * 大于等于
     */
    public final String GTE = "gte";

    /**
     * 小于
     */
    public final String LT = "lt";
    /**
     * 小于等于
     */
    public final String LTE = "lte";

    /**
     * 不等于
     */
    public final String NOT = "not is";

    /**
     * between and
     */
    public final String BETWEEN_AND = "between_and";

    /**
     * 返回类型
     */
    public final String RESULT_TYPE = "result_type";


    public static DSLQueryHelper getInstance() {
        return new DSLQueryHelper();
    }

    // 表达式
    @Override
    public Object getExpression() {
        return dslQuery;
    }

    @Override
    public DSLQueryHelper in(String field, List<?> values) {
        dslQuery.in(field,values);
        return this;
    }

    @Override
    public DSLQueryHelper is(String field, Object value) {
        dslQuery.is(field,value);
        return this;
    }

    @Override
    public DSLQueryHelper exists(String field, boolean exists) {
        dslQuery.exists(field,exists);
        return this;
    }

    @Override
    public DSLQueryHelper notIs(String field, Object value) {
        dslQuery.notIs(field,value);
        return this;
    }

    @Override
    public DSLQueryHelper ge(String field, Object value) {
        dslQuery.ge(field,value);
        return this;
    }

    @Override
    public DSLQueryHelper gte(String field, Object value) {
        dslQuery.gte(field,value);
        return this;
    }

    @Override
    public DSLQueryHelper lt(String field, Object value) {
        dslQuery.lt(field,value);
        return this;
    }

    @Override
    public DSLQueryHelper lte(String field, Object value) {
        dslQuery.lte(field,value);
        return this;
    }

    @Override
    public DSLQueryHelper like(String field, String value) {
        dslQuery.like(field,value);
        return this;
    }

    @Override
    public DSLQueryHelper page(int page, int size, Sort sort) {
        dslQuery.page(page,size,sort);
        return this;
    }

    @Override
    public DSLQueryHelper page(int page, int size) {
        dslQuery.page(page,size);
        return this;
    }

    @Override
    public DSLQueryHelper page(int page, int size, Sort.Direction direction, String... fields) {
        dslQuery.page(page,size,direction,fields);
        return this;
    }

    @Override
    public DSLQueryHelper and() {
        dslQuery.and();
        return this;
    }

    @Override
    public DSLQueryHelper or() {
        dslQuery.or();
        return this;
    }

    @Override
    public DSLQueryHelper nor() {
        dslQuery.nor();
        return this;
    }

    @Override
    public DSLQuery build(Object args) {
        return dslQuery.build(args);
    }

    // 初始化目前系统支持的DSLQuery
    static  {
        if(System.getSecurityManager() != null) {
            useMongoDSLEngine = (boolean)AccessController.doPrivileged((PrivilegedAction<Object>) () -> Boolean.getBoolean("useMongoDSLEngine"));
        }
        else {
            useMongoDSLEngine = Boolean.getBoolean("useMongoDSLEngine");
        }
    }

}
