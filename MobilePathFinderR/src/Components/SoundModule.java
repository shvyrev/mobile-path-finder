/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import DataStructure.NavDirCommand;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;

/**
 *
 * @author rajeevan
 */
public class SoundModule {

    private VolumeControl volume;
    static Player player;

    public SoundModule(VolumeControl volume) {
        this.volume = volume;
    }
    
    public SoundModule(){
        
    }

    public  void play_Sound(String loc) {
        try {
            player = Manager.createPlayer(getClass().getResourceAsStream(loc+".wav"), "audio/x-wav");
            player.start();
        }catch(NullPointerException e){
            System.out.println("null pointer exception : playing sound");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("error on play sound");
        }

    }

    public void play_Sound(NavDirCommand nav) {
        
        
        this.play_Sound(nav.toString());
        //this.play_Sound("STRIGHT.wav");
    }
}
