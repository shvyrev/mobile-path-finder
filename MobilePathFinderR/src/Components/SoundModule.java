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
            player[0] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/LEFT.wav"), "audio/x-wav");
            player[1] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/LLEFT.wav"), "audio/x-wav");
            player[2] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/STRIGHT.wav"), "audio/x-wav");
            player[3] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/RRIGHT.wav"), "audio/x-wav");
            player[4] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/RIGHT.wav"), "audio/x-wav");
            player[5] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/BACK.wav"), "audio/x-wav");
            player[7] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/ARRIVED.wav"), "audio/x-wav");
            player[8] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/SCAN.wav"), "audio/x-wav");
            player[9] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/L.wav"), "audio/x-wav");
            player[10] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/R.wav"), "audio/x-wav");
            player[11] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/SELECT.wav"), "audio/x-wav");
            player[12] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/UP.wav"), "audio/x-wav");
            player[13] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/DOWN.wav"), "audio/x-wav");
            player[14] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/WAIT.wav"), "audio/x-wav");
            player[15] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/STEPIN.wav"), "audio/x-wav");
            player[16] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/CLOSE.wav"), "audio/x-wav");
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

//        initialized = false;
//        ComponentsLib.keyScanner.printException("Sound:INITIALIZING");
//        try {
//            if (commandNo == 0) {
//                player[0] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/LEFT.wav"), "audio/x-wav");
//                player[1] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/LLEFT.wav"), "audio/x-wav");
//                player[2] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/STRIGHT.wav"), "audio/x-wav");
//                player[3] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/RRIGHT.wav"), "audio/x-wav");
//                player[4] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/RIGHT.wav"), "audio/x-wav");
//                player[5] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/BACK.wav"), "audio/x-wav");
//                for (int i = 0; i <= 5; i++) {
//                    player[i].prefetch();
//                }
//                initialized = true;
//            } else if (commandNo == 8) {
//                for (int i = 0; i <= 5; i++) {
//                    try {
//                        player[i].deallocate();
//                    } catch (NullPointerException e) {
//                    }
//                }
//                player[7] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/ARRIVED.wav"), "audio/x-wav");
//                player[8] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/SCAN.wav"), "audio/x-wav");
//                player[9] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/L.wav"), "audio/x-wav");
//                player[10] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/R.wav"), "audio/x-wav");
//                for (int i = 7; i <= 10; i++) {
//                    player[i].prefetch();
//                }
//                initialized = true;
//
//            } else if (commandNo == 11) {
//                for (int i = 7; i <= 10; i++) {
//                    try {
//                        player[i].deallocate();
//                    } catch (NullPointerException e) {
//                    }
//                }
//                player[11] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/SELECT.wav"), "audio/x-wav");
//                player[12] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/UP.wav"), "audio/x-wav");
//                player[13] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/DOWN.wav"), "audio/x-wav");
//                for (int i = 11; i <= 13; i++) {
//                    player[i].prefetch();
//                }
//                initialized = true;
//            } else if (commandNo == 14) {
//
//                for (int i = 11; i <= 13; i++) {
//                    try {
//                        player[i].deallocate();
//                    } catch (NullPointerException e) {
//                    }
//                }
//                player[14] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/WAIT.wav"), "audio/x-wav");
//                player[15] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/STEPIN.wav"), "audio/x-wav");
//                player[16] = Manager.createPlayer(getClass().getResourceAsStream("./Sound/CLOSE.wav"), "audio/x-wav");
//                for (int i = 14; i <= 16; i++) {
//                    player[i].prefetch();
//                }
//                initialized = true;
//            }
//
//            ComponentsLib.keyScanner.printException("Sound:INITIALIZED");
//        } catch (IOException ex) {
//            ComponentsLib.keyScanner.printException("Sound:IOEx on init");
//        } catch (MediaException ex) {
//            ComponentsLib.keyScanner.printException("Sound:MediaEx on init");
//        } catch (NullPointerException e) {
//            ComponentsLib.keyScanner.printException("Sound:NullEx on init");
//        } finally {
//        }
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
