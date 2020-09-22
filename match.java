/*
 * match.java
 *
 * Version:
 *     1.00
 *
 */


import java.util.ArrayList;

/**
 * @author: Renke Wang
 */

public class match {
    ArrayList<round> match;
    match(){
        match=new ArrayList<>();
    }
    match add(round new_round){
        match.add( new_round );
        return this;
    }

    @Override
    public String toString() {
        return match.toString();
    }
}
