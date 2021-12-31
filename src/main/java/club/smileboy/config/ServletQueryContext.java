package club.smileboy.config;

import club.smileboy.dsl.constant.DSLResultType;
import club.smileboy.dsl.convention.DSLQuery;
import club.smileboy.dsl.convention.DSLResult;
import club.smileboy.dsl.convention.QueryContext;
import club.smileboy.dsl.convention.impl.DefaultDSLPair;
import club.smileboy.dsl.materials.query.impl.MongoQuery;
import club.smileboy.dsl.result.impl.DSLList;
import club.smileboy.dsl.result.impl.Empty;
import club.smileboy.dsl.result.impl.Value;
import com.mongodb.MongoException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 直接默认绑定MongoQuery 查询体系
 */
public class ServletQueryContext implements QueryContext {
    /**
     * mongoTemplate
     */
    private final MongoTemplate mongoTemplate;

    private final Map<String,String> indexCollections;

    private final Executor executor = Executors.newFixedThreadPool(4);
    // 开启多个线程处理
    private final ExecutorCompletionService<Object> completionService = new ExecutorCompletionService<>(executor);

    public ServletQueryContext(MongoTemplate mongoTemplate) {
       try {
           this.mongoTemplate = Objects.requireNonNull(mongoTemplate);
       }catch (Exception e) {
           throw new RuntimeException("mongoTemplate 不能为空!");
       }
       indexCollections = new HashMap<>();
       indexCollections.put("one","one");
       indexCollections.put("two","two");
       indexCollections.put("three","three");
       indexCollections.put("business","business");
    }

    @Override
    public DSLResult invoke(DSLQuery query) {
        MongoQuery mongoQuery = (MongoQuery) query;
        DefaultDocumentCallbackHandler defaultDocumentCallbackHandler = new DefaultDocumentCallbackHandler();
        String[] tables = resolveIndex(mongoQuery);
        HashMap<String,Boolean> map = new HashMap<>();
        for (String table : tables) {
            String name = indexCollections.get(table);
            map.put(table,Boolean.FALSE);
            if (StringUtils.hasText(name)) {
                map.put(table,Boolean.TRUE);
                completionService.submit(() -> {
                    List<Map> maps = mongoTemplate.find(((MongoQuery) query).getExpression(), Map.class, name);
                    // 处理...
                    for (Map map1 : maps) {
                        defaultDocumentCallbackHandler.processDocument(map1);
                    }
                    // 获取结果
                    return defaultDocumentCallbackHandler.getResult();
                });
            }
        }
        Map<String,Object> results = new HashMap<>();
        map.forEach((k,v) -> {
            if(v) {
                try {
                    Object result = completionService.take().get();
                    results.put(k,result);
                }catch (InterruptedException | ExecutionException ex) {
                    // pass
                }
            }
            else {
                results.put(k,Empty.of());
            }
        });
        // 判断返回类型是什么...
        DSLResultType resultType = mongoQuery.getResultType();
        if(resultType != null) {
            switch (resultType) {
                case MAP:
                    return club.smileboy.dsl.result.impl.Map.of(results.entrySet().stream().map(entry -> new DefaultDSLPair(entry.getKey(),entry.getValue())).collect(Collectors.toList()));
                case LIST:
                    return DSLList.of(results.values());
                case VALUE:
                    return Value.of(results);
            }
        }
        // 判断是单个还是多个
        if (results.size() > 0) {
            if(results.size() == 1) {
                AtomicReference<club.smileboy.dsl.result.impl.Map> maped = new AtomicReference<>(club.smileboy.dsl.result.impl.Map.of(null));
                results.forEach((key,value) -> {
                    maped.set(club.smileboy.dsl.result.impl.Map.of(new DefaultDSLPair(key, value)));
                });
                return maped.get();
            }
            return DSLList.of(results);
        }
        return Empty.of();
    }

    protected String[] resolveIndex(MongoQuery mongoQuery) {
        String collectionName = mongoQuery.getCollectionName();
        String[] split = collectionName.split(",");
        if(split.length <= 1) {
            String[] s = collectionName.split(" ");
            if(s.length > 1) {
                // 开始查询多张表
                return s;
            }
        }
        // 否则返回第一种切割..
        return split;
    }

    static class DefaultDocumentCallbackHandler {
        /**
         * 默认原始数据
         */
        public final List<Object> rawResults = new ArrayList<>();

        public void processDocument(Map<?,?> document) throws MongoException, DataAccessException {
            // 只遍历一级属性
            rawResults.add(document);
        }

        public List<Object> getResult() {
            return rawResults;
        }
    }


}

