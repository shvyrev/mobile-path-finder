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
    public final static Direction NORTH = new Direction(0);
    public final static Direction NORTHWEST = new Direction(1);
    public final static Direction WEST = new Direction(2);
    public final static Direction SOUTHWEST = new Direction(3);
    public final static Direction SOUTH = new Direction(4);
    public final static Direction SOUTHEAST = new Direction(5);
    public final static Direction EAST = new Direction(6);
    public final static Direction NORTHEAST = new Direction(7);

    public final static double piBy4=1;
    public final static double piBy8=0.41421;
    public final static double TpiBy8=2.4142;
    
    public Direction(int value) {
        this.currentDirection = value;
    }

    public int getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(int CurrentDirection) {
        this.currentDirection = CurrentDirection;
    }

    public void turnLeft() {
        currentDirection++;
        currentDirection %= 8;
    }

    public void turnRight() {
        currentDirection += 9;
        currentDirection %= 8;
    }

    public static Direction getDirection(int startX, int startY, int endX, int endY) {
float m;
        if(endX!=startX){
        m = ((endY - startY) / (endX - startX));
        }else{
            m=5;
        }
        if (m >= -piBy8 && m <piBy8) {
            //EAST && WEST
            if (endX >= startX) {
                return Direction.EAST;
            } else {
                return Direction.WEST;
            }

        } else if (m >= piBy8 && m < TpiBy8) {
            //NORTHEAST && SOUTHWEST
            if (endX >= startX) {
                return Direction.NORTHEAST;
            } else {
                return Direction.SOUTHWEST;
            }
        } else if (m >= TpiBy8 || m <= -TpiBy8) {
            //NORTH && SOUTH
            if (endY >= startY) {
                return Direction.NORTH;
            } else {
                return Direction.SOUTH;
            }
        } else {
            //NORTHWEST && SOUTHEAST
            if (endX >= startX) {
                return Direction.SOUTHEAST;
            } else {
                return Direction.NORTHWEST;
            }
        }


    }

    public String toString() {


        switch (currentDirection) {
            case 0:
                return "NORTH";
            case 1:
                return "NORTHWEST";
            case 2:
                return "WEST";
            case 3:
                return "SOUTHWEST";
            case 4:
                return "SOUTH";
            case 5:
                return "SOUTHEAST";
            case 6:
                return "EAST";
            case 7:
                return "NORTHEAST";
        }
        return "no Direction";
    }
}
