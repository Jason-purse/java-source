package club.smileboy.dsl.convention;

import org.springframework.beans.MutablePropertyValues;

/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 21:49
 * @description DSL 查询材料
 **/
public interface DSLMaterial {
    /**
     * 获取所有查询材料
     */
    MutablePropertyValues getMaterials();
}
