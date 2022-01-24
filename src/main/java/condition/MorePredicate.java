package condition;

import java.util.function.Supplier;

/**
 * @author JASONJ
 * @date 2022/1/24
 * @time 7:55
 * @description 多个else if 结合体..
 **/
public interface MorePredicate {
    // else - if
    MorePredicate ElseIf(Supplier<Boolean> predicate, Operation operation);

    void Else(Operation operation);

    static MorePredicate of(OncePredicate associatePredicate,Supplier<Boolean> predicate,Operation operation) {
        return new DefaultMorePredicateImpl(associatePredicate,predicate,operation);
    }
}

class DefaultMorePredicateImpl implements MorePredicate {
    // 条件标识 为true 表示已经完成 false 表示未完成..
    private Boolean conditionFinishFlag = Boolean.FALSE;

    private final OncePredicate associatePredicate;

    public DefaultMorePredicateImpl(OncePredicate associatePredicate,Supplier<Boolean> predicate,Operation operation) {
        if (!associatePredicate.getConditionFinishFlag()) {
            Boolean aBoolean = predicate.get();
            if (aBoolean != null && aBoolean) {
                this.conditionFinishFlag = true;
                operation.exec();
            }
        }
        this.associatePredicate = associatePredicate;
    }
    @Override
    public MorePredicate ElseIf(Supplier<Boolean> supplier, Operation operation) {
        // 执行..
        if(!this.conditionFinishFlag) {
            Boolean aBoolean = supplier.get();
            if(aBoolean != null && aBoolean) {
                this.conditionFinishFlag = true;
                operation.exec();
            }
        }
        return this;
    }

    @Override
    public void Else(Operation operation) {
        // 执行...
        // 如果前置 if 条件为true,那么这里的Else 必然不执行...
        if(!this.conditionFinishFlag && !this.associatePredicate.getConditionFinishFlag()) {
            operation.exec();
        }
    }
}
