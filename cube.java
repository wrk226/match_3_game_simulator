/*
 * cube.java
 *
 * Version:
 *     1.00
 *
 */


/**
 * @author: Renke Wang
 */

public class cube {

    int level=0;
    int monster;
    int grade;
    cube(int monster){
        this.monster=monster;
        if ( config.GRADE_TYPE.equals( "xiao chu wang based" ) ){
            grade=config.GRADE_XCW[level];
        }
    }
    private cube(int level,int monster,int grade){
        this.level=level;
        this.monster=monster;
        this.grade=grade;
    }

    void level_up(){
        level+=1;
        grade=config.GRADE_XCW[level];
    }
    void remove(){
        monster=-1;
        level=0;
        grade=config.GRADE_XCW[level];
    }
    cube copy(){
        return new cube( level,monster,grade);
    }
    boolean equal(cube cube){
        return (cube.monster==monster&&cube.level==level);
    }
    boolean equals(cube cube1,cube cube2){
        return (cube1.monster==monster&&cube1.level==level
                &&cube2.monster==monster&&cube2.level==level);
    }
    @Override
    public String toString() {
        if(config.OUTPUT){
            return level+""+monster;
        }else{
        if(monster==-1){
            return "\033[1;51m" +" x "+"\033[m";
        }else {
            return ("\033[1;"+config.monster_color.get( monster )+";"+config.level_color.get( level )+"m " + monster+" \033[m");
        }}
    }
}
