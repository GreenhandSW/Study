package jvm;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RuntimeConstantPoolOOM {
    public static void main(String[] args) {
        Set<String> set=new HashSet<>();
        Random r=new Random();
        StringBuilder b=new StringBuilder(100000);
        while (true){
            for (int i = 0; i < b.capacity(); i++) {
                b.append('a'+r.nextInt(26));
            }
            set.add(b.toString().intern());
        }
    }
}
