package club.smileboy.config;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice(basePackages = "club.smileboy.controller")
public class GenericResponseAdvice {
    @ExceptionHandler
    public Object resolveException(Exception e) {
        e.printStackTrace();
        HashMap<Object, Object> result = new HashMap<>();
        result.put("code",500);
        result.put("msg",e.getMessage());
        result.put("result",null);
        return result;
    }
}
