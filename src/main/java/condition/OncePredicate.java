package condition;

import java.util.LinkedList;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author JASONJ
 * @date 2022/1/24
 * @time 7:21
 * @description 一次性条件
 **/
public interface OncePredicate {

    static <T> OncePredicate of(Supplier<Boolean> predicate, Operation operation) {
        return new DefaultOncePredicateImpl(predicate,operation);
    }

    OncePredicate If(Supplier<Boolean> predicate,Operation operation);

    void Else(Operation operation);

    MorePredicate ElseIf(Supplier<Boolean> predicate,Operation operation);

    // 获取条件完成标志
    Boolean getConditionFinishFlag();
}
// 单个条件的默认实现
class DefaultOncePredicateImpl implements OncePredicate {
    // 条件是否执行完毕..
    private Boolean conditionFinishFlag = Boolean.FALSE;

    public DefaultOncePredicateImpl(Supplier<Boolean> predicate,Operation operation) {
        Boolean aBoolean = predicate.get();
        if(aBoolean != null && aBoolean) {
            this.conditionFinishFlag = true;
            operation.exec();
        }
    }

    @Override
    public  OncePredicate If(Supplier<Boolean> predicate, Operation operation) {
        Boolean aBoolean = predicate.get();
        if(aBoolean != null && aBoolean) {
            this.conditionFinishFlag = true; // 可以重复设置(主要是为了给MorePredicate使用)
            operation.exec();
        }
        return this;
    }

    @Override
    public MorePredicate ElseIf(Supplier<Boolean> predicate, Operation operation) {
//        OperationSet last = this.conditions.getLast();
//        last.addNextPredicate(predicate,operation);
        return MorePredicate.of(this,predicate,operation);
    }

    @Override
    public Boolean getConditionFinishFlag() {
        return conditionFinishFlag;
    }

    @Override
    public void Else(Operation operation) {
        if(!conditionFinishFlag) {
            operation.exec();
        }
    }
}
