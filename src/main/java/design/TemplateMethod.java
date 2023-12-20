package design;

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
