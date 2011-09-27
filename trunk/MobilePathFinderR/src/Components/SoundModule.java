/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import DataStructure.Command;
import java.io.IOException;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

/**
 *
 * @author rajeevan
 */
public class SoundModule {

    //private VolumeControl volume;
    // private Player player;
    public static Player[] player = new Player[20];

    public SoundModule() {
        initilize();
    }

    public void initilize() {
        try {
            player[0] = Manager.createPlayer(getClass().getResourceAsStream("LLEFT.wav"), "audio/x-wav");
            player[1] = Manager.createPlayer(getClass().getResourceAsStream("LEFT.wav"), "audio/x-wav");
            player[2] = Manager.createPlayer(getClass().getResourceAsStream("STRIGHT.wav"), "audio/x-wav");
            player[3] = Manager.createPlayer(getClass().getResourceAsStream("RIGHT.wav"), "audio/x-wav");
            player[4] = Manager.createPlayer(getClass().getResourceAsStream("RRIGHT.wav"), "audio/x-wav");
            player[5] = Manager.createPlayer(getClass().getResourceAsStream("BACK.wav"), "audio/x-wav");

            player[7] = Manager.createPlayer(getClass().getResourceAsStream("ARRIVED.wav"), "audio/x-wav");
            player[8] = Manager.createPlayer(getClass().getResourceAsStream("SCAN.wav"), "audio/x-wav");
            player[9] = Manager.createPlayer(getClass().getResourceAsStream("LEFT.wav"), "audio/x-wav");
            player[10] = Manager.createPlayer(getClass().getResourceAsStream("RIGHT.wav"), "audio/x-wav");
            player[11] = Manager.createPlayer(getClass().getResourceAsStream("SELECT.wav"), "audio/x-wav");
            player[12] = Manager.createPlayer(getClass().getResourceAsStream("UP.wav"), "audio/x-wav");
            player[13] = Manager.createPlayer(getClass().getResourceAsStream("DOWN.wav"), "audio/x-wav");
            player[14] = Manager.createPlayer(getClass().getResourceAsStream("FINISH.wav"), "audio/x-wav");
            player[15] = Manager.createPlayer(getClass().getResourceAsStream("BACK.wav"), "audio/x-wav");
            player[16] = Manager.createPlayer(getClass().getResourceAsStream("CLOSE.wav"), "audio/x-wav");
            for (int i = 0; i <= 16; i++) {
                player[i].prefetch();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
    }

    public void play_Sound(Command command) {

        try {
            player[command.getCommand()].start();
        } catch (NullPointerException e) {
            System.out.println("null pointer exception : playing sound");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("error on play sound");
        }
    }
}
