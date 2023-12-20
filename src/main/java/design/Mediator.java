package design;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
