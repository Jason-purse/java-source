package club.smileboy.dsl.convention.impl;

import club.smileboy.dsl.convention.DSLPlugin;
import club.smileboy.dsl.convention.DSLQuery;
/**
 * @author JASONJ
 * @date 2021/12/17
 * @time 20:52
 * @description 在解析查询参数解析完毕之后,增强...
 **/
public interface DSLQueryPlugin<T extends DSLQuery> extends DSLPlugin<T> {

}
