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

    private Commands command;
    public static Player[] player = new Player[20];
    private int mode = NAVIGATION;
    public static final int NAVIGATION = 1;
    public static final int ORIENTATION = 2;
    public static final int STEPIN = 3;
    private boolean initialized = false;

    public SoundModule() {
        this.command = ComponentsLib.currentCommand;
    }

    public void setCommand(Commands c) {
        this.command = c;
    }

    private void initilize() {
        initialized = false;
        ComponentsLib.keyScanner.printException("Sound:INITIALIZING");
        try {
            if (mode == NAVIGATION) {
                player[0] = Manager.createPlayer(getClass().getResourceAsStream("LEFT.wav"), "audio/x-wav");
                player[1] = Manager.createPlayer(getClass().getResourceAsStream("LLEFT.wav"), "audio/x-wav");
                player[2] = Manager.createPlayer(getClass().getResourceAsStream("STRIGHT.wav"), "audio/x-wav");
                player[3] = Manager.createPlayer(getClass().getResourceAsStream("RRIGHT.wav"), "audio/x-wav");
                player[4] = Manager.createPlayer(getClass().getResourceAsStream("RIGHT.wav"), "audio/x-wav");
                player[5] = Manager.createPlayer(getClass().getResourceAsStream("BACK.wav"), "audio/x-wav");
                player[7] = Manager.createPlayer(getClass().getResourceAsStream("ARRIVED.wav"), "audio/x-wav");
                player[8] = Manager.createPlayer(getClass().getResourceAsStream("SCAN.wav"), "audio/x-wav");
                for (int i = 0; i <= 8; i++) {
                    if (i == 6) {
                        continue;
                    }
                    player[i].prefetch();
                }
                initialized = true;
                mode = ORIENTATION;

            } else if (mode == ORIENTATION) {
                for (int i = 0; i <= 7; i++) {
                    if (i == 6) {
                        continue;
                    }
                    player[i].deallocate();
                }

                player[9] = Manager.createPlayer(getClass().getResourceAsStream("L.wav"), "audio/x-wav");
                player[10] = Manager.createPlayer(getClass().getResourceAsStream("R.wav"), "audio/x-wav");
                player[11] = Manager.createPlayer(getClass().getResourceAsStream("SELECT.wav"), "audio/x-wav");
                player[12] = Manager.createPlayer(getClass().getResourceAsStream("UP.wav"), "audio/x-wav");
                player[13] = Manager.createPlayer(getClass().getResourceAsStream("DOWN.wav"), "audio/x-wav");

                for (int i = 9; i <= 13; i++) {
                    player[i].prefetch();
                }

                initialized = true;
                mode = STEPIN;

            } else if (mode == STEPIN) {
                for (int i = 7; i <= 10; i++) {
                    if (i == 6) {
                        continue;
                    }
                    player[i].deallocate();
                }


                player[14] = Manager.createPlayer(getClass().getResourceAsStream("WAIT.wav"), "audio/x-wav");
                player[15] = Manager.createPlayer(getClass().getResourceAsStream("STEPIN.wav"), "audio/x-wav");
                player[16] = Manager.createPlayer(getClass().getResourceAsStream("CLOSE.wav"), "audio/x-wav");
                for (int i = 11; i <= 16; i++) {
                    player[i].prefetch();
                }
                initialized = true;
                mode = NAVIGATION;
            }

            ComponentsLib.keyScanner.printException("Sound:INITIALIZED");
        } catch (IOException ex) {
            ComponentsLib.keyScanner.printException("Sound:IOEx on init");
        } catch (MediaException ex) {
            ComponentsLib.keyScanner.printException("Sound:MediaEx on init");
        } catch (NullPointerException e) {
            ComponentsLib.keyScanner.printException("Sound:NullEx on init");
        }
    }

    public void play_Sound() {

        int commandNo = 0;

        synchronized (command) {
            try {
                command.wait();
            } catch (InterruptedException ex) {
            }
        }

        //seems not working
        
        //check weather previous one finished playing
        if (player[commandNo].getState() == Player.STARTED) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }
        //commandNo should be updated after the command updated and notified
        commandNo = command.getCommand();
        try {
            if (initialized) {

                player[commandNo].start();
            }
        } catch (MediaException ex) {
            ComponentsLib.keyScanner.printException("Sound:MediaEx on playing");
        } catch (NullPointerException e) {
            ComponentsLib.keyScanner.printException("Sound:NullEx on playing");
        }

        if (commandNo == 8) {
            initilize();
            ComponentsLib.keyScanner.printException("Sound:ORI_INITIALIZED");
        } else if (commandNo == 12||commandNo==13) {
            initilize();
            ComponentsLib.keyScanner.printException("Sound:STEP_INITIALIZED");
        }
    }

    public void run() {
        mode = NAVIGATION;
        initialized = false;
        this.initilize();
        while (true) {
            this.play_Sound();
        }
    }

    public boolean isInitialized() {
        return this.initialized;
    }
}
