/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import DataStructure.Commands;
import DataStructure.Coordinate;
import Exceptions.InvaliedData;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author rajeevan
 */
public class Bluetooth implements Communicatable {

    //##############################################################################################
    private static final char ERROR = '.';
    private static final char EXITSEQ = 'q';
    //The Bluetooth connection State
    private static final int TURNOFF = 0;
    private static final int INITIATE = 1;
    private static final int CONNECTED = 2;
    //The Bluetooth Mode
    private static final int HAND = 3;
    private static final int ELEV = 4;
    //The subModes of Bluetooth at ELEV mode
    private static final int IR = 5;
    private static final int UPDOWN = 6;
    private static final int CLOSEDOOR = 7;
    //The subModes of Bluetooth at HAND mode
    private static final int MAP = 8;
    private static final int COORDINATE = 9;
    private static final int IROFF = 10;
    //Bluetooth URL
    private static final String ELEVATOR = "btspp://0015833D0A57:1;authenticate=false;encrypt=false;master=false";
    private static final String HANDHELD = "btspp://0015833D0A57:1;authenticate=false;encrypt=false;master=false";
    //ajanthan pc:btspp://001FE1F85EB2:1;authenticate=false;encrypt=false;master=false
    //hanaheld:btspp://001106220300:1;authenticate=false;encrypt=false;master=false
    //extra:btspp://001106220296:1;authenticate=false;encrypt=false;master=false
    //elevator:btspp://:1;authenticate=false;encrypt=false;master=false
    //myBluetoothAdapter:  btspp://0015833D0A57:1;authenticate=false;encrypt=false;master=false
    //##############################################################################################
    private int state = INITIATE;
    private int mode = HAND;
    private int subMode = COORDINATE;
    private String btUrl = HANDHELD;
    private boolean modeChanged = false;
    EventCanvas disp;
    Commands currentCommand;
    Coordinate coordinate;
    private StreamConnection conn = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    public Bluetooth() {
        disp = ComponentsLib.keyScanner;
        currentCommand = ComponentsLib.currentCommand;
        coordinate = ComponentsLib.coordinate;
    }

    private void connect() {
        close();
        try {
            disp.printMessage("connecting.");
            conn = (StreamConnection) Connector.open(btUrl, Connector.READ_WRITE);
            in = new DataInputStream(conn.openInputStream());
            out = new DataOutputStream(conn.openOutputStream());
            state = CONNECTED;
            disp.printMessage("connected");
        } catch (IOException e) {
            disp.printMessage("BT: IOEx connection");
            close();
        }
    }

    private void close() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.close();
            }
            in = null;
            out = null;
            conn = null;
        } catch (Throwable t) {
        } finally {
            in = null;
            out = null;
            conn = null;
            state = INITIATE;
        }
    }

    public void changeMode() {
        if (mode == HAND) {
            mode = ELEV;
            btUrl = ELEVATOR;
           subMode=IR;
        } else {
            mode = HAND;
            btUrl = HANDHELD;
        }
        state = INITIATE;
        modeChanged = true;
        
    }

    public void changeSubMode() {
        if (mode == ELEV) {
            switch (subMode) {
                case IR:
                    subMode = UPDOWN;
                    break;
                case UPDOWN:
                    subMode = CLOSEDOOR;
                    break;
                default:
                    subMode = IR;
                    break;
            }
        } else {
            switch (subMode) {
                case MAP:
                    subMode = COORDINATE;
                    break;
                case COORDINATE:
                    subMode = IROFF;
                    break;
                default:
                    subMode = COORDINATE;
                    break;
            }
        }
        modeChanged = true;
    }

    public void turnOff() {
        close();
        state = TURNOFF;
        modeChanged = true;
    }

    private boolean senddata(String comm) {
        try {
            if (out == null) {
                out = new DataOutputStream(conn.openOutputStream());
            }
            out.write(comm.getBytes());
            out.flush();
            return true;
        } catch (IOException ex) {
            disp.printMessage("BT:IOEx sendData");
        } catch (NullPointerException e) {
            disp.printMessage("BT:NulEx sendData");
        }
        return false;
    }

    private char receiveChar() {
        int num;
        char temp;
        while (true) {
            try {
                if (in.available() != 0) {
                    num = in.read();
                    temp = (char) num;
                    break;
                }
            } catch (IOException ex) {
                disp.printMessage("BT:IOEx receive chr");
            } finally {
                if (modeChanged) {
                    return ERROR;
                }
            }
        }
        return temp;
    }

    private char receiveChar(char stopChar) throws IOException {
        char temp = 'f';
        do {
            if (in.available() != 0) {
                temp = (char) in.read();
            }
        } while (temp != stopChar);
        return temp;
    }

    private int receiveValue() throws IOException, NumberFormatException {
        String data = "";
        char temp = 'f';
        while (true) {
            if (in.available() != 0) {
                temp = (char) in.read();
                if (temp != 'e') {
                    data = data.concat(String.valueOf(temp));
                } else {
                    break;
                }
            }
        }
        return Integer.valueOf(data).intValue();
    }

    private boolean sendExitSeq() {
        return senddata(String.valueOf(EXITSEQ));
    }

    private void sendStartSeq() {
        senddata(String.valueOf('s'));
    }

    private int[] receiveCoordinate() throws NumberFormatException, IOException, InvaliedData {
        char temp = 'f';
        int x = 0;
        int y = 0;

        //wait for getting char x
        //return ERROR when mode changed
        temp = receiveChar('x');
        //if mode changed when waiting for x it will return null
        if (temp == ERROR) {
            return null;
        }

        //start to receive x coordinate value
        x = receiveValue();

        //wait for getting char y
        //return ERROR when mode changed
        temp = receiveChar('y');
        //if mode changed when waiting for x it will return null
        if (temp == ERROR) {
            return null;
        }

        //start to receive y coordinate value
        y = receiveValue();

        int posi[] = new int[2];
        posi[0] = x;
        posi[1] = y;
        return posi;
    }

    private void handheldFlow() {
        if (subMode == MAP) {
            disp.printMessage("MAP MODE");
            return;
        } else if (subMode == COORDINATE) {
            disp.printMessage("COORDINATE MODE");

            int[] tempCoord = {0, 0};
            
            
            //sendStartSeq();
            while(!modeChanged){
                //send start sequence 
                sendStartSeq(); 
                try {
                    tempCoord = receiveCoordinate();
                    if (!modeChanged) {
                        coordinate.setCoordinate(tempCoord[0], tempCoord[1]);
                    }else{
                        sendExitSeq();
                        break;
                    }
                } catch (NumberFormatException e) {
                    disp.printMessage("BT:NFEx handheldflow");
                } catch (IOException ex) {
                    disp.printMessage("BT:IOEx handheld flow");
                } catch (InvaliedData ex) {
                    disp.printMessage("BT:InvalidData handheld flow");
                }

            }
            sendExitSeq();
        } else if (subMode == IROFF) {
            disp.printMessage("IROFF");
        }
    }

    private void elevatorFlow() {

        int pressedKey;

        if (subMode == IR) {
            disp.printMessage("IR");
            char c;
            sendStartSeq();
            disp.printMessage("START SCANING");
            currentCommand.setCommand(Commands.SCAN);
            while (subMode == IR) {
                c = receiveChar();
                if (c == 'l') {
                    disp.printMessage("LEFTIR");
                    currentCommand.setCommand(Commands.LEFTIR);
                } else if (c == 'r') {
                    disp.printMessage("RIGHTIR");
                    currentCommand.setCommand(Commands.RIGHTIR);
                } else if (c == ERROR) {
                    return;
                }
            }
        } else if (subMode == UPDOWN) {
            disp.printMessage("UPDOWN");
            disp.printMessage("Select Up or Down");
            currentCommand.setCommand(Commands.SELECTUPORDOWN);
            pressedKey = ComponentsLib.pressedKey.getKey();
            if (pressedKey == EventCanvas.U) {
                senddata(String.valueOf('u'));
                disp.printMessage("Up selected");
                currentCommand.setCommand(Commands.UP);
            } else if (pressedKey == EventCanvas.D) {
                senddata(String.valueOf('d'));
                disp.printMessage("Down selected");
                currentCommand.setCommand(Commands.DOWN);
            }
            changeSubMode();
            
        } else if (subMode == CLOSEDOOR) {
            disp.printMessage("CLOSEDOOR");
            currentCommand.setCommand(Commands.WAITING);
                try{
                receiveChar('a');
                }catch(IOException ex){
                    disp.printMessage("BT: IOEx receiveChar");
                }
                disp.printMessage("STEPIN");
                currentCommand.setCommand(Commands.STEPIN);
                ComponentsLib.pressedKey.getKey();
                disp.printMessage("CLOSE DOOR");
                currentCommand.setCommand(Commands.CLOSING);
                ComponentsLib.pressedKey.getKey();
                this.senddata(String.valueOf('c'));
                this.state = TURNOFF;
        }

    }

    public void run() {

        while (true) {
            //disable the flag
            modeChanged = false;


            if (state == TURNOFF) {
                turnOff();
                break;
            }
            switch (state) {
                case INITIATE: {
                    connect();
                    break;
                }
                case CONNECTED: {

                    switch (mode) {
                        case HAND: {
                            handheldFlow();
                            break;
                        }
                        case ELEV:
                            elevatorFlow();
                            break;
                    }
                    break;
                }
            }
        }
    }
}
