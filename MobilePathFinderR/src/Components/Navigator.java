/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import DataStructure.Direction;
import DataStructure.Map;
import DataStructure.NavDirCommand;
import javax.microedition.lcdui.Form;

/**
 *
 * @author rajeevan
 */
public class Navigator {

    NavDirCommand navDir;
    private boolean updated;
    SoundModule s;

    //remove after testing phase
    Form f;
    //

    public Navigator() {
        s = new SoundModule();
        navDir = new NavDirCommand(0);
        this.f=ComponentsLib.f;
    }
    //have to remove after testing pahse

    //
    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

  public NavDirCommand updateCommandDirection(Direction currentDirection,Direction pathDirection) {

        Direction difference = Direction.getDirection(currentDirection, pathDirection);
        navDir = NavDirCommand.convertDirection(difference);
        this.setUpdated(true);
        s.play_Sound(navDir);
        f.append(navDir.toString());
        return navDir;
    }
}
