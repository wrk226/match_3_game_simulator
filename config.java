/*
 * config.java
 *
 * Version:
 *     1.00
 *
 */


import java.io.*;
import java.util.*;

/**
 * @author: Renke Wang
 */

public class config {
    static String GRADE_TYPE="xiao chu wang based";
//    static int ROUND_PER_BOARD=100;
    static Map<String,Integer> move_index;

//    static int MAX_STEPS=30;
    static boolean OUTPUT=false;
    static int SIZE=6;
    static boolean RANDOM_DROP=true;
    static String FILL_QUEUE="221001020211";
    static int[] GRADE_XCW=new int[]{10,40,160,640,4000};
    static boolean RANDOM_GENERATE=true;
    static cube[][] INIT_BOARD= matrix_transfer(new int[][]
            {{0, 0, 2, 0, 1, 1} ,
            {1, 1, 0, 2, 1, 0} ,
            {0, 2, 0, 2, 0, 2} ,
            {1, 2, 1, 1, 2, 1} ,
            {2, 1, 0, 1, 2, 1} ,
            {2, 2, 0, 2, 0, 0} });

    static HashMap<Integer,Integer> monster_color=new HashMap<>();
    static HashMap<Integer,Integer> level_color=new HashMap<>();
    static void init_color(){
        monster_color.put( 0,93 );
        monster_color.put( 1,96 );
        monster_color.put( 2,95 );
        monster_color.put( -1,51 );
        level_color.put( 0,100 );
        level_color.put( 1,42 );
        level_color.put( 2,44 );
        level_color.put( 3,45 );
        level_color.put( 4,41 );
    }



    static private cube[][] matrix_transfer(int[][] board){
        cube[][] new_board=new cube[SIZE][SIZE];
        for ( int row=0;row<SIZE;row++){
            for(int column=0;column<SIZE;column++){
                new_board[row][column]=new cube( board[row][column] );
            }
        }
        return new_board;
    }
    static int RANDOM_SEED=-1;
    static ArrayList<command[]> POSSIBLE_MOVES;

    static {
        try {
            POSSIBLE_MOVES = get_moves(SIZE);
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    static private ArrayList<command[]> get_moves(int SIZE) throws IOException {
        ArrayList<command[]> combinations = new ArrayList<>();
        for(int row1 = 0;row1<SIZE;row1++){
            for(int column1 = 0;column1<SIZE;column1++){
                // cube remove
                combinations.add(new command[]{new command(row1, column1)});
                for(int row2 = 0;row2<SIZE;row2++){
                    for(int column2 = 0;column2<SIZE;column2++){
                        if(row2>row1 || (row2==row1 && column2>column1)){
                            // cube switch
                            combinations.add( new command[]{new command(row1, column1),new command(row2, column2)} );
                        }
                    }
                }
            }
        }
        // save to map
        move_index = new HashMap<>();
        for (int i=0;i<combinations.size();i++) {
            move_index.put(Arrays.toString(combinations.get(i)),i);
        }

        // write move index to csv
//        try ( PrintWriter writer = new PrintWriter( new BufferedWriter( new FileWriter( new File( "move_index.csv" ) ) ) ) ) {
//            // thread list
//            ArrayList<one_step_optimal_multiple_thread> thread_list = new ArrayList<>();
//            StringBuilder sb = new StringBuilder();
//            for(int i=0;i<combinations.size();i++){
//                sb.append( i );
//                sb.append( "," );
//                sb.append( Arrays.toString( combinations.get( i ) ).replaceAll( ","," " ) );
//                sb.append( "\n" );
//            }
//            writer.print( sb.toString() );
//        }

        return combinations;
    }

    static public String board_to_string(cube[][] board) {
        StringBuilder output = new StringBuilder();
        for ( cube[] row : board ) {
            for ( cube cube : row ) {
                output.append( cube );
            }
            output.append( "\n" );
        }
        return output.toString();
    }
//    static public ArrayList<Integer> board_to_data(cube[][] board) {
//        ArrayList<Integer> monster_list=new ArrayList<>();
//        ArrayList<Integer> level_list=new ArrayList<>();
//        for ( cube[] row : board ) {
//            for ( cube cube : row ) {
//                monster_list.add( cube.monster+1 );
//                level_list.add( cube.level+1 );
//            }
//        }
//        monster_list.addAll( level_list );
//        return monster_list;
//    }
//
//    static public ArrayList<Integer> board_to_data_reverse(cube[][] board) {
//        ArrayList<Integer> monster_list=new ArrayList<>();
//        ArrayList<Integer> level_list=new ArrayList<>();
//        for ( cube[] row : board ) {
//            for(int j=row.length-1;j>=0;j--){
//                monster_list.add( row[j].monster+1 );
//                level_list.add( row[j].level+1 );
//            }
//        }
//        monster_list.addAll( level_list );
//        return monster_list;
//    }

    static public boolean list_contain(command[] list,command command1){
        for(command command2:list){
            if(command1.equals( command2 )){
                return true;
            }
        }
        return false;
    }
    static public boolean set_contain( HashSet<command> set, command command1){
        for(command command2:set){
            if(command1.equals( command2 )){
                return true;
            }
        }
        return false;

    }

}
