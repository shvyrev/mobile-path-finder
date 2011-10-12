/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import DataStructure.Commands;
import DataStructure.Direction;

/**
 *
 * @author rajeevan
 */
public class Navigator {

    public Commands navigateCommand(Direction currentDirection, Direction pathDirection) {
        Direction difference = Direction.getDirection(currentDirection, pathDirection);
        return Commands.convertDirection(difference);
        
    }
}
