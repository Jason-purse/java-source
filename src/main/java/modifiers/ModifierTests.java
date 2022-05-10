package modifiers;

public class ModifierTests {

}

class A {
    private void  fun() {
        System.out.println("A");
    }

    protected void fun1() {
        System.out.println("A");
    }
}

class B  extends A {
    private void fun() {

    }

    @Override
    public void fun1() {
        super.fun1();
    }
}
