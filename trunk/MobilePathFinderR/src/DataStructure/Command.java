/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructure;

import Components.SoundModule;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

/**
 *
 * @author rajeevan
 */
public class Command {

    private static int command;
    //commands that canbe used for navigation
    public static final int LEFT = 0;
    public static final int LLEFT = 1;
    public static final int STRIGHT = 2;
    public static final int RRIGHT = 3;
    public static final int RIGHT = 4;
    public static final int BACK = 5;
    //command that canbe used at elevator front
    public static final int ARRIVED = 7;
    public static final int SCAN = 8;
    public static final int LEFTIR = 9;
    public static final int RIGHTIR = 10;
    public static final int SELECTUPORDOWN = 11;
    public static final int UP = 12;
    public static final int DOWN = 13;
    public static final int CONFORM = 14;
    public static final int WAITING = 15;
    public static final int CLOSING = 16;

    public Command(int commandNo) {
        command = commandNo;

    }

    public String toString() {

        switch (command) {
            case 0:
                return "LEFT";
            case 1:
                return "LLEFT";
            case 2:
                return "STRIGHT";
            case 3:
                return "RRIGHT";
            case 4:
                return "RIGHT";
            case 5:
                return "BACK";
            case 6:
                return "NOTHINGASSGNED YET";
            case 7:
                return "ARRIVED";
            case 8:
                return "SCAN";
            case 9:
                return "LEFTIR";
            case 10:
                return "RIGHTIR";
            case 11:
                return "SELECT_UP_OR_DOWN";
            case 12:
                return "UP";
            case 13:
                return "DOWN";
            case 14:
                return "CONFORM";
            case 15:
                return "WAITING";
            case 16:
                return "CLOSING";

        }
        return "STOP";
    }

    
    
    public int getCommand(){
        return command;
    }

    public static Command convertDirection(Direction d) {
        if(d.equals(Direction.d_90)){
            return new Command(Command.LEFT);
        }else if(d.equals(Direction.d_45)){
            return new Command(Command.LLEFT);
        }else if(d.equals(Direction.d_0)){
            return new Command(Command.STRIGHT);
        }else if(d.equals(Direction.d_315)){
            return new Command(Command.RRIGHT);
        }else if(d.equals(Direction.d_270)){
            return new Command(Command.RIGHT);
        }else{
            return new Command(Command.BACK);
        }
        
    }
}
