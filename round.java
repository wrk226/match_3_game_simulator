/*
 * round.java
 *
 * Version:
 *     1.00
 *
 */


import java.util.ArrayList;

/**
 * @author: Renke Wang
 */

public class round {
    ArrayList<group> round;
    round(){
        round=new ArrayList<>();
    }
    round add(group new_group){
        round.add( new_group );
        return this;
    }

    @Override
    public String toString() {
        return round.toString();
    }

}
