package condition;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author JASONJ
 * @date 2022/1/24
 * @time 7:21
 * @description when 帮助器..
 **/
public class WhenHelper {

    // if (根据它  引导出if/else  / if/elseif/else)
    public  static  <T> OncePredicate If(Supplier<Boolean> predicate, Operation operation) {
        return OncePredicate.of(predicate,operation);
    }
}
