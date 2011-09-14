/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructure;

/**
 *
 * @author rajeevan
 */
public class NavDirCommand {

    private int direction = 0;

    public NavDirCommand(int direction){
        this.direction=direction;
    }
    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
    public static final NavDirCommand LLEFT = new NavDirCommand(-2);
    public static final NavDirCommand RRIGHT = new NavDirCommand(2);
    public static final NavDirCommand STRIGHT = new NavDirCommand(0);
    public static final NavDirCommand LEFT = new NavDirCommand(-1);
    public static final NavDirCommand RIGHT = new NavDirCommand(1);
    public static final NavDirCommand BACK = new NavDirCommand(3);

    public String toString() {
        switch (direction) {
            case 0:
                return "STRIGHT";
            case 1:
                return "RIGHT";
            case -1:
                return "LEFT";
            case 2:
                return "RRIGHT";
            case -2:
                return "LLEFT";
            case 3:
                return "BACK";
        }
        return "STOP";
    }
}
