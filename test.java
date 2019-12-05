import java.util.*;
public class test {
    public static void main(String[] args){
        ArrayList<Integer> t = new ArrayList<Integer>();
        
        t.add(1);
        t.add(2);
        t.add(3);
        t.add(0);
        
        t.remove(0);
        System.out.println(t);
    }
}