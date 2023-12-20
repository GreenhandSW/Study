package design;

public class Strategy {
    interface Allocation{
        void allocate();
        String getName();
    }
    static class Tradition implements Allocation{
        @Override
        public void allocate() {
            System.out.println("地位高的坐主席，其他依次排列");
        }
        @Override
        public String getName() {
            return "传统的方式";
        }
    }
    static class Drinking implements Allocation{
        @Override
        public void allocate() {
            System.out.println("不能喝的坐小孩那桌");
        }
        @Override
        public String getName() {
            return "喝死人的方式";
        }
    }
    static class Chairman{
        private Allocation allocation;
        public void setAllocation(Allocation allocation) {
            this.allocation = allocation;
            System.out.printf("现在咱们按照%s安排位置啊\n", allocation.getName());
        }
        public void allocate(){
            allocation.allocate();
        }
    }
    public static void main(String[] args) {
        Chairman chairman=new Chairman();
        Allocation tradition = new Tradition();
        chairman.setAllocation(tradition);
        chairman.allocate();
        Allocation drinking = new Drinking();
        chairman.setAllocation(drinking);
        chairman.allocate();
    }
}
