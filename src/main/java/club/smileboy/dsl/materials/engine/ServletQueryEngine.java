package club.smileboy.dsl.materials.engine;

import club.smileboy.dsl.convention.*;
import club.smileboy.dsl.convention.impl.DefaultDSLMaterial;
import club.smileboy.dsl.handler.DSLQueryBuilder;
import club.smileboy.dsl.result.impl.Empty;
import club.smileboy.util.JSONUtil;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.stereotype.Service;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 21:57
 * @description 基于Servlet 的查询引擎
 **/
@Service
public class ServletQueryEngine extends DefaultQueryEngine {

    private final QueryContext queryContext;

    private List<DSLPlugin<?>> plugins;

    public ServletQueryEngine(QueryContext queryContext,Optional<List<DSLPlugin<?>>> plugins) {
        plugins.ifPresent(dslPlugins -> this.plugins = dslPlugins);
        this.queryContext = queryContext;
    }

    /**
     * 执行plugins
     * @param target 目标对象
     * @return 增强结果..
     */
    protected Object invokePlugins(Object target) {
        if (plugins != null && plugins.size() > 0) {
            Object result = null;
            for (DSLPlugin<?> plugin : plugins) {
                if (plugin.support(target)) {
                    result = plugin.plugin(target);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return target;
    }

    @Override
    protected DSLMaterial parseMaterial(Object query) {
        // 如果之前的解析不为空
        if (query instanceof HttpServletRequest) {
            HttpServletRequest query1 = (HttpServletRequest) query;
            MutablePropertyValues propertyValues = new MutablePropertyValues();
            // 解析过程
            try {
                ServletInputStream inputStream = query1.getInputStream();
                Map<?, ?> map = JSONUtil.readToMap(inputStream);
                if(map != null) {
                    propertyValues = new MutablePropertyValues(map);
                }
            }catch (Exception e) {
                // pass
                e.printStackTrace();
            }
            Object plugin_o = invokePlugins(new DefaultDSLMaterial(propertyValues));
            if(plugin_o instanceof DSLMaterial) {
                return (DSLMaterial) plugin_o;
            }
        }
        throw new RuntimeException(String.format("can't parse material from %s", query));
    }

    @Override
    protected DSLQuery buildQuery(DSLMaterial material) {
        DSLQuery dslQuery = null;
        if(material != null) {
            // 处理...
            dslQuery = new DSLQueryBuilder().resolveMaterial(material);
        }
        Object result = invokePlugins(dslQuery);
        if(result instanceof DSLQuery) {
            return ((DSLQuery) result);
        }
        throw new RuntimeException(String.format("can't build query from %s", material));
    }

    @Override
    protected DSLResult internalHandle(DSLQuery query) {
        if(query != null) {
            DSLResult invoke = queryContext.invoke(query);
            Object result = invokePlugins(invoke);
            if(result instanceof DSLResult) {
                return ((DSLResult) result);
            }
        }
        throw new RuntimeException(String.format("can't internal handle from %s", query));
    }

    @Override
    protected Object returnResult(DSLResult result) {
        if(result != null) {
            return result.getLogicResult();
        }
        return Empty.of().getLogicResult();
    }
}

