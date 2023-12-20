package design;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
