package shousi.cider.b;

public class Main {
    public static class Node{
        int val;
        Node next;
        public Node(int val, Node next){
            this.val=val;
            this.next=next;
        }
    }
    /*
     * 默认 1<=begin<=end<=list.length
     */
    public Node rev(Node list, int begin, int end){
        if(list==null){
            return null;
        }
        Node fake=new Node(0, list);
        Node h=fake;
        for(int i=1;i<begin;i++, h=h.next);
        Node head=h.next, res=head;
        for(Node t=head.next;t!=null && begin<end;begin++){
            head.next=t.next;
            t.next=res;
            res=t;
            t=head.next;
        }
        h.next=res;
        return fake.next;
    }
    public static void main(String[] args) {
        int begin=4, end=5;
        Node list=new Node(1,new Node(2, new Node(3, new Node(4, new Node(5, null)))));
        Main main=new Main();

        show(list);
        Node revList=main.rev(list, begin, end);
        show(revList);
    }

    public static void show(Node list){
        for(;list!=null;list=list.next){
            System.out.printf("%d ", list.val);
        }
        System.out.println();
    }
}
