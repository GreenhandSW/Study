# 深入理解Java虚拟机
## 5 调优案例分析与实战

### 5.3 Eclipse运行速度调优

#### 1. 准备工作

>eclipse版本：2021-09(4.21)
>jdk: openjdk-11-jdk
>需提前安装visualvm，并添加visualGC插件
>eclipse插件：[StartTime](https://github.com/fenixsoft/jvm_book/blob/master/src/org/fenixsoft/jvm/chapter5/EclipseStartTime_1.0.0.201011281102.jar)，下载后放置到`ECLIPSE_PATH/plugins/`目录下。
>虚拟机参数配置：在`ECLIPSE_PATH/eclipse.ini`中，进行如下配置：
>
>> 删除`-XX:+UseG1GC`和`plugins/org.eclipse.justj.openjdk.hotspot.jre.full.linux.x86_64_16.0.2.v20210721-1149/jre/bin`
>>
>> 添加jdk路径为`/usr/lib/jvm/java-11-openjdk-amd64/bin/`
>>
>> 添加GC参数为`-XX:+UseSerialGC`，即采用Serial+Serial Old(MSC)收集器
>>
>> 添加`-Dcom.sun.management.jmxremote`参数以启用JMX管理（从而在VisualVM中收集原始数据）。

#### 2. 基本测试结果

多次启动获得测试结果如下：

>启动时间：稳定在7500ms附近（用VisualVM监测时会上升到8500ms）
>
>最后一次启动时，垃圾收集耗时310 ms，共6次
>
>> Minor GC 3次，耗时100 ms,
>>
>> Full GC 3次，耗时210 ms
>
>加载类9946个，耗时4663 ms
>
>即时编译时间15224 ms
>
>堆内存划分：共256 M，其中新生代85 M（Eden 68 M, 两个Survivor各8.5 M），老年代62 M。

#### 3. 优化类加载时间

取消字节码验证前的类加载时间：

```
Loaded  Bytes  Unloaded  Bytes     Time   
 12654 26565.9        0     0.0       8.02
```

取消字节码验证（添加`-Xverify:none`参数）后的类加载时间：

```
Loaded  Bytes  Unloaded  Bytes     Time   
 11152 23342.9        0     0.0       6.01
```

取消后，启动时间最大下降1s，达到6500 ms，平均略微超过7000 ms。

另外，关闭即时编译（添加`-Xint`参数）后，启动时间高达20000 ms，说明Hotspot的即时编译功能是很有用的。

#### 4. 调整内存设置以控制垃圾收集频率

##### 4.1. 减少 Full GC

添加一些参数用于输出启动过程中发生的GC事件详情：

```
-verbose:gc
-Xlog:gc*,safepoint:gc.log:time,uptime
```

以下为某次测试过程中发生的垃圾收集事件：

```
[1.526s][info][gc] GC(0) Pause Young (Allocation Failure) 68M->14M(247M) 42.195ms
[1.923s][info][gc] GC(1) Pause Full (Metadata GC Threshold) 31M->16M(247M) 43.528ms
[3.695s][info][gc] GC(2) Pause Young (Allocation Failure) 84M->27M(247M) 27.092ms
[3.800s][info][gc] GC(3) Pause Full (Metadata GC Threshold) 30M->27M(247M) 70.599ms
[4.934s][info][gc] GC(4) Pause Young (Allocation Failure) 95M->38M(247M) 29.296ms
[6.125s][info][gc] GC(5) Pause Young (Allocation Failure) 106M->44M(247M) 27.049ms
[6.820s][info][gc] GC(6) Pause Full (Metadata GC Threshold) 57M->46M(247M) 142.959ms
```

> 此处需要声明一些关于元空间的知识：元空间是在JDK1.8中出现的，取代了JDK1.7中的永生代空间。此前的永生代空间的确可以通过`-XX:PermGenSize=`设置初始容量。但元空间的扩容是Hotspot内部通过一些复杂的逻辑进行的，默认的容量是跟系统内存相关的，无法改变；名称类似的`-XX:MetaspaceSize=`参数设置的不是metaspace容量，而是metaspace经过若干次扩容后若达到该值，则触发一次Full GC，对整个内存进行垃圾回收。随后MetaspaceSize会由虚拟机控制，进而在再次达到该值时触发Full GC。[^1]

可以看出，由于默认设置的触发Full GC的元空间容量太小，导致发生了多次Full GC，这几次Full GC回收的空间微乎其微，却耗费了大量时间（每次超过0.1s）。

通过添加`-XX:+PrintFlagsInitial`参数，输出默认的虚拟机参数，发现元空间容量设置如下：

```
   size_t MetaspaceSize                            = 21810376                               {pd product} {default}
```

即默认设置是21M。通过VisualVM中的Monitor>Metaspace页签观察也发现经过几次扩容后已经达到了75.125 M（注意在Visual GC页签中，Metaspace后的数字为(1.016 M, 75.125 M)，这里的1.016G并非Metaspace的当前容量，而是允许虚拟机对Metaspace扩容能达到的最大容量；75.125 M才是当前容量，其他参数同理）。说明初始触发Full GC的容量太小。

查询资料后发现，初始的触发Full GC的元空间容量设置参数为：`-XX:MetaspaceSize=128M`，设置该参数以提高触发Full GC的元空间容量阈值。

再次测试后发现已经没有Full GC事件发生。

删除日志输出参数重新测试，发现最低启动时间没有明显下降，但平均启动时间已经基本稳定在6800 ms左右。

>参考资料：
>
>[^1]:[JVM参数MetaspaceSize的误解](https://www.jianshu.com/p/b448c21d2e71)

##### 4.2. 减少Minor GC的次数

从上面的垃圾回收过程还可以看出，发生了数次Minor GC，最大的时候是新生代占用100M时发生了Young GC。但之前也提到“堆内存划分：共256 M，其中新生代85 M（Eden 68 M, 两个Survivor各8.5 M），老年代62 M“，也就是Eden加1个Survivor共76.5 M可以作为一次Minor GC的空间。

通过提高`-Xms256M`参数，可以增大使用的内存空间，从而增大Eden区、Survivor区，能够适度减少发生Minor GC的次数。但是相应的，容量增大会导致单次停顿时间变长。例如设置`-Xms1024M`使得Eden区变为273 M时，Minor GC次数减少到了1次，但单次停顿时间也长达100多ms。

查看VisualVM发现，发生的几次Minor GC中，都是新生代已经满了，然后清除掉绝大部分对象，并将少部分（约6M/次）的年龄提升。由于Survivor区只有8M，根据”3.8.4节 动态对象年龄判定“得出，6M已经超过了8M的一半，因此直接将这6M的对象复制晋升到老年代。

针对这样的情况，理论上有两种思路：

1. 增大Survivor区的相对容量（默认的Eden: Survivor1: Survivor2=8:1:1，对应参数为`-XX:SurvivorRatio=8`），通过将该参数调小来增大Survivor区的容量。
2. 减少Survivor区的相对容量。由于上述的动态对象年龄判定的特点，减少Survivor区容量后，每次Minor GC后预备提升年龄的对象总容量会更可能大于Survivor区的一半，从而直接进入老年代而非Survivor区，节省一次复制的时间。（这是因为Appel式收集法每次使用Eden区和一个Survivor区来分配对象，另一个Survivor用于在GC时作为存活对象的复制位置。因此Minor GC时一般会发生一次复制，而在晋升到老年代时又会发生一次复制）。

通过固定新生代容量（`-Xmn96m`），将SurvivorRatio从4（一个Survivor为16 M）到8（一个Survivor为9.5 M）之间来回变动，发现对结果的影响很小，说明上述猜想错误，少量的对象复制对GC停顿时间的影响微乎其微。

##### 4.3. 选择收集器降低延迟

通过测试多种收集器发现G1、CMS+ParNew、Serial等收集器对延迟及停顿的影响很小，基本都在300 ms以内，整体启动时间也已经稳定在7000ms以内，可见启动的性能已经基本不受GC的影响，达到了较好的水平。

## 6. 类文件结构

### 6.3. Class类文件的结构

```java
package top.helloshiwei.chp6classfile;

public class TestClass {
    private int m;
    public int inc(){
        return m+1;
    }
}
```

openjdk-11编译结果

#### 访问标志

<img src="assets/image-20211019200305269.png" alt="image-20211019200305269" style="zoom:80%;" />

选中字段为访问标志(access_flags)，表示其标志为为`ACC_PUBLIC`及`ACC_SUPER`。

#### 类索引、父类索引和接口索引集合

<img src="assets/image-20211019200657061.png" alt="image-20211019200657061" style="zoom:80%;" />

表示其类索引为常量池的第三项，父类索引为第四项，没有接口索引。

为方便期间，通过`javap -verbose TestClass`命令查看字节码翻译结果：

```
Constant pool:
   #1 = Methodref          #4.#15         // java/lang/Object."<init>":()V
   #2 = Fieldref           #3.#16         // top/helloshiwei/chp6classfile/TestClass.m:I
   #3 = Class              #17            // top/helloshiwei/chp6classfile/TestClass
   #4 = Class              #18            // java/lang/Object
   #5 = Utf8               m
   #6 = Utf8               I
   #7 = Utf8               <init>
   #8 = Utf8               ()V
   #9 = Utf8               Code
  #10 = Utf8               LineNumberTable
  #11 = Utf8               inc
  #12 = Utf8               ()I
  #13 = Utf8               SourceFile
  #14 = Utf8               TestClass.java
  #15 = NameAndType        #7:#8          // "<init>":()V
  #16 = NameAndType        #5:#6          // m:I
  #17 = Utf8               top/helloshiwei/chp6classfile/TestClass
  #18 = Utf8               java/lang/Object
```

可以看到：

第3项指向的第17项为`top/helloshiwei/chp6classfile/TestClass`，即定义的类。

第4项指向的第18项为`java/lang/Object`，即默认的父类Object。

#### 字段表集合

<img src="assets/image-20211019202427138.png" alt="image-20211019202427138" style="zoom:80%;" />

这一部分为字段表集合：

- 0x0001表示只有一个字段表数据。
- 0x0002表示该字段的访问标志为ACC_PRIVATE，即private变量
- 0x0005表示该字段的名称为常量池中第5项，即m
- 0x0006表示该字段的类型为常量池中第6项，即`I`（代表int数据类型）

综上，该字段表中有一个数据，为`private int m;`。

另外：尽管Java语言中字段不能重载，即两个作用域相同的变量无论类型、修饰符是否相同，都必须使用不一样的名称，否则无法编译。但对Class文件来说，只要其数据类型或修饰符不完全相同，就可以使用相同的名称。

#### 方法表集合

<img src="assets/image-20211019203748899.png" alt="image-20211019203748899" style="zoom:80%;" />

这一部分为方法表集合：

- 0x0002表示方法表中有2条数据。
- 第一个方法：
    - 0x0001表示该方法的访问标志为ACC_PUBLIC，即public方法
    - 0x0007表示该方法的名称为常量池中第7项，即`<init>`（编译器自动添加的构造器方法）
    - 0x0008表示该方法的类型为常量池中第8项，即`()V`（`()`代表参数列表为空，`V`代表void，即返回值为空）
    - 0x0001表示该方法的属性表中有1项属性
    - 0x0009表示该方法的这个属性为第9项，即`Code`，表示该属性是方法的字节码描述

**若子类没有重写父类某个方法，则子类的class文件的方法表中不会有这一个方法。**

##### Code 属性

<img src="assets/image-20211020161837267.png" alt="image-20211020161837267" style="zoom:80%;" />

上图中为`<init>`方法的Code属性：

- 0x0002表示其操作数栈的最大深度max_stack为2
- 0x0001表示其本地变量表的容量max_locals为1
- 0x000000B表示字节码长度code_length为11

使用`javap -verbose`命令翻译得到的方法字节码为：

```
{
  public top.helloshiwei.chp6classfile.TestClass();
    descriptor: ()V
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=2, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: aload_0
         5: bipush        125
         7: putfield      #2                  // Field m:I
        10: return
      LineNumberTable:
        line 3: 0
        line 4: 4
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      11     0  this   Ltop/helloshiwei/chp6classfile/TestClass;

  public int inc();
    descriptor: ()I
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=2, locals=1, args_size=1
         0: aload_0
         1: getfield      #2                  // Field m:I
         4: iconst_1
         5: iadd
         6: ireturn
      LineNumberTable:
        line 6: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       7     0  this   Ltop/helloshiwei/chp6classfile/TestClass;
}
```

从代码中能看到，虽然inc()方法没有参数，但其参数数量args_size仍为1，这是因为其作为实例函数，肯定能够访问实例自身。javac编译器在编译时会自动把对this的访问转为对一个普通参数的访问，然后在虚拟机调用实例方法时自动传入这一参数。

对于static方法，其args_size=0。

# 其他

## 堆外内存
广义的堆外内存是除了堆栈内存之外的所有内存，包括JVM本身分配的内存、JNI分配的内存、DirectByteBuffer分配的内存等等。

一般提到堆外内存指的是狭义的堆外内存，也就是直接内存DirectByteBuffer。

优点：
1. 可以在进程之间共享，减少虚拟机之间的复制
2. 堆外内存一般放一些长期存活并大量存在的对象，如果放在堆内会频繁触发Minor GC或者Full GC，而放在堆外可以减少GC对应用的影响。
3. 某些场景下可以提升IO性能，无需将数据从堆内复制到堆外。

堆外内存用于IO主要是为了减少复制的次数。

常规情况下读写文件需从用户态切换到内核态，从而需要在内核地址空间和用户地址空间保存两份数据，空间利用率低，且复制耗时也长。[^1] 
![img.png](assets/bio.png)

采用直接缓冲区可以通过NIO读写，直接缓冲区可以同时被内核态和用户态的程序访问到，从而减少一次复制。
![img.png](assets/nio.png)

### 创建过程
1. 暴露出来的分配方法：
```java
public static ByteBuffer allocateDirect(int capacity) {
    return new DirectByteBuffer(capacity);
}
```
2. `DirectByteBuffer`类构造方法：
```java
DirectByteBuffer(int cap) {                   // package-private
    super(-1, 0, cap, cap, null);
    boolean pa = VM.isDirectMemoryPageAligned();
    int ps = Bits.pageSize();
    long size = Math.max(1L, (long)cap + (pa ? ps : 0));
    Bits.reserveMemory(size, cap);
    long base = 0;
    // 分配内存并返回地址
    try {
        base = UNSAFE.allocateMemory(size);
    } catch (OutOfMemoryError x) {
        Bits.unreserveMemory(size, cap);
        throw x;
    }
    // 初始化分配的内存值为0
    UNSAFE.setMemory(base, size, (byte) 0);
    if (pa && (base % ps != 0)) {
        // Round up to page boundary
        address = base + ps - (base & (ps - 1));
    } else {
        address = base;
    }
    // 创建一个清理线程，如果创建失败，则直接调用freeMemory()清除分配的内存
    try {
        cleaner = Cleaner.create(this, new Deallocator(base, size, cap));
    } catch (Throwable t) {
        // Prevent leak if the Deallocator or Cleaner fail for any reason
        UNSAFE.freeMemory(base);
        Bits.unreserveMemory(size, cap);
        throw t;
    }
    att = null;
}
```

# 参考资料

[^1]: [JVM第十一章_直接内存](https://blog.csdn.net/m0_45097637/article/details/106854560)

[^2]: [【JVM】详解直接内存](https://blog.csdn.net/weixin_51146329/article/details/128770701)