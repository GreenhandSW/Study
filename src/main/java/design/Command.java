package design;

import java.util.ArrayList;
import java.util.List;

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
