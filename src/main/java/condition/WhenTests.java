package condition;

public class WhenTests {
    public static void main(String[] args) {
        WhenHelper.If(() -> true, () -> {
                    System.out.println("默认就是true");
                })
                .If(() -> true,
                        () -> {
                            System.out.println("百度一下!");
                        }
                )
                .ElseIf(() -> Boolean.TRUE,
                        () -> {
                            System.out.println("无法处理..");
                        })
                .Else(() -> {
                    System.out.println("条件为假!!!");
                });


        WhenHelper.If(() -> false, () -> {
                    System.out.println("默认就是true");
                })
                .If(() -> false,
                        () -> {
                            System.out.println("百度一下!");
                        }
                )
                .ElseIf(() -> Boolean.TRUE,
                        () -> {
                            System.out.println("能够处理...");
                        })
                .ElseIf(() -> Boolean.TRUE, () -> {
                    System.out.println("无法执行,仅有一个能够执行!!!");
                })
                .Else(() -> {
                    System.out.println("条件为假!!!");
                });
    }
}
