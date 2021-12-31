package club.smileboy.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletInputStream;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author JASONJ
 * @date 2021/12/17
 * @time 21:44
 * @description json 解析器
 **/
public class JSONUtil {
    private  final  static ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }

    public static String asJSON(Object target) {
        try {
            return objectMapper.writeValueAsString(target);
        }catch (Exception e) {
            // pass
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 读取input Stream
     * @param inputStream input stream
     * @return map
     */
    public static Map<?,?> readToMap(ServletInputStream inputStream) {
        try {
            return objectMapper.readValue(inputStream, new TypeReference<Map<?,?>>() {
            });
        }catch (Exception e)  {
            // pass
        }
        return null;
    }
}
