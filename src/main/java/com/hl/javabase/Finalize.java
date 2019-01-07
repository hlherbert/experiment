package com.hl.javabase;

public class Finalize {

    // make SAVE_HOOK = this, can save this object from delete
    private static Finalize SAVE_HOOK = null;

    public void isAlive() {
        System.out.println("i am alive");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize executed. this method run only once.");
        SAVE_HOOK = this;
        System.out.println("save myself.");
    }

    public static void gc() {
        System.out.println("--------- gc -----------");
        System.gc();
    }

    public static void showAlive() {
        if (SAVE_HOOK != null) {
            SAVE_HOOK.isAlive();
        } else {
            System.out.println("i am dead");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Object superclass = " + Object.class.getSuperclass());

        SAVE_HOOK = new Finalize();
        SAVE_HOOK = null;
        gc();
        Thread.sleep(500); // finalize 优先级很低，暂停0.5秒等待他
        showAlive();

        SAVE_HOOK = null;
        gc();
        Thread.sleep(500);
        showAlive();

        String a = "abc";
        String b = new String("abc");
        String x = a.intern();
        String y = b.intern();
        System.out.println(x == y);
        System.out.println(a == b);
    }
}
