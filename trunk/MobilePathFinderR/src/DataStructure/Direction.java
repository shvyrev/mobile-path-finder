/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructure;

/**
 *
 * @author rajeevan
 */
public class Direction {

    private int currentDirection;
    
    public final static Direction d_0 = new Direction(0);
    public final static Direction d_45 = new Direction(1);
    public final static Direction d_90 = new Direction(2);
    public final static Direction d_135 = new Direction(3);
    public final static Direction d_180 = new Direction(4);
    public final static Direction d_225 = new Direction(5);
    public final static Direction d_270 = new Direction(6);
    public final static Direction d_315 = new Direction(7);
    
    private final static double piBy4 = 1;
    private final static double piBy8 = 0.41421;
    private final static double TpiBy8 = 2.4142;

    public Direction(int value) {
        this.currentDirection = value;
    }

    public int getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(int CurrentDirection) {
        this.currentDirection = CurrentDirection;
    }

    

    public static Direction getDirection(int startX, int startY, int endX, int endY) {
        float m;
        if (endX != startX) {
            m = ((endY - startY) / (endX - startX));
        } else {
            m = 5;
        }
        if (m >= -piBy8 && m < piBy8) {
            
            if (endX >= startX) {
                return Direction.d_0;
            } else {
                return Direction.d_180;
            }

        } else if (m >= piBy8 && m < TpiBy8) {
            //NORTHEAST && SOUTHWEST
            if (endX >= startX) {
                return Direction.d_45;
            } else {
                return Direction.d_225;
            }
        } else if (m >= TpiBy8 || m <= -TpiBy8) {
            //NORTH && SOUTH
            if (endY >= startY) {
                return Direction.d_90;
            } else {
                return Direction.d_270;
            }
        } else {
            //NORTHWEST && SOUTHEAST
            if (endX >= startX) {
                return Direction.d_315;
            } else {
                return Direction.d_135;
            }
        }


    }
    
    public static Direction getDirection(Direction start,Direction end){
        int stdir=start.getCurrentDirection();
        int enddir=end.getCurrentDirection();
        
        int diff=enddir-stdir;
        diff=diff<0?(8+diff):diff;
        
        return new Direction(diff);
    }

    public String toString() {
        switch (currentDirection) {
            case 0:
                return "0d";
            case 1:
                return "45d";
            case 2:
                return "90d";
            case 3:
                return "135d";
            case 4:
                return "180d";
            case 5:
                return "225d";
            case 6:
                return "270d";
            case 7:
                return "315d";
        }
        return "no Direction";
    }
    
    public boolean equals(Object o){
        if(o!=null && o instanceof Direction ){
            Direction d=(Direction)o;
            return (d.currentDirection==this.currentDirection)?true:false;
        }else{
            return false;
        }
    }
}
