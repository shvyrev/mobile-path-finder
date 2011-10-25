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

    //#######################################
    public static final int NAVIGATION = 1;
    public static final int ARRIVAL = 2;
    public static final int ORIENTATION = 3;
    public static final int STEPIN = 4;
    //########################################
    private Commands command;
    public static Player[] player = new Player[20];
    private boolean initialized = false;

    public SoundModule() {
        this.command = ComponentsLib.currentCommand;
        load();

    }

    private void load() {
        try {
            player[0] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/LEFT.mp3"), "audio/mpeg");
            player[1] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/LLEFT.mp3"), "audio/mpeg");
            player[2] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/STRIGHT.mp3"), "audio/mpeg");
            player[3] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/RRIGHT.mp3"), "audio/mpeg");
            player[4] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/RIGHT.mp3"), "audio/mpeg");
            player[5] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/BACK.mp3"), "audio/mpeg");
            player[7] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/ARRIVED.mp3"), "audio/mpeg");
            player[8] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/SCAN.mp3"), "audio/mpeg");
            player[9] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/L.mp3"), "audio/mpeg");
            player[10] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/R.mp3"), "audio/mpeg");
            player[11] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/SELECT.mp3"), "audio/mpeg");
            player[12] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/UP.mp3"), "audio/mpeg");
            player[13] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/DOWN.mp3"), "audio/mpeg");
            player[14] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/WAIT.mp3"), "audio/mpeg");
            player[15] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/STEPIN.mp3"), "audio/mpeg");
            player[16] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/CLOSE.mp3"), "audio/mpeg");
        } catch (MediaException e) {
            ComponentsLib.keyScanner.printException("MedEx on loading");
        } catch (IOException e) {
            ComponentsLib.keyScanner.printException("IOEx on loading");
        }
    }

    private void initilize(int commandNo) {
        initialized=false;
        int start;
        int end;
        if (commandNo >= 0 && commandNo <= 6) {
            start = 0;
            end = 5;
        } else if (commandNo >= 7 && commandNo <= 10) {
            start = 7;
            end = 10;
        } else if (commandNo >= 11 && commandNo <= 13) {
            start = 11;
            end = 13;
        } else {
            start = 14;
            end = 16;
        }

        for (int i = 0; i < start; i++) {
            try {
                player[i].deallocate();
            } catch (NullPointerException e) {
            }
        }
        for(int i=start;i<=end;i++){
            try {
                player[i].prefetch();
            } catch (MediaException ex) {
                ex.printStackTrace();
            }
        }
        initialized=true;
    }

    public void play_Sound() {

        int commandNo = 0;

        //wait for command to notify when it updated
        synchronized (command) {
            try {
                command.wait();
            } catch (InterruptedException ex) {
            }
        }
        try {
            player[commandNo].stop();
        } catch (MediaException ex) {
        } catch (NullPointerException e) {
        }
        //commandNo should be updated after the command updated and notified
        commandNo = command.getCommand();

        //sound modu
        if (commandNo == 8 || commandNo == 11 || commandNo == 14) {
            initilize(commandNo);
        }

        try {
            if (initialized) {
                player[commandNo].start();
            }
        } catch (MediaException ex) {
            ComponentsLib.keyScanner.printException("Sound:MediaEx on playing");
        } catch (NullPointerException e) {
            ComponentsLib.keyScanner.printException("Sound:NullEx on playing");
        }


    }

    public void run() {

        initialized = false;
        this.initilize(0);
        while (true) {
            this.play_Sound();
        }
    }

    public boolean isInitialized() {
        return this.initialized;
    }
}
