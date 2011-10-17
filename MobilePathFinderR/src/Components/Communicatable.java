/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

/**
 *
 * @author rajeevan
 */
public interface Communicatable extends Runnable {

    //The Bluetooth connection State
    int TURNOFF = 0;
    int INITIATE = 1;
    int CONNECTED=2;
    
    //The Bluetooth Mode
    int HAND = 0;
    int ELEV = 1;
    //The subModes of Bluetooth at ELEV mode
    int IR = 0;
    int UPDOWN = 1;
    int CLOSEDOOR = 2;
    //Bluetooth URL
    String ELEVATOR = "btspp://0015833D0A57:1;authenticate=false;encrypt=false;master=false";
    String HANDHELD = "btspp://0015833D0A57:1;authenticate=false;encrypt=false;master=false";

    //ajanthan pc:btspp://001FE1F85EB2:1;authenticate=false;encrypt=false;master=false
    //hanaheld:btspp://001106220300:1;authenticate=false;encrypt=false;master=false
    //extra:btspp://001106220296:1;authenticate=false;encrypt=false;master=false
    //elevator:btspp://001105180007:1;authenticate=false;encrypt=false;master=false
    //myBluetoothAdapter:  btspp://0015833D0A57:1;authenticate=false;encrypt=false;master=false
    
    void changeMode(int mode);

    void changeSubMode(int submode);

    void turnOff();
}
