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


    public Navigator() {
        currentCommand = ComponentsLib.command;

    }

    public Command navigateCommand(Direction currentDirection, Direction pathDirection) {

        Direction difference = Direction.getDirection(currentDirection, pathDirection);
        currentCommand.setCommand(Command.convertDirection(difference));
        ComponentsLib.f.append(currentCommand.toString());
        synchronized(currentCommand){
            currentCommand.notify();
        }
        return currentCommand;
    }
}
