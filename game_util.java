/*
 * game_util.java
 *
 * Version:
 *     1.00
 *
 */


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;


/**
 * @author: Renke Wang
 *
 * This class is used to store all game functions, like match,
 */

public class game_util {
    command[] edit_command = null;
    int[][] color_matrix;
    cube[][] sand_board;
    int new_color;
    int cube_count = 0;
    int size;
    Random r;

    game_util() {
        size = config.SIZE;
        r = new Random();
        if ( config.RANDOM_SEED > 0 ) {
            r.setSeed( config.RANDOM_SEED );
        }
        if ( config.RANDOM_GENERATE ) {
            sand_board = this.get_random_board();
        } else {
            sand_board = config.INIT_BOARD;
        }
    }



    private cube[][] get_random_board() {
        cube[][] new_board = new cube[size][size];
        for ( cube[] row : new_board ) {
            for ( int i = 0; i < row.length; i++ ) {
                row[i] = new cube( r.nextInt( 3 ) );
            }
        }
        do {
            // fill all the empty blocks
            while (true) {
                get_new_cubes(new_board);
                move_down(new_board);
                if (!has_empty_block(new_board)) {
                    break;
                }
            }
            match_once(new_board, new command[]{}, 2);
            remove_high_level_cube(new_board);
        } while (has_empty_block(new_board));
        return new_board;
    }

    private void remove_high_level_cube( cube[][] board ) {
        for ( cube[] row : board ) {
            for ( cube cube : row ) {
                if ( cube.level > 0 ) {
                    cube.remove();
                }
            }
        }
    }

    private int get_random_cube() {
        return r.nextInt( 3 );
    }

    private int get_next_cube() {
        char[] queue = config.FILL_QUEUE.toCharArray();
        cube_count += 1;
        return queue[( cube_count - 1 ) % 12] - 48;
    }

    private boolean has_empty_block( cube[][] board ) {
        for ( cube[] row : board ) {
            for ( cube cube : row ) {
                if ( cube.monster == -1 ) {
                    return true;
                }
            }
        }
        return false;
    }

    private void get_new_cubes( cube[][] board ) {
        // fill the first row
        for ( int column = 0; column < size; column++ ) {
            if ( board[0][column].monster == -1 ) {
                if ( config.RANDOM_DROP ) {
                    board[0][column].monster = get_random_cube();
                } else {
                    board[0][column].monster = get_next_cube();
                }
            }
        }
    }

    private void switch_cube( cube[][] board, int row1, int column1, int row2, int column2 ) {
        cube temp = board[row1][column1];
        board[row1][column1] = board[row2][column2];
        board[row2][column2] = temp;
    }

    private boolean move_down( cube[][] board ) {
        boolean dropped = false;
        // from left to right
        for ( int column = 0; column < size; column++ ) {
            // from bottom to second row
            for ( int row_i = size - 1; row_i > 0; row_i-- ) {
                if ( board[row_i][column].monster == -1 ) {
                    // if it is empty, find the cube above it and switch
                    for ( int row_j = row_i - 1; row_j > -1; row_j-- ) {
                        if ( board[row_j][column].monster != -1 ) {
                            switch_cube( board, row_i, column, row_j, column );
                            dropped = true;
                            break;
                        }
                    }
                }
            }
        }
        return dropped;
    }

    public solution_one_match move( command[] edit_command, boolean update_board ) {
        cube[][] board;
        if ( update_board ) {
            board = sand_board;
        } else {
            board = matrix_copy( sand_board );
        }
        ArrayList<cube[][]> process = new ArrayList<>();
        // matched_cubes(several round) ArrayList<ArrayList>
        // matched_cubes_one_round ArrayList<ArrayList>
        // group={{x,y,k},{x,y,k}} ArrayList<int[]>
        match matched_cubes = new match();
        // init state
//        process.add( matrix_copy( sand_board ) );
        if ( edit_command.length == 1 ) {
            //remove
            matched_cubes
                    .add( new round()
                            .add( new group()
                                    .add( board[edit_command[0].row][edit_command[0].column] ) ) );

            board[edit_command[0].row][edit_command[0].column].remove();

        } else {
            // switch
            switch_cube( board, edit_command[0].row, edit_command[0 ].column, edit_command[1 ].row, edit_command[1 ].column );
            process.add( matrix_copy( board ) );
            matched_cubes.add( match_once( board, edit_command, 1 ) );
        }
        // state after remove/switch
        process.add( matrix_copy( board ) );
        // match during drop
        while ( has_empty_block( board ) ) {
            move_down( board );
            // drop until no empty blocks
            do {
                get_new_cubes(board);
            } while (move_down(board));
            // state after each drop
            process.add( matrix_copy( board ) );
            matched_cubes.add( match_once( board, null, 2 ) );
            // state after each match
            process.add( matrix_copy( board ) );
        }

        return new solution_one_match( matched_cubes, process );
    }

    private cube[][] matrix_copy( cube[][] board ) {
        cube[][] new_matrix = new cube[size][size];
        for ( int row = 0; row < size; row++ ) {
            for ( int column = 0; column < size; column++ ) {
                new_matrix[row][column] = board[row][column].copy();
            }
        }
        return new_matrix;
    }

    private round match_once( cube[][] board, command[] edit_command, int pair_mode ) {
        new_color = 0;
        color_matrix = new int[size][size];
        for ( int[] row : color_matrix ) {
            Arrays.fill( row, -1 );
        }
        return get_pair_lists( board, edit_command, pair_mode );
    }

    public int cal_grade( match matched_cubes ) {
        int grade = 0;
        for ( round round : matched_cubes.match ) {
            if ( config.GRADE_TYPE.equals( "score based" ) ) {
                for ( group group : round.round ) {
                    grade += ( Math.max( 1, group.group.size() - 2 ) ) * Math.pow( 3, group.group.get( 0 ).level );
                }
            } else if ( config.GRADE_TYPE.equals( "xiao chu wang based" ) ) {
                for ( group group : round.round ) {
                    int base_score = 0;
                    switch ( group.group.get( 0 ).level ) {
                        case 0:
                            base_score = 10;
                            break;
                        case 1:
                            base_score = 40;
                            break;
                        case 2:
                            base_score = 160;
                            break;
                        case 3:
                            base_score = 640;
                            break;
                        case 4:
                            base_score = 4000;
                            break;
                    }
                    grade += ( Math.max( 1, group.group.size() - 2 ) * base_score );
                }
            }
        }
        return grade;
    }

    private boolean has_pair( int row, int column, cube[][] board, String direction ) {
        if ( board[row][column].monster == -1 ) {
            return false;
        }
        switch (direction) {
            case "u":
                return (board[row][column].equals(board[row - 1][column], board[row - 2][column]));
            case "ul":
                return (board[row][column].equals(board[row - 1][column - 1], board[row - 2][column - 2]));
            case "ur":
                return (board[row][column].equals(board[row - 1][column + 1], board[row - 2][column + 2]));
            case "l":
                return (board[row][column].equals(board[row][column - 1], board[row][column - 2]));
            default:
                System.out.println(direction);
                System.out.println("error direction");
                break;
        }
        return false;
    }

    private command get_color( int row, int column, String direction ) {
        int curr_color = -1;
        command center = new command( -1, -1 );
        HashSet<command> group = new HashSet<>();
        group.add( new command( row, column ) );
        switch (direction) {
            case "u":
                curr_color = color_matrix[row - 1][column];
                curr_color = curr_color == -1 ? color_matrix[row - 2][column] : curr_color;
                center = new command(row - 1, column);
                group.add(new command(row - 1, column));
                group.add(new command(row - 2, column));
                break;
            case "ul":
                curr_color = color_matrix[row - 1][column - 1];
                curr_color = curr_color == -1 ? color_matrix[row - 2][column - 2] : curr_color;
                center = new command(row - 1, column - 1);
                group.add(new command(row - 1, column - 1));
                group.add(new command(row - 2, column - 2));
                break;
            case "ur":
                curr_color = color_matrix[row - 1][column + 1];
                curr_color = curr_color == -1 ? color_matrix[row - 2][column + 2] : curr_color;
                center = new command(row - 1, column + 1);
                group.add(new command(row - 1, column + 1));
                group.add(new command(row - 2, column + 2));
                break;
            case "l":
                curr_color = color_matrix[row][column - 1];
                curr_color = curr_color == -1 ? color_matrix[row][column - 2] : curr_color;
                center = new command(row, column - 1);
                group.add(new command(row, column - 1));
                group.add(new command(row, column - 2));
                break;
            default:
                System.out.println(direction);
                System.out.println(false);
                System.out.println("error direction?");
                break;
        }
        //if curr cube no color
        if ( color_matrix[row][column] == -1 ) {
            // if no color in extended two cubes
            if ( curr_color == -1 ) {
                // then set new color
                curr_color = new_color;
                new_color += 1;
            } else {
                // no center generate
                center = new command( -1, -1 );
            }
        } else {
            if ( curr_color == -1 ) {
                curr_color = color_matrix[row][column];
            }
            //no center generate
            center = new command( -1, -1 );
        }
        // fill color
        for ( command point : group ) {
            color_matrix[point.row][point.column] = curr_color;
        }
        return center;
    }

    private command get_center( int row, int column, cube[][] board, String direction ) {
        // if not on board
        if ( row >= 2 && direction.equals( "u" )
                || ( row >= 2 && column >= 2 && direction.equals( "ul" ) )
                || ( row >= 2 && column < board.length - 2 && direction.equals( "ur" ) )
                || ( column >= 2 && direction.equals( "l" ) ) ) {
            if ( has_pair( row, column, board, direction ) ) {
                return get_color( row, column, direction );
            }
        }
        return new command( -1, -1 );
    }

    private HashSet<command> get_centers( int row, int column, cube[][] board ) {
        HashSet<command> centers = new HashSet<>();
        String[] directions = new String[]{"u", "ul", "ur", "l"};
        // check each direction of the curr cube
        for ( String direction : directions ) {
            command center = get_center( row, column, board, direction );
            if ( !center.equals( new command( -1, -1 ) ) ) {
                centers.add( center );
            }
        }
        return centers;
    }

    private round get_pair_lists( cube[][] board, command[] edit_command, int pair_mode ) {
        HashSet<command> centers = new HashSet<>();
        // add level-up-cube place for each of the cube, also update color matrix
        for ( int row = 0; row < size; row++ ) {
            for ( int column = 0; column < size; column++ ) {
                centers.addAll( get_centers( row, column, board ) );
            }
        }
        // traverse color matrix, get pair_lists
        ArrayList<Integer> color_types = new ArrayList<>();
        for ( int[] row : color_matrix ) {
            for ( int color : row ) {
                if ( !color_types.contains( color ) && color != -1 ) {
                    color_types.add( color );
                }
            }
        }
        round pair_lists = new round();
        // group cubes based on color
        for ( int color : color_types ) {
            group temp_list = new group();
            for ( int row = 0; row < size; row++ ) {
                for ( int column = 0; column < size; column++ ) {
                    if ( color_matrix[row][column] == color ) {
                        // save cubes before match
                        temp_list.add( board[row][column].copy() );
                    }
                }
            }
            pair_lists.add( temp_list );
        }
        // update data matrix
        for ( int row = 0; row < size; row++ ) {
            for ( int column = 0; column < size; column++ ) {
                // if it has color
                if ( color_matrix[row][column] != -1 ) {
                    // if it is center, then level up(+100)
                    if ( pair_mode == 1 && config.list_contain( edit_command, new command( row, column ))) {
                        //centers are the switched blocks
                        board[row][column].level_up();
                    } else if ( pair_mode == 2 && config.set_contain( centers,new command( row, column ) )) {
                        // centers will generate during drop period
                        board[row][column].level_up();
                    } else {
                        board[row][column].remove();
                    }
                }
            }
        }
        return pair_lists;
    }

    public static void main( String[] args ) {
        game_util a = new game_util();
        for ( int i = 0; i < 100; i++ ) {
            System.out.println( a.get_next_cube() );
        }
    }

}
