package club.smileboy.config;

import club.smileboy.dsl.convention.DSLResult;
import club.smileboy.dsl.result.impl.DSLList;
import club.smileboy.util.JSONUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new HttpMessageConverter<Object>() {
            @Override
            public boolean canRead(Class<?> clazz, MediaType mediaType) {
                return false;
            }

            @Override
            public boolean canWrite(Class<?> clazz, MediaType mediaType) {
                return true;
            }

            @Override
            public List<MediaType> getSupportedMediaTypes() {
                return List.of(MediaType.APPLICATION_JSON);
            }

            @Override
            public Object read(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
                return null;
            }

            @Override
            public void write(Object o, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
               if(o instanceof DSLResult) {
                   DSLResult o1 = (DSLResult) o;
                   outputMessage.getBody().write(JSONUtil.asJSON(o1.getLogicResult()).getBytes());
               }else {
                   outputMessage.getBody().write("{}".getBytes());
               }
            }
        });
    }
}
