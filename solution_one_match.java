/*
 * solution_one_match.java
 *
 * Version:
 *     1.00
 *
 */


import java.util.ArrayList;

/**
 * @author: Renke Wang
 */

public class solution_one_match {
    match matched_cubes;
    ArrayList<cube[][]> process;
    solution_one_match(match matched_cubes,ArrayList<cube[][]> process){
        this.matched_cubes=matched_cubes;
        this.process=process;
    }

    @Override
    public String toString() {
        StringBuilder output= new StringBuilder();

        for(cube[][] board:process){
            output.append( config.board_to_string( board ) );
            output.append( "\n" );
        }
        return output.toString();
    }
}
