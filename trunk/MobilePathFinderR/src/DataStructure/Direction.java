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
    
    
    public final static int NORTH=0;
    public final static int NORTHWEST=1;
    public final static int WEST=2;
    public final static int SOUTHWEST=3;
    public final static int SOUTH=4;
    public final static int SOUTHEAST=5;
    public final static int EAST=6;
    public final static int NORTHEAST=7;
    

    
    private int CurrentDirection=NORTH;

    public int getCurrentDirection() {
        return CurrentDirection;
    }

    public void setCurrentDirection(int CurrentDirection) {
        this.CurrentDirection = CurrentDirection;
    }
    
    public void turnLeft(){
        CurrentDirection++;
        CurrentDirection%=8;
    }
    
    public void turnRight(){
        CurrentDirection+=9;
        CurrentDirection%=8;
    }
    
    
}
