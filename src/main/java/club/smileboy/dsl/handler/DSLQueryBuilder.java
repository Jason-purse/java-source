package club.smileboy.dsl.handler;

import club.smileboy.dsl.convention.DSLMaterial;
import club.smileboy.dsl.convention.DSLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.util.StringUtils;

import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Map;

/**
 * @author JASONJ
 * @date 2021/12/17
 * @time 22:14
 * @description 用于解析DSLMaterial 用来构建DSLQuery
 **/
@Slf4j
public class DSLQueryBuilder {
    /**
     * 查询数据库类型
     */
    private final static String TYPE = "TYPE";
    /**
     * 是否智能处理(默认true)
     */
    private boolean isSmart = true;

    /**
     * yyyy-mm-dd hh:mm:ss 格式
     */
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public DSLQueryBuilder() {

    }
    public DSLQueryBuilder(boolean isSmart) {
        this.isSmart = isSmart;
    }
    /**
     * 解析material 产生一个DSLQuery
     * @param material material
     * @return DSLQuery
     */
    public DSLQuery resolveMaterial(DSLMaterial material) {
        if(material != null) {
            MutablePropertyValues materials = material.getMaterials();
            Object dataType = materials.get(TYPE);
            if(!(dataType instanceof String)) {
                throw new RuntimeException("请指明查询数据库类型TYPE");
            }
            DSLQueryHelper instance = DSLQueryHelper.getInstance(dataType.toString());

            // 执行标准动作..
            for (PropertyValue propertyValue : materials) {
                String fieldName = propertyValue.getName();
                // 不需要再解析
                if(DSLQueryBuilder.TYPE.equals(fieldName)) {
                    continue;
                }
                Object value = propertyValue.getValue();
                // in 包含的关系...
                if(value != null) {
                    // 判断表名
                    if(DSLQueryHelper.INDEX.equals(fieldName)) {
                        // 获取
                        if(!(value instanceof String)) {
                            throw new RuntimeException("index use syntax is -> index: '1,2,3,4....'");
                        }
                        instance.setIndex(value.toString());
                        continue;
                    }
                    // 可能是等于 is / in
                    // 如果value = "" 不需要处理
                    if (value instanceof String && StringUtils.hasText(value.toString())) {
                       instance.addStringCondition(fieldName,value.toString());
                       // 只是普通情况(没有使用对象操作符)
                        // continue;
                    }
                    // 如果它是一个Map,标识对象操作符...
                    else if(value instanceof Map) {
                        MutablePropertyValues propertyValues = new MutablePropertyValues(((Map<?, ?>) value));
                        for (PropertyValue propertyValue1 : propertyValues) {
                            String name = propertyValue1.getName();
                            // 它可能使用了关键字进行标识, 如果是
                            if (DSLQueryHelper.findKeyWord(name)) {
                                // 可能是字符串
                                Object value1 = propertyValue1.getValue();
                                if (value1 != null) {
                                    // 判断非空
                                    if (value1 instanceof String) {
                                        if (!StringUtils.hasText(value1.toString())) {
                                            continue;
                                        }
                                    }
                                    switch (name) {
                                        case DSLQueryHelper.IN:
                                            if (value1 instanceof String) {
                                                // 智能处理String
                                                if (isSmart) {
                                                    instance.addStringCondition(fieldName, value1.toString());
                                                } else {
                                                    instance.in(fieldName, new Object[]{value1});
                                                }
                                            }
                                            // 否则 判断是否为数组
                                            else {
                                                if (value1.getClass().isArray()) {
                                                    instance.in(fieldName, ((Object[]) value1));
                                                } else if (value1 instanceof List<?>) {
                                                    instance.in(fieldName, ((List<?>) value1));
                                                }
                                                // 排除基础数据
                                                else if (value1.getClass().isPrimitive()) {
                                                    instance.is(fieldName, value1);
                                                }
                                            }
                                            break;
                                        // ==,如果是一个对象,那么不设置
                                        case DSLQueryHelper.EQ:
                                            // 仅仅是基础数据才进行设置
                                            if (value1 instanceof String) {
                                                if (isSmart) {
                                                    instance.addStringCondition(fieldName, value1.toString());
                                                } else {
                                                    instance.is(fieldName, value1);
                                                }
                                            }
                                            // ELSE  省略...
                                            break;
                                        // between ... and ..
                                        case DSLQueryHelper.BETWEEN_AND:

                                            // 使用 “.." 解析
                                            if (value1 instanceof String) {
                                                // 如果是时间...
                                                String[] split = ((String) value1).split(" .. ");
                                                if (split.length == 2) {
                                                    // 如果智能,判断是否是时间
                                                    if (isSmart) {
                                                        try {
                                                            // 本身就是 yyyy-MM-dd hh: mm: ss,不需要转换...
                                                            dateFormatter.parse(split[0]);
                                                            // 如果能正确解析,没必要format
                                                        } catch (DateTimeParseException e) {
                                                            // pass
                                                            // 尝试ISO (仅仅可ISO）
                                                            try {
                                                                TemporalAccessor parse = DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(split[0]);
                                                                TemporalAccessor parse1 = DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(split[1]);
                                                                // 尝试使用yyyy-MM-dd HH:mm:ss 解析
                                                                instance.betweenAnd(fieldName, dateFormatter.format(parse), dateFormatter.format(parse1));
                                                                break;
                                                            } catch (DateTimeException ex) {
                                                                // 普通数据
                                                            }
                                                        }
                                                    }
                                                    // 没办法了,直接传入比较(例如yyyy-MM-dd HH:mm:ss 原始数据)
                                                    instance.betweenAnd(fieldName, split[0], split[1]);
                                                } else {
                                                    throw new RuntimeException("%s required two parameters,format is parameter1 .. parameter2");
                                                }
                                            }
                                            break;
                                        case DSLQueryHelper.GT:
                                            // 标识
                                            instance.gt(fieldName, value1);
                                            break;
                                        case DSLQueryHelper.GTE:
                                            instance.gte(fieldName, value1);
                                            break;
                                        case DSLQueryHelper.LT:
                                            instance.lt(fieldName, value1);
                                            break;
                                        case DSLQueryHelper.LTE:
                                            instance.lte(fieldName, value1);
                                            break;
                                        case DSLQueryHelper.LIKE:
                                            instance.like(fieldName, value1.toString());
                                            break;
                                        case DSLQueryHelper.NOT:
                                            instance.notIs(fieldName, value1);
                                            break;
                                        default:
                                            // 无法处理,日志记录
                                            log.info("can't handle query partition, field: {}, condition: {} value: {}!!!!", fieldName, name, value1);
                                            break;
                                    }
                                }
                                break;
                            }
                        }

                    }
                }
            }
            return instance.build(null);
        }
        return null;
    }


    // 解析格式
    // TYPE  (mongo)// FX(指标Code) // payload // version

//    "{schoolId: "123"}" => is
//    "{schoolId: {in: ""}" => in
}
