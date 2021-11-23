package dsl.materials.engine;

import com.mongodb.MongoException;
import dsl.convention.DSLMaterial;
import dsl.convention.DSLQuery;
import dsl.convention.DSLResult;
import dsl.convention.QueryContext;
import dsl.convention.impl.DefaultDSLPair;
import dsl.materials.query.impl.MongoQuery;
import dsl.result.impl.DSLList;
import dsl.result.impl.Empty;
import org.bson.Document;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 21:57
 * @description 基于Servlet 的查询引擎
 **/
@Configuration
public class ServletQueryEngine extends DefaultQueryEngine {

    private final QueryContext queryContext;
    /**
     * 方法参数解析器...
     */
    private HandlerMethodArgumentResolverComposite methodArgumentResolver;

    public ServletQueryEngine(
            HandlerMethodArgumentResolverComposite resolvers,
            MongoTemplate mongoTemplate) {
        this.methodArgumentResolver = resolvers;
        queryContext = new ServletQueryContext(mongoTemplate);
    }

    @Override
    protected DSLMaterial resolveQuery(Object query) {
        if (query instanceof HttpServletRequest) {

        }
        return null;
    }

    @Override
    protected DSLQuery buildQuery(DSLMaterial material) {
        return null;
    }

    @Override
    protected DSLResult internalHandle(DSLQuery query) {
        return null;
    }

    @Override
    protected Object returnResult(DSLResult result) {
        if(result != null) {
            return result.getLogicResult();
        }
        return new Object();
    }
}

/**
 * 直接默认绑定MongoQuery 查询体系
 */
class ServletQueryContext implements QueryContext {
    /**
     * mongoTemplate
     */
    private final MongoTemplate mongoTemplate;

    public ServletQueryContext(MongoTemplate mongoTemplate) {
       try {
           this.mongoTemplate = Objects.requireNonNull(mongoTemplate);
       }catch (Exception e) {
           throw new RuntimeException("mongoTemplate 不能为空!");
       }
    }

    @Override
    public DSLResult invoke(DSLQuery query) {
        MongoQuery mongoQuery = (MongoQuery) query;
        DefaultDocumentCallbackHandler defaultDocumentCallbackHandler = new DefaultDocumentCallbackHandler();
        mongoTemplate.executeQuery(((MongoQuery) query).getExpression(),mongoQuery.getCollectionName(),defaultDocumentCallbackHandler);
        return defaultDocumentCallbackHandler.getResult();
    }

    static class DefaultDocumentCallbackHandler implements DocumentCallbackHandler {
        /**
         * 默认原始数据
         */
        public final List<Object> rawResults = new ArrayList<>();
        @Override
        public void processDocument(Document document) throws MongoException, DataAccessException {
            // 只遍历一级属性
            document.forEach((key,value) -> {
                rawResults.add(new DefaultDSLPair(key,value));
            });
        }

        public DSLResult getResult() {
            return rawResults.size() == 0 ? Empty.of() : DSLList.of(rawResults);
        }
    }
}


