package design;

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
