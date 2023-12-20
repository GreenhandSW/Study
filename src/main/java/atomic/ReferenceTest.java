package atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.concurrent.ThreadLocalRandom.current;

public class ReferenceTest {

    /**
     * 定义AtomicReference并且初始值为DebitCard("zhangsan", 0)
     */
    private static final AtomicReference<DebitCard> debitCardRef
            = new AtomicReference<>(new DebitCard("zhangsan", 0));

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread("T-" + i) {
                @Override
                public void run() {
                    // 获取AtomicReference的当前值
                    final DebitCard dc = debitCardRef.get();
                    // 基于AtomicReference的当前值创建一个新的DebitCard
                    DebitCard newDC = new DebitCard(dc.getAccount(),
                            dc.getAmount() + 10);
                    // 基于CAS算法更新AtomicReference的当前值
                    if(debitCardRef.compareAndSet(dc,newDC)){
                        // 更新成功
                        System.out.println(newDC);
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(current().nextInt(20));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}
