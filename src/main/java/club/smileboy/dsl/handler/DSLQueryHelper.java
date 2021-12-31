package club.smileboy.dsl.handler;

import club.smileboy.dsl.convention.DSLQuery;
import club.smileboy.dsl.materials.query.impl.MongoQuery;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.data.domain.Sort;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 20:37
 * @description club.smileboy.dsl 查询帮助器
 **/
public class DSLQueryHelper implements DSLQuery {

    private static final boolean useMongoDSLEngine;

    private final DSLQuery dslQuery;

    public DSLQueryHelper() {
        // 否则无法使用
        if(useMongoDSLEngine) {
            dslQuery = new MongoQuery(null);
        }
        throw new RuntimeException("当前DSLQuery 无法建立,系统不支持,如需使用默认DSLQuery,请设置 'useMongoDSLEngine' 属性!");
    }

    public DSLQueryHelper(String dataType) {
        if(!"mongo".equals(dataType)) {
            throw new RuntimeException(String.format("当前系统不支持此 %s 的数据库查询!", dataType));
        }
        // 否则无法使用
        if(useMongoDSLEngine) {
            dslQuery = new MongoQuery(null);
            return ;
        }
        throw new RuntimeException("当前DSLQuery 无法建立,系统不支持,如需使用默认DSLQuery,请设置 'useMongoDSLEngine' 属性!");
    }

    /**
     * 包含关系
     */
    public static final String IN = "in";
    /**
     * 分页关键字
     */
    public static final String PAGE = "page";
    /**
     * 页码
     */
    public static final String OFFSET = "offset";
    /**
     * limit(每页多少条)
     */
    public static final String SIZE = "size";

    /**
     * 模糊匹配
     */
    public static final String LIKE ="like";

    /**
     * 等于
     */
    public static final String EQ = "is";

    /**
     * 大于
     */
    public static final String GT = "gt";

    /**
     * 大于等于
     */
    public static final String GTE = "gte";

    /**
     * 小于
     */
    public static final String LT = "lt";
    /**
     * 小于等于
     */
    public static final String LTE = "lte";

    /**
     * 不等于
     */
    public static final String NOT = "nis";

    /**
     * between and
     */
    public static final String BETWEEN_AND = "between_and";

    /**
     * 返回类型
     */
    public static final String RESULT_TYPE = "result_type";
    /**
     * 索引
     */
    public static final String INDEX = "index" ;


    private final static Map<String,Boolean> KEY_WORDS = new HashMap<>();


    public static DSLQueryHelper getInstance() {
        return new DSLQueryHelper();
    }

    public static DSLQueryHelper getInstance(String dataType) {
        return new DSLQueryHelper(dataType);
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

    public DSLQueryHelper in(String field,Object[] values) {
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

    public DSLQueryHelper betweenAnd(String field,Object parameter1,Object parameter2) {
        dslQuery.betweenAnd(field,parameter1,parameter2);
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
    public DSLQuery gt(String fieldName, Object value) {
        dslQuery.gt(fieldName,value);
        return this;
    }

    @Override
    public DSLQuery build(Object index) {
        return dslQuery.build(index);
    }

    public DSLQuery setIndex(String index) {
        return dslQuery.setIndex(index);
    }

    // 初始化目前系统支持的DSLQuery
    static  {
        if(System.getSecurityManager() != null) {
            useMongoDSLEngine = (boolean)AccessController.doPrivileged((PrivilegedAction<Object>) () -> Boolean.getBoolean("useMongoDSLEngine"));
        }
        else {
            useMongoDSLEngine = Boolean.getBoolean("useMongoDSLEngine");
        }

        KEY_WORDS.put(IN,Boolean.TRUE);
        KEY_WORDS.put(OFFSET,Boolean.TRUE);
        KEY_WORDS.put(SIZE,Boolean.TRUE);
        KEY_WORDS.put(LIKE,Boolean.TRUE);
        KEY_WORDS.put(EQ,Boolean.TRUE);
        KEY_WORDS.put(GT,Boolean.TRUE);
        KEY_WORDS.put(GTE,Boolean.TRUE);
        KEY_WORDS.put(LT,Boolean.TRUE);
        KEY_WORDS.put(LTE,Boolean.TRUE);
        KEY_WORDS.put(NOT,Boolean.TRUE);
    }

    /**
     * 根据获取关键字是否存在
     * @param keyword 关键字
     * @return 是否存在
     */
    public static Boolean findKeyWord(String keyword) {
       return  KEY_WORDS.getOrDefault(keyword,Boolean.FALSE);
    }

    // ------------------------------------------- 工具方法 -----------------------------------

    /**
     * 字符串条件添加
     * @param fieldName 字段名
     * @param value 条件
     * @return 添加结果
     */
    public DSLQueryHelper addStringCondition(String fieldName,String value) {
        // 尝试使用 "," 解析
        String[] split = value.toString().split(",");
        if(split.length > 1) {
            // 则是数组, in
            this.in(fieldName,split);
            return this;
        }
        // 尝试 " "解析
        String[] split1 = value.toString().split(" ");
        if(split1.length > 1) {
            this.in(fieldName,split1);
            return this;
        }
        // 可能是 is
        this.is(fieldName,value);
        return this;
    }

}
