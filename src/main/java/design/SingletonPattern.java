package design;

public class SingletonPattern {
    // 枚举实现单例
//    public static enum Singleton{
//        INSTANCE;
//        public void showNo(){
//            Integer no=1;
//            System.out.printf("I'm the no.%d instance\n", no);
//        }
//    }
//    public static class Main{
//        public static void main(String[] args) {
//            Singleton instance=Singleton.INSTANCE;
//            instance.showNo();
//        }
//    }
    public static class Singleton {
        private static Integer no = 0;
        private static volatile Singleton instance;

        private Singleton() {
            no++;
            System.out.print("初始化\n");
        }

        public void showNo() {
            System.out.printf("I'm the no.%d instance\n", no);
        }
        // 线程不安全
//        public static Singleton getInstance(){
//            if(instance==null){
//                instance=new Singleton();
//            }
//            return instance;
//        }

        // 线程安全：懒汉
//        public static synchronized Singleton getInstance(){
//            if(instance==null){
//                instance=new Singleton();
//            }
//            return instance;
//        }

        // 线程安全：双重检查锁
//        public static Singleton getInstance(){
//            if(instance==null){
//                synchronized (Singleton.class){
//                    if(instance==null){
//                        instance=new Singleton();
//                    }
//                }
//            }
//            return instance;
//        }
        // 线程安全：静态内部类（也是懒汉式加载）
        public static class SingletonInstance {
            private static final Singleton singleton = new Singleton();
        }

        public static Singleton getInstance() {
            return SingletonInstance.singleton;
        }
    }

    public static class Main {
        public static class SingletonTest implements Runnable {
            @Override
            public void run() {
                Singleton singleton = Singleton.getInstance();
                singleton.showNo();
            }
        }

        public static void main(String[] args) {
            Thread[] threads = new Thread[100];
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(new SingletonTest());
                threads[i].start();
            }
        }
    }
}
