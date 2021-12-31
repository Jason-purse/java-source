package club.smileboy.config;

import club.smileboy.dsl.convention.DSLPlugin;
import club.smileboy.dsl.convention.DSLQuery;
import club.smileboy.dsl.convention.impl.DSLQueryPlugin;
import club.smileboy.dsl.materials.query.impl.MongoQuery;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class DSLConfig {

    @Bean
    public DSLPlugin<? extends DSLQuery> dslQueryPlugin() {
        return new DSLQueryPlugin<MongoQuery>() {
            @Override
            public MongoQuery plugin(Object target) {
                System.out.println("plugin one phase!!!");
                return null;
            }

            @Override
            public boolean support(Object target) {
                return target instanceof HttpServletRequest;
            }
        };
    }

    @Bean
    public ServletQueryContext queryContext(MongoTemplate mongoTemplate) {
        return new ServletQueryContext(mongoTemplate);
    }
}
