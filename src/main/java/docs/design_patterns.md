<div style="text-align: center;font-size: larger">设计模式</div>

# 工厂模式(Factory)

场景：需要获取不同的产品

主要实现逻辑：提供一个创建对象的接口，但把具体的对象创建延迟到子类中。客户端直接通过工厂方法创建对象而不需要知道其具体的类。

优点：将客户端和具体对象创建解耦。

缺点：工厂类和产品类的层次结构复杂，增加系统中类的数量。

方案：
1. 创建一个产品类接口
2. 所有的产品都实现该接口的方法。
3. 创建一个工厂抽象类
4. 每一个产品实现一个具体工厂类。
5. 使用的时候通过`SuperFactory s=new SubFactory()`的方式创建一个具体的工厂类，然后调用`s.create()`构造一个对应的具体产品。
```java
public class Factory {
    public interface Shape {
        void draw();
    }

    public static class Circle implements Shape{
        @Override
        public void draw() {
            System.out.println("Circle");
        }
    }

    public static class Rectangle implements Shape{
        @Override
        public void draw() {
            System.out.println("Rectangle");
        }
    }

    public static abstract class ShapeFactory{
        abstract Shape createShape();
    }

    public static class CircleFactory extends ShapeFactory{

        @Override
        Shape createShape() {
            return new Circle();
        }
    }

    public static class RectangleFactory extends ShapeFactory{

        @Override
        Shape createShape() {
            return new Rectangle();
        }
    }

    public static void main(String[] args) {
        ShapeFactory circleFactory=new CircleFactory();
        Shape circle=circleFactory.createShape();
        circle.draw();

        ShapeFactory rectangleFactory=new RectangleFactory();
        Shape rectangle=rectangleFactory.createShape();
        rectangle.draw();
    }
}
```

# 抽象工厂模式(Abstract Factory)

场景：需要创建A、B产品和X、Y产品，其中AB属于一个系列，XY属于一个系列。A和X相关，B和Y相关。（比如Java虚拟机有Win虚拟机和Linux虚拟机，OS有Win和Linux，现在需要获取Java虚拟机以及其运行系统，则需要创建抽象工厂类，包含获取虚拟机和OS两个方法。WinJavaFactory实现该类，获取Win系统和Win虚拟机，LinuxJavaFactory实现该类，获取Linux系统和Linux虚拟机）

方案：
1. 创建各系列产品对应的接口
2. 实现具体产品类
3. 创建一个抽象工厂类，包含不同系列产品
4. 实现不同的具体工厂类，实现获取不同系列产品的方法。
```java
public class AbstractFactory {
    public interface Pen{
        void stroke();
    }

    public static class Pencil implements Pen{
        @Override
        public void stroke(){
            System.out.println("Will draw with Pencil");
        }
    }

    public static class Marker implements Pen{
        @Override
        public void stroke(){
            System.out.println("Will draw with Marker");
        }
    }

    public interface Shape {
        void draw();
    }

    public static class Circle implements Shape{
        @Override
        public void draw() {
            System.out.println("Circle");
        }
    }

    public static class Rectangle implements Shape{
        @Override
        public void draw() {
            System.out.println("Rectangle");
        }
    }

    public static abstract class DrawShapeFactory {
        abstract Pen getPen();
        abstract Shape createShape();
    }

    public static class DrawCircleFactory extends DrawShapeFactory {
        @Override
        Pen getPen(){
            return new Pencil();
        }

        @Override
        Shape createShape() {
            return new Circle();
        }
    }

    public static class DrawRectangleFactory extends DrawShapeFactory {
        @Override
        Pen getPen(){
            return new Marker();
        }

        @Override
        Shape createShape() {
            return new Rectangle();
        }
    }

    public static void main(String[] args) {
        DrawShapeFactory circleFactory=new DrawCircleFactory();
        Pen pencil=circleFactory.getPen();
        Shape circle=circleFactory.createShape();
        pencil.stroke();
        circle.draw();

        DrawShapeFactory rectangleFactory=new DrawRectangleFactory();
        Pen marker=rectangleFactory.getPen();
        Shape rectangle=rectangleFactory.createShape();
        marker.stroke();
        rectangle.draw();
    }
}
```

# 建造者模式(Builder)

场景：产品包含多个构建步骤，每个步骤实现方式都可能不同。

方案：
1. 创建产品
2. 创建一个抽象的建造者，包含各个步骤。
3. 实现多个不同的具体建造者。
4. 创建一个指导者，将各个步骤整合起来并调用建造者获取产品。

把产品、建造步骤和建造过程分离，其中建造步骤在各个具体建造者类内实现，建造过程及产品结果的获取在指导者内实现。
```java
public class Builder {
    /**
     * 产品类：面试
     */
    public static class Interview{
        private String interviewer; // 面试官
        private String interviewee; // 应聘人
        private String time; // 面试时间
        private String place; // 面试地点

        public void setInterviewer(String interviewer){
            this.interviewer=interviewer;
        }
        public void setInterviewee(String interviewee){
            this.interviewee=interviewee;
        }
        public void setTime(String time){
            this.time=time;
        }
        public void setPlace(String place){
            this.place=place;
        }
        @Override
        public String toString(){
            return String.format("time:%s\nplace:%s\ninterviewer:%s\ninterviewee:%s\n", time, place, interviewer, interviewee);
        }
    }

    /**
     * 抽象建造者类：面试构建者
     */
    public static abstract class InterviewBuilder{
        protected Interview interview=new Interview();

        public abstract void buildTime();
        public abstract void buildPlace();
        public abstract void buildInterviewer();
        public abstract void buildInterviewee();
        public Interview getInterview(){
            return interview;
        }
    }

    public static class OnlineInterviewBuilder extends InterviewBuilder{
        @Override
        public void buildTime() {
            interview.setTime("今天");
        }
        @Override
        public void buildPlace() {
            interview.setPlace("腾讯会议");
        }
        @Override
        public void buildInterviewer() {
            interview.setInterviewer("马化腾");
        }
        @Override
        public void buildInterviewee() {
            interview.setInterviewee("张三");
        }
    }

    public static class OnsiteInterviewBuilder extends InterviewBuilder{
        @Override
        public void buildTime() {
            interview.setTime("明天");
        }
        @Override
        public void buildPlace() {
            interview.setPlace("会议室");
        }
        @Override
        public void buildInterviewer() {
            interview.setInterviewer("马云");
        }
        @Override
        public void buildInterviewee() {
            interview.setInterviewee("李四");
        }
    }

    public static class Director{
        private final InterviewBuilder builder;
        public Director(InterviewBuilder builder){
            this.builder=builder;
        }
        public Interview makeInterview(){
            builder.buildInterviewee();
            builder.buildInterviewer();
            builder.buildPlace();
            builder.buildTime();
            return builder.getInterview();
        }
    }

    public static void main(String[] args) {
        InterviewBuilder onlineInterviewBuilder=new OnlineInterviewBuilder();
        Director onlineDirector=new Director(onlineInterviewBuilder);
        Interview onlineInterview=onlineDirector.makeInterview();
        System.out.println(onlineInterview);

        InterviewBuilder onsiteInterviewBuilder = new OnsiteInterviewBuilder();
        Director onsiteDirector=new Director(onsiteInterviewBuilder);
        Interview onsiteInterview=onsiteDirector.makeInterview();
        System.out.println(onsiteInterview);
    }
}
```

# 原型模式(Prototype)

场景：创建对象副本，但并非从头开始构建，而是使用对象的当前信息直接复制。

方案：
1. 创建一个抽象原型（实际上是Cloneable），包含clone()方法声明
2. 实现具体原型，需要实现具体的clone()方法
3. 客户端调用clone()直接复制即可

```java
public class Prototype {
    /**
     * 抽象原型是Cloneable，声明了clone()方法
     * 具体原型是Shape，需要实现clone()
     */
    public static class Shape implements Cloneable{
        private String type;
        public Shape(String type){
            this.type=type;
        }
        public String getType(){
            return type;
        }
        public void setType(String type){
            this.type=type;
        }

        @Override
        public Shape clone() throws CloneNotSupportedException{
            Shape shape=(Shape) super.clone();
            // 深拷贝需要在调用父类的clone()后，继续复制子类特有的的一些属性
            shape.setType(type);
            return shape;
        }
        @Override
        public String toString(){
            return type;
        }
    }
    /**
     * 客户端
     */
    public static class Client{
        public static void main(String[] args) throws CloneNotSupportedException {
            Shape shape=new Shape("Circle");
            Shape cloned=shape.clone();
            System.out.printf("original: %s\n", shape);
            System.out.printf("cloned: %s\n", cloned);
        }
    }
}
```

# 单例模式(Singleton)

场景：确保全局只有该类的唯一一个实例

方案：
1. 将本类的构造函数声明为private
2. 实现静态的构建方法，当非空时直接返回当前实例，否则创建实例。

线程不安全的单例模式：
懒汉式：线程不安全
```java
public class SingletonPattern {
    public static class Singleton{
        private static Integer no=0;
        private static Singleton instance;
        private Singleton(){
            no++;
            System.out.print("初始化\n");
        }
        public void showNo(){
            System.out.printf("I'm the no.%d instance\n", no);
        }
        public static Singleton getInstance(){
            if(instance==null){
                instance=new Singleton();
            }
            return instance;
        }
    }
    public static class Main{
        public static class SingletonTest implements Runnable{
            @Override
            public void run() {
                Singleton singleton=Singleton.getInstance();
                singleton.showNo();
            }
        }
        public static void main(String[] args) {
            Thread[] threads=new Thread[100];
            for (int i = 0; i < threads.length; i++) {
                threads[i]=new Thread(new SingletonTest());
                threads[i].start();
            }
        }
    }
}
```
线程安全的单例模式：
1. 懒汉式：直接给方法加锁，确保唯一性
```java
public static synchronized Singleton getInstance(){
        if(instance==null){
        instance=new Singleton();
        }
        return instance;
        }
```
2. 懒汉式：双重检查锁
```java
private static volatile Singleton instance;
public static Singleton getInstance(){
    if(instance==null){
        synchronized (Singleton.class){
            if(instance==null){
                instance=new Singleton();
            }
        }
    }
    return instance;
}
```
3. 懒汉式：静态内部类(静态内部类只有在使用的时候彩绘被加载，因此也是懒汉式)
```java
public static class SingletonInstance{
    private static final Singleton singleton = new Singleton();
}
public static Singleton getInstance(){
    return SingletonInstance.singleton;
}
```
4. 饿汉式：静态常量或者静态代码块
```java
private static Singleton instance=new Singleton();
public static Singleton getInstance(){
    return instance;
}
```
5. 枚举：
```java
public static enum Singleton{
    INSTANCE;
    public void showNo(){
        Integer no=1;
        System.out.printf("I'm the no.%d instance\n", no);
    }
}
public static class Main{
    public static void main(String[] args) {
        Singleton instance=Singleton.INSTANCE;
        instance.showNo();
    }
}
```

# 适配器模式(Adapter)

场景： 将不兼容的接口集成进来

方案：
1. 创建当前使用的接口
2. 实现一个适配器，实现接口中的方法，并引入不兼容的类
3. 接口中的兼容方法应该将本方法的参数转换为适合旧的不兼容接口采用的参数

```java
public class Adapter {
    static class LegacyRectangle{
        public void display(int x1, int y1, int x2, int y2){
            System.out.printf("旧的矩形：(%d, %d), (%d, %d)\n", x1, y1, x2, y2);
        }
    }
    interface Shape{
        void draw(int x, int y, int width, int height);
    }
    static class RectangleAdapter implements Shape{
        private final LegacyRectangle legacyRectangle;
        public RectangleAdapter(LegacyRectangle legacyRectangle){
            this.legacyRectangle=legacyRectangle;
        }
        @Override
        public void draw(int x, int y, int width, int height) {
            int x2=x+width, y2=y+height;
            legacyRectangle.display(x, y, x2, y2);
        }
    }
    public static void main(String[] args) {
        LegacyRectangle legacyRectangle= new LegacyRectangle();
        Shape shapeAdapter= new RectangleAdapter(legacyRectangle);
        shapeAdapter.draw(10,15, 50, 100);
    }
}
```

# 桥接模式(Bridge)

场景：类可能有抽象、具体的部分，需要将两种分离出来，独立地变化。

方案：
1. 将类的非主要变化维度分离出来作为其他类，而在主类里面保存对通用接口类的引用。同时将主要逻辑方法作为抽象方法，从而主类成为抽象类。
2. 实现其他变化维度下的类
3. 用一个或多个具体类实现抽象主类里的抽象方法，该方法内可调用接口类对象，实现不同逻辑。

```java
public class Bridge {
    interface Lunch{
        String getLunch();
    }
    static class Rice implements Lunch{
        @Override
        public String getLunch() {
            return "rice";
        }
    }
    static class Noodle implements Lunch{
        @Override
        public String getLunch() {
            return "noodle";
        }
    }
    abstract static class MakeLunch{
        protected Lunch lunch;
        public MakeLunch(Lunch lunch){
            this.lunch=lunch;
        }
        abstract void make();
    }
    static class FriedLunch extends MakeLunch{
        public FriedLunch(Lunch lunch){
            super(lunch);
        }
        public void make(){
            System.out.printf("Will make fried %s\n", lunch.getLunch());
        }
    }
    static class CookLunch extends MakeLunch{
        public CookLunch(Lunch lunch){
            super(lunch);
        }
        public void make(){
            System.out.printf("Will make cook %s\n", lunch.getLunch());
        }
    }

    public static void main(String[] args) {
        MakeLunch cookNoodle=new CookLunch(new Noodle());
        cookNoodle.make();
        MakeLunch cookRice=new CookLunch(new Rice());
        cookRice.make();
        MakeLunch friedNoodle=new FriedLunch(new Noodle());
        friedNoodle.make();
        MakeLunch friedRice=new FriedLunch(new Rice());
        friedRice.make();
    }
}
```

# 组合模式(Composite)

场景：以一致的方式处理单个或组合的对象。

方案：
1. 声明统一的接口类及其抽象方法
2. 对单个或组合的对象分别实现接口，其中组合里可能需要有对单个对象的添加、删除等操作。

```java
public class Composite {
    interface FileSystem{
        void info(int space);
    }
    static class File implements FileSystem{
        private final String name;
        public File(String name){
            this.name=name;
        }
        @Override
        public void info(int space) {
            System.out.printf("%s文件：%s\n", " ".repeat(space), name);
        }
    }
    static class Directory implements FileSystem{
        private final String name;
        private final List<FileSystem> files;

        public Directory(String name){
            this.name=name;
            this.files=new ArrayList<>();
        }
        public void addFileSystem(FileSystem... files){
            this.files.addAll(Arrays.asList(files));
        }
        @Override
        public void info(int space) {
            System.out.printf("%s文件夹：%s\n", " ".repeat(space), name);
            files.forEach(file->file.info(space+2));
        }
    }
    public static void main(String[] args) {
        Directory dir1=new Directory("dir1");
        dir1.addFileSystem(new File("f1.txt"), new File("f2.txt"));
        Directory dir2=new Directory("dir2");
        dir2.addFileSystem(new File("f3.txt"), dir1, dir1);
        dir2.info(0);
    }
}
```

# 装饰器(Decorator)

场景：在不修改现有类结构的情况下动态地添加功能。

方案：
1. 原始类的各个功能应当在其接口类中声明
2. 装饰器类内部包含一个对原始对象的引用
3. 抽象的装饰器类内部同样实现了接口的各个方法，直接调用原始对象的方法
4. 具体的装饰器类实现抽象类，然后根据需要在方法中添加不同的功能。

```java
public class Decorator {
    interface Race{
        void begin();
    }
    static class Running implements Race{
        @Override
        public void begin(){
            System.out.println("正在进行跑步比赛");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    static abstract class RunningDecorator implements Race{
        protected Race race;
        public RunningDecorator(Race race){
            this.race=race;
        }
        @Override
        public void begin(){
            System.out.println("正在进行跑步比赛");
        }
    }
    static class RunningTimer extends RunningDecorator{
        public RunningTimer(Race race) {
            super(race);
        }
        @Override
        public void begin() {
            Long start=System.currentTimeMillis();
            super.begin();
            Long end=System.currentTimeMillis();
            System.out.printf("跑步总时间：%d秒\n", (end-start)/1000);
        }
    }
    static class RunningCheerer extends RunningDecorator{
        public RunningCheerer(Race race) {
            super(race);
        }
        @Override
        public void begin() {
            super.begin();
            System.out.println("为跑步者加油！！！");
        }
    }
    public static void main(String[] args) {
        Race running=new Running();
        running.begin();
        Race timer=new RunningTimer(running);
        timer.begin();
        Race cheerer=new RunningCheerer(running);
        cheerer.begin();
    }
}
```

# 外观模式(Facade)

场景：多个子系统交互、调用、依赖关系混乱，需要封装这些关系，提供简单的接口

方案：
1. 引入外观类将复杂的子系统接口封装
2. 提供简洁的接口

```java
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
```

# 享元模式(Flyweight)

场景：存在大量相似对象，其大部分属性相同，需要共享。

方案：
1. 创建公共接口类
2. 创建具体实现类，返回底层共享资源
3. 创建工厂类，内部的map保存类名和共享资源的映射，然后通过`get(key)`的方式返回对应的资源

```java
public class Flyweight {
    public interface Font{
        String getFont();
    }
    public static class Song implements Font{
        @Override
        public String getFont(){
            return "宋体";
        }
    }
    public static class Kai implements Font{
        @Override
        public String getFont(){
            return "楷体";
        }
    }
    public static class FontFactory{
        private final HashMap<String, Font> fonts;
        private static final FontFactory factory=new FontFactory();
        private FontFactory(){
            fonts=new HashMap<>();
            fonts.put("Song", new Song());
            fonts.put("Kai", new Kai());
        }
        public static FontFactory getInstance(){
            return factory;
        }
        public Font getFont(String fontName){
            return fonts.get(fontName);
        }
    }
    public static void main(String[] args) {
        FontFactory factory=FontFactory.getInstance();
        System.out.println(factory.getFont("Song").getFont());
        System.out.println(factory.getFont("Kai").getFont());
    }
}
```

# 代理模式(Proxy)

场景：控制、增强或者隐藏对具体对象的访问

方案：
1. 有通用接口，包含具体对象的主要方法声明
2. 具体对象实现接口方法
3. 创建一个代理对象同样实现该接口，在内部保存一个对具体对象的引用，对接口方法的实现直接调用具体对象的方法，并添加代理所需的其他逻辑。

```java
public class Proxy {
    public interface File{
        void open();
    }
    static class LocalFile implements File{
        @Override
        public void open() {
            System.out.println("打开本地文件");
        }
    }
    static class FileProxy implements File{
        private final File file;
        public FileProxy(File file){
            this.file=file;
        }
        @Override
        public void open() {
            file.open();
        }
    }
    public static void main(String[] args) {
        File file=new LocalFile();
        FileProxy fileProxy=new FileProxy(file);
        fileProxy.open();
    }
}
```

# 解释器模式(Interpreter)

场景：解释处理一些特定表达式

方案：将语句表示为抽象语法树
1. 声明主接口及解释方法
2. 终结符表达式实现主接口及解释方法，终结符表示最小语法单元，比如a属于终结符
3. 非终结符表达式实现主接口及解释方法，非终结符由终结符及其他非终结符组成。比如a-b属于非终结符。
4. 实现上下文，包含解释信息，主要是输入语句和解释器（这里省略了，直接放在main里）

```java
public class Interpreter {
    interface Expression{
        int calculate();
    }
    static class Number implements Expression{
        private final int value;
        public Number(int value){
            this.value=value;
        }
        @Override
        public int calculate() {
            return value;
        }
    }
    static class Add implements Expression{
        private final Expression left, right;
        public Add(Expression left, Expression right){
            this.left=left;
            this.right=right;
        }
        @Override
        public int calculate() { return left.calculate()+right.calculate(); }
    }
    static class Subtract implements Expression{
        private final Expression left, right;
        public Subtract(Expression left, Expression right){
            this.left=left;
            this.right=right;
        }
        @Override
        public int calculate() {
            return left.calculate()-right.calculate();
        }
    }

    public static void main(String[] args) {
        Expression expr=new Add(new Subtract(new Number(1), new Number(2)), new Number(3));
        System.out.printf("1-2+3=%d\n", expr.calculate());
    }
}
```

# 模板方法模式(Template Method)

场景：方法步骤是类似的，但需要对每种情况下的步骤调整。

方案：
1. 定义抽象的父类，包含方法核心结构，其中各步骤如果通用可以直接实现，否则声明为抽象方法
2. 子类实现父类，并实现自己的某些步骤的方法。

```java
public class TemplateMethod {
    static abstract class Commute{
        public void commute(){
            leaveHome();
            onTheWay();
            goToWorkplace();
        }
        abstract void leaveHome();
        abstract void onTheWay();
        void goToWorkplace(){
            System.out.println("坐电梯上工位");
        }
    }
    static class SubwayCommute extends Commute{
        @Override
        void leaveHome() {
            System.out.println("锁门，下楼去地铁站");
        }
        @Override
        void onTheWay() {
            System.out.println("坐着地铁呢");
        }
    }
    static class DriveCommute extends Commute{
        @Override
        void leaveHome() {
            System.out.println("锁门，拿着车钥匙去车库");
        }
        @Override
        void onTheWay() {
            System.out.println("开车呢，然后停到停车场");
        }
    }

    public static void main(String[] args) {
        Commute drive=new DriveCommute();
        drive.commute();
        Commute subway=new SubwayCommute();
        subway.commute();
    }
}
```

# 责任链模式(Chain of Responsibility)

场景：请求需要在多个对象之间传递，每个对象都可能处理或者传递给其他对象。并且可以动态修改处理的顺序、职责等。

方案：
1. 定义一个请求
2. 声明一个抽象类，是所有处理者的父类，包含设置下一个处理者的方法，以及处理请求的抽象方法
3. 实现多个具体的处理者，在处理方法中处理及传递请求。

```java
public class ChainOfResponsibility {
    record AskForLeave(int days, String reason) {
        @Override
        public int days() {
            System.out.printf("员工：俺想请假%d天，事由是%s\n", days, reason);
            return days;
        }
    }
    static abstract class Leader {
        protected Leader leader;
        public void setLeader(Leader leader){
            this.leader = leader;
        }
        public abstract void handleRequest(AskForLeave askForLeave);
    }
    static class Director extends Leader {
        @Override
        public void handleRequest(AskForLeave askForLeave) {
            if(askForLeave.days()<=7){
                System.out.println("主管：爷批准了");
            }else if(leader !=null){
                System.out.println("主管：爷批准不了，7天以上得找部长");
                leader.handleRequest(askForLeave);
            }
        }
    }
    static class Minister extends Leader {
        @Override
        public void handleRequest(AskForLeave askForLeave) {
            System.out.println("部长：爷批准了");
        }
    }
    public static void main(String[] args) {
        AskForLeave askForLeave=new AskForLeave(10, "累了");
        Leader director=new Director();
        Leader minister=new Minister();
        director.setLeader(minister);
        director.handleRequest(askForLeave);
    }
}
```

# 命令模式(Command)

场景：需要将请求的发送者与接收者分离，从而可以对请求排队、记录、撤销、重做等，而无需修改发送者和接收者之间的代码。

方案：
1. 实现实际的接收者，其中包含各种操作方法
2. 声明抽象的命令接口，包含各种操作方法的声明
3. 实现具体的各种命令，包含对接收者的引用，并且实现操作方法，调用接收者执行操作
4. 实现调用者，将命令组织起来传递给合适的接收者执行
5. 客户端创建命令、接收者、调用者，然后组织起来实现特定的操作流程

```java
public class Command {
    interface Order{
        void execute();
    }
    static class Stock{
        private final String name;
        private final double price;
        public Stock(String name, double price){
            this.name=name;
            this.price=price;
        }
        public void buy(){
            System.out.printf("有人买了%s花了%.2f块\n", name, price);
        }
        public void cancelBuy(){
            System.out.printf("人家不买%s了，得退回%.2f块\n", name, price);
        }
    }
    static class BuyStock implements Order{
        private final Stock stock;
        public BuyStock(Stock stock){
            this.stock=stock;
        }
        @Override
        public void execute(){
            stock.buy();
        }
    }
    static class CancelBuyStock implements Order{
        private final Stock stock;
        public CancelBuyStock(Stock stock){
            this.stock=stock;
        }
        @Override
        public void execute(){
            stock.cancelBuy();
        }
    }
    static class Broker{
        private final List<Order> orders=new ArrayList<>();
        public void addOrder(Order... order){
            orders.addAll(List.of(order));
        }
        public void operate(){
            orders.forEach(Order::execute);
        }
    }

    public static void main(String[] args) {
        Broker broker=new Broker();
        Stock watermelon=new Stock("西瓜", 10.05);
        Stock strawberry=new Stock("草莓", 50.53);
        broker.addOrder(new BuyStock(watermelon), new CancelBuyStock(watermelon), new BuyStock(strawberry));
        broker.operate();
    }
}
```

# 迭代器模式(Iterator)

场景：需要用统一的方式遍历各种不同的集合

方案：
1. 创建集合接口，包含获取迭代器的抽象方法
2. 创建迭代器接口，包含判断是否有下一个元素、获取下一个元素的抽象方法
3. 实现集合
4. 迭代器直接在具体集合类内部实现即可，其中一般可包含一个index以判断是否还有下一个

```java
public class IteratorPattern {
    interface Iterator {
        boolean hasNext();
        Object next();
    }
    interface Container{
        Iterator getIterator();
    }
    static class Array implements Container{
        private final String[] array=new String[]{"学习", "雷锋", "好榜样"};
        @Override
        public Iterator getIterator() {
            return new ArrayIterator();
        }
        class ArrayIterator implements Iterator{
            int index;
            @Override
            public boolean hasNext() {
                return index<array.length;
            }
            @Override
            public Object next() {
                return "数组   " + array[index++];
            }
        }
    }
    static class List implements Container{
        private final java.util.List<String> list=new ArrayList<>(java.util.List.of(new String[]{"学习", "雷锋", "好榜样"}));
        @Override
        public Iterator getIterator() {
            return new ListIterator();
        }
        class ListIterator implements Iterator{
            int index;
            @Override
            public boolean hasNext() {
                return index<list.size();
            }
            @Override
            public Object next() {
                return "列表   " + list.get(index++);
            }
        }
    }
    public static void main(String[] args) {
        Container array=new Array();
        for(Iterator iterator=array.getIterator();iterator.hasNext();){
            System.out.println(iterator.next());
        }
        Container list=new List();
        for(Iterator iterator=list.getIterator();iterator.hasNext();){
            System.out.println(iterator.next());
        }
    }
}
```

# 中介者模式(Mediator)

场景： 对象之间的通信等关联关系较为复杂，需要将其解耦出来

方案：
1. 声明抽象的关系接口（或者抽象类），包含对对象的管理接口方法、关系的接口方法
2. 实现具体的关系类，并实现对对象的管理方法、关系方法
3. 对象类内部需要改造，在相关方法内将关系交由关系类处理

```java
public class Mediator {
    static class User{
        private final String name;
        private final Chat chatter;
        public User(String name, Chat chatter){
            this.name=name;
            this.chatter=chatter;
        }
        public void send(String msg){
            System.out.printf(">>>%s 发送了消息 “%s”。\n", name, msg);
            chatter.send(this, msg);
        }
        public void receive(String msg){
            System.out.printf("<<<%s 收到了消息 “%s”。\n", name, msg);
        }
    }
    interface Chat{
        void send(User src, String msg);
        void addUser(User... user);
    }
    static class Chatter implements Chat{
        private final List<User> users=new ArrayList<>();
        @Override
        public void send(User/* 信息来自哪个用户 */ src, String msg) {
            users.stream().filter(user-> user!=src).forEach(user -> user.receive(msg));
        }
        @Override
        public void addUser(User... user) {
            users.addAll(Arrays.asList(user));
        }
    }
    public static void main(String[] args) {
        Chat chatter=new Chatter();
        User u1=new User("张三", chatter);
        User u2=new User("李斯", chatter);
        User u3=new User("王五", chatter);
        chatter.addUser(u1,u2,u3);
        u1.send("你好，我是"+u1.name);
        u3.send("吼蛙");
    }
}
```

# 备忘录模式(Memento)

场景：需要记录对象的内部状态并在需要时回滚，从而实现撤销、历史等功能。

方案：
1. 分析对象中需要记录的状态，在备忘录中同样声明
2. 对象中需要实现对备忘录的操作，如生成、恢复等。
3. 实现一个管理者，用于保存备忘录

```java
public class MementoPattern {
    record Memento(int score, String state) {
    }
    static class Gamer{
        private int score;
        private String state;
        public void set(int score, String state){
            this.score = score; this.state=state;
        }
        public String get(){
            return toString();
        }
        @Override
        public String toString(){
            return String.format("游戏状态：%s；总分：%d", state, score);
        }
        public Memento createMemento(){
            return new Memento(score, state);
        }
        public void restoreMemento(Memento memento){
            this.state= memento.state;
            this.score= memento.score;
        }
    }
    static class CareTaker{
        private final Map<String, Memento> map=new HashMap<>();
        public Memento getMemento(String state){
            return map.get(state);
        }
        public void setMemento(Memento memento){
            map.put(memento.state(), memento);
        }
    }
    public static void main(String[] args) {
        Gamer gamer=new Gamer();
        CareTaker careTaker=new CareTaker();
        gamer.set(0, "第一关");
        System.out.println(gamer);
        careTaker.setMemento(gamer.createMemento());
        gamer.set(15, "第二关");
        System.out.println(gamer);
        careTaker.setMemento(gamer.createMemento());
        // 重开第一关
        gamer.restoreMemento(careTaker.getMemento("第一关"));
        System.out.println(gamer);
    }
}
```

# 观察者模式(Observer)

场景：对象的状态发生改变时，需要发出通知，其他对象收到通知时可能会进行对应的更新。

方案：
1. 声明抽象的通知接口，包含管理观察者、通知观察者等方法
2. 实现具体的通知对象，包含对各种观察者的引用，并实现管理、通知等方法。对象修改状态的方法内部需要调用通知方法。通知方法内部调用观察者的接收通知的方法。
3. 声明抽象的观察者接口，包含接收通知的方法
4. 实现具体的观察者对象，实现具体接收通知并更新的逻辑。

```java
public class ObserverPattern {
    interface LotterySubject{   // 彩票事件
        void addCustomer(Customer... customer);
        void removeCustomer(Customer... customer);
        void notifyCustomers();
        void setMsg(String msg);
    }
    static class Drawing implements LotterySubject{ // 开奖了！！！
        private final List<Customer> customers=new ArrayList<>();
        @Override
        public void addCustomer(Customer... customer) {
            customers.addAll(List.of(customer));
        }
        @Override
        public void removeCustomer(Customer... customer) {
            customers.removeAll(Arrays.asList(customer));
        }
        @Override
        public void notifyCustomers() {
            customers.forEach(Customer::getResult);
        }
        @Override
        public void setMsg(String msg) {
            System.out.println(msg);
            notifyCustomers();
        }
    }
    interface Customer{
        void getResult();
    }
    static class Buyer implements Customer{ // 彩民
        private final String name;
        public Buyer(String name){
            this.name=name;
        }
        @Override
        public void getResult() {
            System.out.printf("爷是%s，爷知道了，反正爷肯定没中奖。\n", name);
        }
    }
    public static void main(String[] args) {
        LotterySubject drawing =new Drawing();
        Customer c1=new Buyer("真彩民");
        Customer c2=new Buyer("彩票站工作人员");
        drawing.addCustomer(c1, c2);
        drawing.setMsg("开奖了，但是奖金让站长贪了");
        drawing.removeCustomer(c1);
        drawing.setMsg("重新彩排一下，开奖了，咱们工作人员配合一下");
    }
}
```

# 状态模式

场景：对象包含多个状态，每种状态下的各种操作的行为都不相同。

方案：
1. 声明抽象的状态接口，其方法与对象的各种操作方法对应。
2. 实现具体的各种状态类
3. 对象内部需要添加设置状态的方法，并且各种操作直接调用状态的操作方法

```java
public class State {
    interface OrderState{
        void order();
        void cancel();
        void receive();
    }
    static class Ordering implements OrderState{
        @Override
        public void order() {
            System.out.println("好，马上给您下单");
        }
        @Override
        public void cancel() {
            System.out.println("还没下单怎么取消哦");
        }
        @Override
        public void receive() {
            System.out.println("正下单呢你这就收到了？");
        }
    }
    static class Canceling implements OrderState{
        @Override
        public void order() {
            System.out.println("取消的时候怎么下单？");
        }
        @Override
        public void cancel() {
            System.out.println("马上给您取消订单");
        }
        @Override
        public void receive() {
            System.out.println("取消了不准点收货");
        }
    }
    static class Receiving implements OrderState{
        @Override
        public void order() {
            System.out.println("下过单了，快给老子收货");
        }
        @Override
        public void cancel() {
            System.out.println("收货了还取消？想吃霸王餐？");
        }
        @Override
        public void receive() {
            System.out.println("确认收货了？概不退换熬");
        }
    }
    static class Order{
        private OrderState state;
        public void setState(OrderState state){
            this.state=state;
        }
        public void order(){
            state.order();
        }
        public void cancel(){
            state.cancel();
        }
        public void receive(){
            state.receive();
        }
    }

    public static void main(String[] args) {
        Order order=new Order();
        OrderState[] states=new OrderState[]{new Ordering(), new Canceling(), new Receiving()};
        Arrays.stream(states).forEach(state->{order.setState(state); order.order();order.receive();order.cancel();});
    }
}
```

# 策略模式(Strategy)

场景：一个系统需要根据不同的场景或条件使用不同的策略，需要将系统与策略解耦。

方案：
1. 声明抽象的策略接口，包含策略的执行方法
2. 实现各种具体的策略的执行方法
3. 构建一个系统类，包含对抽象策略的引用，然后直接调用策略的执行方法执行。

```java
public class Strategy {
    interface Allocation{
        void allocate();
        String getName();
    }
    static class Tradition implements Allocation{
        @Override
        public void allocate() {
            System.out.println("地位高的坐主席，其他依次排列");
        }
        @Override
        public String getName() {
            return "传统的方式";
        }
    }
    static class Drinking implements Allocation{
        @Override
        public void allocate() {
            System.out.println("不能喝的坐小孩那桌");
        }
        @Override
        public String getName() {
            return "喝死人的方式";
        }
    }
    static class Chairman{
        private Allocation allocation;
        public void setAllocation(Allocation allocation) {
            this.allocation = allocation;
            System.out.printf("现在咱们按照%s安排位置啊\n", allocation.getName());
        }
        public void allocate(){
            allocation.allocate();
        }
    }
    public static void main(String[] args) {
        Chairman chairman=new Chairman();
        Allocation tradition = new Tradition();
        chairman.setAllocation(tradition);
        chairman.allocate();
        Allocation drinking = new Drinking();
        chairman.setAllocation(drinking);
        chairman.allocate();
    }
}
```

实例：
RocketMQ负载均衡的时候，会有平均分配、环状平均分配、按机房位置分配等等多种分配算法。因此在负载均衡类`RebalanceImpl`里有一个对抽象分配算法`AllocateMessageQueueStrategy`的引用，并调用`strategy.allocate()`方法获取分配结果。而具体的各种分配算法实现抽象分配算法接口，在各自的`allocate()`内部实现其具体分配逻辑。

# 访问者模式

场景：对象中的元素需要进行不同的操作

方案：引入访问者，将操作从元素中分离出来
1. 声明抽象的元素接口，包含统一的操作方法接口
2. 实现具体的各种元素及其操作
3. 声明抽象的访问者接口，包含对各种具体元素的操作方法
4. 实现具体的访问者，对不同元素调用其操作方法

```java
public class Visitor {
    interface AllFile{
        void show(Explorer explorer);
    }
    static class File implements AllFile{
        private final String name;
        public File(String name){
            this.name=name;
        }
        @Override
        public void show(Explorer explorer) {
            System.out.printf("这是个文件：%s\n", name);
            explorer.show(this);
        }
    }
    static class Directory implements AllFile{
        private final String name;
        private final List<File> files=new ArrayList<>();
        public Directory(String name){
            this.name=name;
        }
        public void addFile(File... files){
            this.files.addAll(Arrays.asList(files));
        }
        @Override
        public void show(Explorer explorer) {
            System.out.printf("这是个文件夹：%s\n", name);
            explorer.show(this);
            for(File file: files){
                file.show(explorer);
            }
        }
    }
    interface Explorer{
        void show(File file);
        void show(Directory directory);
    }
    static class FileExplorer implements Explorer{
        @Override
        public void show(File file) {
            System.out.println("雷吼，介似个文件："+file.name);
        }
        @Override
        public void show(Directory directory) {
            System.out.println("雷吼，介似个文件夹："+directory.name);
        }
    }
    public static void main(String[] args) {
        File f1=new File("f1");
        File f2=new File("f2");
        File f3=new File("f3");
        Directory d1=new Directory("d1");
        d1.addFile(f1, f2);
        Explorer explorer=new FileExplorer();
        f3.show(explorer);
        d1.show(explorer);
    }
}
```

# 参考资料
[^1]: [23种设计模式](https://zhuanlan.zhihu.com/p/651451595)