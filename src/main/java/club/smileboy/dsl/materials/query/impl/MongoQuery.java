package club.smileboy.dsl.materials.query.impl;

import club.smileboy.dsl.constant.DSLResultType;
import club.smileboy.dsl.convention.DSLQuery;
import club.smileboy.dsl.materials.query.AbstractQuery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 20:39
 * @description 根据mongo query 实现mongo 查询
 **/
public class MongoQuery extends AbstractQuery {
    /**
     * 集合名称
     */
    private String collectionName;

    public MongoQuery(DSLResultType dslResultType) {
        super(dslResultType);
        // 自动初始化
        init();
    }

    public MongoQuery() {
        super();
        // 自动初始化
        init();
    }

    /**
     * query
     */
    private Query query;

    /**
     * temp 临时条件
     */
    private Criteria criteria;

    /**
     * 入栈..
     */
    private List<Criteria> array;

    private boolean isBuild =  false;
    @Override
    public Query getExpression() {
        return query;
    }

    public String getCollectionName() {
        return collectionName;
    }

    @Override
    public DSLQuery setIndex(String index) {
        this.collectionName = index;
        return this;
    }

    /**
     * 初始化查询(默认构造函数自动初始化)
     * 创建一个query对象
     * 如果重复初始化,则始终是一个新的对象
     */
    public void init() {
        this.isBuild = false;
        query = new Query();
        criteria = new Criteria();
        array = new ArrayList<>();
    }

    /**
     * 摧毁...
     * query = null
     */
    public void destroy() {
        query = null;
        criteria = null;
        array = null;
        this.isBuild = false;
    }

    public MongoQuery in(String field, List<?> values) {
        criteria.and(field).in(values);
        return this;
    }

    @Override
    public DSLQuery in(String field, Object[] values) {
        criteria.and(field).in(values);
        return this;
    }

    public MongoQuery is(String field, Object value) {
        criteria.and(field).is(value);
        return this;
    }

    public MongoQuery exists(String field,boolean exists) {
        criteria.and(field).exists(exists);
        return this;
    }

    public MongoQuery notIs(String field,Object value) {
        criteria.and(field).ne(value);
        return this;
    }


    @Override
    public DSLQuery gt(String fieldName, Object value) {
        this.criteria.and(fieldName).gt(value);
        return this;
    }

    public MongoQuery gte(String field,Object value) {
        criteria.and(field).gte(value);
        return this;
    }

    public MongoQuery lt(String field,Object value) {
        criteria.and(field).gte(value);
        return this;
    }

    public MongoQuery lte(String field,Object value) {
        criteria.and(field).lte(value);
        return this;
    }

    public MongoQuery like(String field,String value) {
        criteria.and(field).regex(value);
        return this;
    }
    public MongoQuery page(int page, int size, Sort sort) {
        PageRequest of = PageRequest.of(page, size, sort);
        query = query.with(of);
        return  this;
    }

    public MongoQuery page(int page, int size) {
        PageRequest of = PageRequest.of(page, size);
        query = query.with(of);
        return  this;
    }

    public MongoQuery page(int page, int size, Sort.Direction direction, String ... fields) {
        PageRequest of = PageRequest.of(page, size,direction,fields);
        query = query.with(of);
        return  this;
    }

    @Override
    public DSLQuery betweenAnd(String field, Object parameter1, Object parameter2) {
        criteria.and(field).gte(parameter1).lte(parameter2);
        return this;
    }

    public MongoQuery and() {
        Criteria criteria = new Criteria();
        this.criteria.andOperator(criteria);
        array.add(criteria);
        this.criteria = criteria;
        return this;
    }

    public MongoQuery or() {
        Criteria criteria = new Criteria();
        this.criteria.orOperator(criteria);
        array.add(criteria);
        this.criteria = criteria;
        return this;
    }

    /**
     * 另一个条件取反...
     */
    public MongoQuery nor() {
        Criteria criteria = new Criteria();
        this.criteria.norOperator(criteria);
        array.add(criteria);
        this.criteria = criteria;
        return this;
    }

    /**
     * 无法重复构建....
     * 可使用{@link #init} 初始化此MongoQuery
     */
    public MongoQuery build(Object index) {
        if(!isBuild) {
            if(index == null) {
                if(!StringUtils.hasText(collectionName)) {
                    throw new RuntimeException("当前底层由mongo支持,需要指定查询的集合(collection)名称!");
                }
            }
            // 存在 and / or /nor ... 等操作符的添加
            if(array.size() > 0) {
                query = this.query.addCriteria(array.get(0));
            }
            else {
                query.addCriteria(criteria);
            }
            this.criteria = null;
            this.array = null;
            if(!StringUtils.hasText(collectionName) && index != null) {
                this.collectionName = index.toString();
                if(!StringUtils.hasText(collectionName)) {
                    throw new RuntimeException("当前底层由mongo支持,需要指定查询的集合(collection)名称!");
                }
            }
            this.isBuild = true;
        }
        return this;
    }


}
