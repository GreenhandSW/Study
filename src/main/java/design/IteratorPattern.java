package design;

import java.util.ArrayList;

public class IteratorPattern {
    interface Iterator {
        boolean hasNext();
        Object next();
    }
    interface Container{
        Iterator getIterator();
    }
    static class Array implements Container{
        private final String[] array=new String[]{"学习", "雷锋", "好榜样"};
        @Override
        public Iterator getIterator() {
            return new ArrayIterator();
        }
        class ArrayIterator implements Iterator{
            int index;
            @Override
            public boolean hasNext() {
                return index<array.length;
            }
            @Override
            public Object next() {
                return "数组   " + array[index++];
            }
        }
    }
    static class List implements Container{
        private final java.util.List<String> list=new ArrayList<>(java.util.List.of(new String[]{"学习", "雷锋", "好榜样"}));
        @Override
        public Iterator getIterator() {
            return new ListIterator();
        }
        class ListIterator implements Iterator{
            int index;
            @Override
            public boolean hasNext() {
                return index<list.size();
            }
            @Override
            public Object next() {
                return "列表   " + list.get(index++);
            }
        }
    }
    public static void main(String[] args) {
        Container array=new Array();
        for(Iterator iterator=array.getIterator();iterator.hasNext();){
            System.out.println(iterator.next());
        }
        Container list=new List();
        for(Iterator iterator=list.getIterator();iterator.hasNext();){
            System.out.println(iterator.next());
        }
    }
}
