package dsl.convention.impl;

import dsl.convention.DSLMaterial;
import org.springframework.beans.MutablePropertyValues;
/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 22:13
 * @description 默认返回空属性
 **/
public class DefaultDSLMaterial implements DSLMaterial {
    @Override
    public MutablePropertyValues getMaterials() {
        return new MutablePropertyValues();
    }
}
