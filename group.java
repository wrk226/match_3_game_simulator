/*
 * group.java
 *
 * Version:
 *     1.00
 *
 */


import java.util.ArrayList;

/**
 * @author: Renke Wang
 */

public class group {
    ArrayList<cube> group;
    group(){
        group=new ArrayList<>();
    }
    group add(cube new_cube){
        group.add( new_cube );
        return this;
    }

    @Override
    public String toString() {
        return group.toString();
    }
}
