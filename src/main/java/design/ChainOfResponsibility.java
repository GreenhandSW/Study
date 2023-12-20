package design;

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
