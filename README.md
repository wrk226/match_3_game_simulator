# match_3_game_simulator
**This is a match three game simulator.**

It implement the match and eliminate logic of "Summon and Merge"(召唤与合成). It contains many handy and useful functions, so you can use it to build your own match 3 games.

# How to use it?
There is a briefly tutorial in the file "run.java". It gives you a galance of what the simulator does and how to use it. You may use it as the start point.


# Tutorial
```
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
```
output:

![20200922134247](https://user-images.githubusercontent.com/7517810/93918046-c87d9e80-fcd9-11ea-8fc3-8cbfc6c336a7.png)

```
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
```
output:

![20200922134846](https://user-images.githubusercontent.com/7517810/93918439-5bb6d400-fcda-11ea-973f-70deb5caddf9.png)

# The match and eliminate logic
In the simulator, each cube has two attributes: level and monster.  
In each game we will always has 3 kind of monsters.
Each monster can be one of 5 levels: 0~4.  
At the beginning of the game, all of the monsters will be level 0.  
After one movement(switch/remove cube), if there exist at least 3 same node in one line(either horizontal/vertical/diagonal) game will start matching process.  
In the matching process, game will eliminate the matched cubes and create a cube at the center.  
The new cube has the same monster type as the eliminate cube, and level get increased by 1.  
After the matching process, the sand board will has some empty slots. Program will drop some random cubes to fill the slots.  
For more detail of the logic, you may check this thread: https://www.taptap.com/topic/7481354
