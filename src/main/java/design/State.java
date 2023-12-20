package design;

import java.util.Arrays;

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
