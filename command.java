/*
 * command.java
 *
 * Version:
 *     1.00
 *
 */


import java.util.Objects;

/**
 * @author: Renke Wang
 */

public class command {
    int row;
    int column;
    command(int row,int column){
        this.row=row;
        this.column=column;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        command command = ( command ) o;
        return row == command.row &&
                column == command.column;
    }

    @Override
    public int hashCode() {
        return row*10+ column ;
    }

    @Override
    public String toString() {
        return "("+row+", "+column+")";
    }
}
