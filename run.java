import java.util.Arrays;

public class run {
    public static void main( String[] args ) {
        // initialize the color setting for cubes
        config.init_color();
        // set the sand board size as 6*6
        config.SIZE=6;
        // let the program random generate a init sand board.
        config.RANDOM_GENERATE=true;
        // create a game object
        game_util my_game = new game_util();
        // show the init sand board
        System.out.println(config.board_to_string(my_game.sand_board));
        // define the action: remove cube (0,3)
        command[] remove_action=new command[]{new command(0, 3)};

        // do the action and get result, result will be a list of sand board state
        // , it contains the state before & after each match during the drop
        solution_one_match result = my_game.move(remove_action, false);
        System.out.println("remove cube (0,3)");
        System.out.println(result);
        // calculate the grade
        int grade=my_game.cal_grade( result.matched_cubes );
        System.out.println("grade:"+grade+"\n");
        System.out.println("============================================");

        // define the action: switch cube (0,0) and (5,5)
        command[] switch_action= new command[]{new command(0, 0),new command(5,5)};
        // do the action
        result = my_game.move(switch_action, false);
        System.out.println("switch cube (0,0) and (5,5)");
        System.out.println(result);
        // calculate the grade
        grade=my_game.cal_grade( result.matched_cubes );
        System.out.println("grade:"+grade+"\n");
        System.out.println("============================================");
    }
}
