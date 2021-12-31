package club.smileboy.dsl.convention.impl;

import club.smileboy.dsl.convention.DSLMaterial;
import org.springframework.beans.MutablePropertyValues;
/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 22:13
 * @description 默认返回空属性
 **/
public class DefaultDSLMaterial implements DSLMaterial {
    private final MutablePropertyValues propertyValues;
    public DefaultDSLMaterial(MutablePropertyValues mutablePropertyValues) {
        this.propertyValues = mutablePropertyValues;
    }
    @Override
    public MutablePropertyValues getMaterials() {
        return propertyValues;
    }
}
