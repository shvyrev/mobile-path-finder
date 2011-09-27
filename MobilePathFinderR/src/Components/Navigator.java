/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import DataStructure.Command;
import DataStructure.Direction;

/**
 *
 * @author rajeevan
 */
public class Navigator {

    Command currentCommand;
    SoundModule sound=new SoundModule();
    

    public Command navigateCommand(Direction currentDirection, Direction pathDirection) {

        Direction difference = Direction.getDirection(currentDirection, pathDirection);
        currentCommand = Command.convertDirection(difference);
        sound.play_Sound(currentCommand);
        return currentCommand;
    }
}
