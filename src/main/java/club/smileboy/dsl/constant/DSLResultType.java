package club.smileboy.dsl.constant;
/**
 * @author JASONJ
 * @date 2021/12/16
 * @time 20:16
 * @description DSL结果类型
 **/
public enum DSLResultType {
    /**
     * list(列表)
     */
    LIST("LIST"),
    /**
     * value(单值)
     */
    VALUE("VALUE"),
    /**
     * map(pairs)
     */
    MAP("PAIRS"),

    /**
     * 空
     */
    EMPTY("EMPTY");

    private final String type;
    DSLResultType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
