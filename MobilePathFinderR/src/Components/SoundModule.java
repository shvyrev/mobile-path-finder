/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import DataStructure.Commands;
import java.io.IOException;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

/**
 *
 * @author rajeevan
 */
public class SoundModule implements Runnable {

    //private VolumeControl volume;
    // private Player player;
    private Commands command;
    public static Player[] player = new Player[20];

    private int mode=NAVIGATION;
    
    public static final int NAVIGATION=0;
    public static final int ORIENTATION=1;
    public static final int STEPIN=2;

    
    public SoundModule(){
        this.command=ComponentsLib.currentCommand;
        initilize();
    }

    public void setCommand(Commands c){
        this.command=c;
    }
    private void initilize() {
        for (int i = 0; i <=16; i++) {
                
                player[i]=null;
            }
        try {
            if(mode==NAVIGATION){
            player[0] = Manager.createPlayer(getClass().getResourceAsStream("LLEFT.wav"), "audio/x-wav");
            player[1] = Manager.createPlayer(getClass().getResourceAsStream("LEFT.wav"), "audio/x-wav");
            player[2] = Manager.createPlayer(getClass().getResourceAsStream("STRIGHT.wav"), "audio/x-wav");
            player[3] = Manager.createPlayer(getClass().getResourceAsStream("RIGHT.wav"), "audio/x-wav");
            player[4] = Manager.createPlayer(getClass().getResourceAsStream("RRIGHT.wav"), "audio/x-wav");
            player[5] = Manager.createPlayer(getClass().getResourceAsStream("BACK.wav"), "audio/x-wav");
            player[7] = Manager.createPlayer(getClass().getResourceAsStream("ARRIVED.wav"), "audio/x-wav");
            for (int i = 0; i <= 7; i++) {
                if(i==6){
                    continue;
                }
                player[i].prefetch();
            }
            }else if(mode==ORIENTATION){
            player[8] = Manager.createPlayer(getClass().getResourceAsStream("SCAN.wav"), "audio/x-wav");
            player[9] = Manager.createPlayer(getClass().getResourceAsStream("L.wav"), "audio/x-wav");
            player[10] = Manager.createPlayer(getClass().getResourceAsStream("R.wav"), "audio/x-wav");
            for (int i = 8; i <= 10; i++) {
                player[i].prefetch();
            }
            }else if(mode==STEPIN){
                for (int i = 11; i <= 16; i++) {
                player[i].prefetch();
            }
            player[11] = Manager.createPlayer(getClass().getResourceAsStream("SELECT.wav"), "audio/x-wav");
            player[12] = Manager.createPlayer(getClass().getResourceAsStream("UP.wav"), "audio/x-wav");
            player[13] = Manager.createPlayer(getClass().getResourceAsStream("DOWN.wav"), "audio/x-wav");
            player[14] = Manager.createPlayer(getClass().getResourceAsStream("FINISH.wav"), "audio/x-wav");
            player[15] = Manager.createPlayer(getClass().getResourceAsStream("BACK.wav"), "audio/x-wav");
            player[16] = Manager.createPlayer(getClass().getResourceAsStream("CLOSE.wav"), "audio/x-wav");
            }
        } catch (IOException ex) {
            System.out.println(ex);
        } catch (MediaException ex) {
            System.out.println(ex);
        }
    }

    public void play_Sound() {

        try {
           // System.out.println("playing"+command.getCommand());
            player[command.getCommand()].start();
        } catch (NullPointerException e) {
            System.out.println("null pointer exception : playing sound");
        } catch (Exception ex) {
            
            System.out.println("error on play sound");
        }
    }

    public void run() {
        while(true){
            this.play_Sound();
        
        }
    }
    
    public void changeMode(int mode){
        this.mode=mode;
        initilize();
    }
}
