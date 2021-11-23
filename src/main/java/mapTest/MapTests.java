package mapTest;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class MapTests {
    @Test
    public void test() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("k1","k2");
        System.out.println(map.values());
    }
}
