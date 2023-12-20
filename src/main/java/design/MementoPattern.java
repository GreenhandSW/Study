package design;

import java.util.HashMap;
import java.util.Map;

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
