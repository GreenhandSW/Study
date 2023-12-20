package design;

public class Facade {
    static class Pen{
        public void startStroke(){
            System.out.println("打开笔帽");
        }
        public void endStroke(){
            System.out.println("合上笔帽");
        }
    }
    static class Book{
        public void open(){
            System.out.println("翻开作业本");
        }
        public void close(){
            System.out.println("合上作业本");
        }
    }
    static class Homework{
        private final Pen pen=new Pen();
        private final Book book=new Book();
        public void startFinishing(){
            book.open();
            pen.startStroke();
            System.out.println("开始做作业");
        }
        public void endFinishing(){
            book.close();
            pen.endStroke();
            System.out.println("作业做完了");
        }
    }
    public static void main(String[] args) {
        Homework homework=new Homework();
        homework.startFinishing();
        System.out.println("\n正在做作业\n");
        homework.endFinishing();
    }
}
