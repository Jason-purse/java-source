package club.smileboy.controller;

import club.smileboy.dsl.convention.QueryEngine;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/bucket/warehouse")
public class BucketController {

    private QueryEngine queryEngine;
    private MongoTemplate mongoTemplate;
    @RequestMapping(value = "query",produces = "application/json")
    public Object queryBy(HttpServletRequest request) {
        return queryEngine.handle(request);
    }


    @GetMapping
    public String query() {
        System.out.println(mongoTemplate.findAll(Map.class, "one"));
        return "123";
    }
}
