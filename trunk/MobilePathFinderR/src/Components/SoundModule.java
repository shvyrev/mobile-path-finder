/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import DataStructure.NavDirCommand;
import java.io.IOException;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;

/**
 *
 * @author rajeevan
 */
public class SoundModule {

    private VolumeControl volume;
   // private Player player;
    
    private Player[] player=new Player[6];
    

    public SoundModule(VolumeControl volume) {
        this.volume = volume;
    }
    
    public SoundModule(){
       initilize();
    }

    public void initilize(){
        try {
            player[0]=Manager.createPlayer(getClass().getResourceAsStream("LLEFT.wav"), "audio/x-wav");
            player[1]=Manager.createPlayer(getClass().getResourceAsStream("LEFT.wav"), "audio/x-wav");
            player[2]=Manager.createPlayer(getClass().getResourceAsStream("STRIGHT.wav"), "audio/x-wav");
            player[3]=Manager.createPlayer(getClass().getResourceAsStream("RIGHT.wav"), "audio/x-wav");
            player[4]=Manager.createPlayer(getClass().getResourceAsStream("RRIGHT.wav"), "audio/x-wav");
            player[5]=Manager.createPlayer(getClass().getResourceAsStream("BACK.wav"), "audio/x-wav");
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
}

    public  void play_Sound(int player) {
        

    }

    public void play_Sound(NavDirCommand nav) {

       try {
            this.player[nav.getDirection()+2].start();
        }catch(NullPointerException e){
            System.out.println("null pointer exception : playing sound");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("error on play sound");
        }
    }
}
