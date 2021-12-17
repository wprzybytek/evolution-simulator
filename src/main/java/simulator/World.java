package simulator;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class World {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(2);
        System.out.println(Collections.max(list));
    }
}
