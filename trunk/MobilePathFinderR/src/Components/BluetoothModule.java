/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import DataStructure.CartisianMap;
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
public class BluetoothModule implements Communicatable {

    private Coordinate coordinate;
    private Commands currentCommand;
    private EventCanvas disp;
    // current bluetooth device
    private String btUrl = HANDHELD;
    //
    // current connection
    private int state = INITIATE;
    private int mode = HAND;
    private int subMode = IR;
    private StreamConnection conn = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private boolean isMapInitialized = true;

    public BluetoothModule(String btUrl) {
        this.btUrl = btUrl;
        this.coordinate = ComponentsLib.coordinate;
        this.currentCommand = ComponentsLib.currentCommand;
        this.disp = ComponentsLib.keyScanner;
    }

    public void run() {

        while (true) {
            if (state == INITIATE) {
                this.connect();
                
            } else if (state == CONNECTED) {
                if (mode == HAND) {
                    if (!isMapInitialized) {
                        try {
                            getMap();
                        } catch (Exception e) {
                        }
                        break;
                    }
                    disp.printMessage("Receiving Coordinate");
                    this.receiveCoordinate();
                } else if (mode == ELEV) {
                    disp.printMessage("On Elevator Control Flow");
                     
                    this.elevatorControlFlow();
                } else {
                    close();
                    break;
                }
            }
        }
    }

    private void connect() {
        boolean a;
        this.close();
        try {
            disp.printMessage("connecting.");
            conn = (StreamConnection) Connector.open(btUrl, Connector.READ_WRITE);
            in = new DataInputStream(conn.openInputStream());
            out = new DataOutputStream(conn.openOutputStream());
            state = CONNECTED;
            disp.printMessage("connected");
            Thread.sleep(100);
            if(mode==ELEV){
                currentCommand.setCommand(Commands.SCAN);
            }
        } catch (InterruptedException ex) {
            
        } catch (IOException e) {
            disp.printMessage("BT: IOEx connection");
            close();
        }
    }

    public void turnOff() {
        this.close();
        state = TURNOFF;
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

    public boolean senddata(String comm) {
        try {
            if (out == null) {
                out = new DataOutputStream(conn.openOutputStream());
            }
            out.write(comm.getBytes());
            out.flush();
            out.close();
            out = null;
            //disp.append("sent:" + comm);
            return true;
        } catch (IOException ex) {
            disp.printMessage("BT:IOEx");
        } catch (NullPointerException e) {
            disp.printMessage("BT:NulEx");
        }
        return false;
    }

    public boolean sendExitSeq() {
        return this.senddata("q");
    }

    public void changeMode(int mode) {
        if (this.mode != mode) {
            this.mode = mode;
            if (btUrl.equals(ELEVATOR)) {
                btUrl = HANDHELD;
            } else if (btUrl.equals(HANDHELD)) {
                btUrl = ELEVATOR;
            }
            this.state = INITIATE;
        }
        this.sendExitSeq();
    }

    public void changeSubMode(int submode) {
        this.subMode = submode;
        if(submode==UPDOWN){
           this.sendOrientationStopCommand();
        }
    }

    public int[] get_coordinate_data() throws NumberFormatException, IOException, InvaliedData {

        char dd = 'f';
        int x = 0;
        int y = 0;
        String data = "";

        do {
            if (in.available() != 0) {
                dd = (char) in.read();

            }
        } while (dd != 'x');


        //start to receive x coordinate value
        //disp.printCoordinate("receiving x");
        while (true) {
            if (in.available() != 0) {
                dd = (char) in.read();
                if (dd != 'e') {
                    data = data.concat(String.valueOf(dd));
                } else {
                    break;
                }
            }
        }
        x = Integer.valueOf(data).intValue();
        //disp.printCoordinate("x:" + data);

        data = "";
        do {
            if (in.available() != 0) {
                dd = (char) in.read();
            }
        } while (dd != 'y');

//start to receive x coordinate value
        //disp.printCoordinate("receiving y");
        while (true) {
            if (in.available() != 0) {
                dd = (char) in.read();
                if (dd != 'e') {
                    data = data.concat(String.valueOf(dd));
                } else if (dd == 'e') {
                    break;
                }
            }
        }
        y = Integer.valueOf(data).intValue();
        //disp.printCoordinate("y" + data);

        int posi[] = new int[2];
        posi[0] = x;
        posi[1] = y;
        return posi;
    }

    public String receiveData() {
        char dd = 'X';
        int num = 0;

        String data = "";

        while (true) {
            try {

                if (in.available() != 0) {
                    num = in.read();

                    dd = (char) num;
                    if (dd != 'e') {
                        data = data.concat(String.valueOf(dd));
                    } else {
                        break;
                    }
                }
            } catch (IOException ex) {
                disp.printMessage("BT:IOEx receive Data");
            }
        }

        //disp.append("the received data:" + data);
        return data;
    }

    public char receiveChar() {
        int num;
        char dd;
        while (true) {
            try {
                if (in.available() != 0) {
                    num = in.read();
                    dd = (char) num;
                    break;
                }
            } catch (IOException ex) {
                disp.printMessage("BT:IOEx receive chr");
            }
        }
        return dd;

    }

    public void receiveCoordinate() {
        try {
            int[] coord = this.get_coordinate_data();
            this.coordinate.setCoordinate(coord[0], coord[1]);
        } catch (NumberFormatException e) {
            disp.printMessage("BT:NFEx");
        } catch (IOException ex) {
            disp.printMessage("BT:IOEx");
        } catch (InvaliedData ex) {
            disp.printMessage("BT:InvalidData");
        }


    }

    public void elevatorControlFlow() {
        if (subMode == IR) {
            char c;
            this.sendOrientationRequest();
            while (subMode == IR) {
                c = receiveChar();
                if (c == 'l') {
                    currentCommand.setCommand(Commands.LEFTIR);
                    disp.printMessage("LEFTIR");
                } else if (c == 'r') {
                    currentCommand.setCommand(Commands.RIGHTIR);
                    disp.printMessage("RIGHTIR");
                }
            }

        } else if (subMode == UPDOWN) {
        } else if (subMode == CLOSEDOOR) {
        }
    }

    private void sendOrientationRequest() {
        this.senddata("s");
    }

    private void sendOrientationStopCommand(){
        this.senddata("z");
    }
    private void getMap() throws NumberFormatException, IOException, InvaliedData {

        //request to send map
        this.senddata(String.valueOf('m'));


        //initialization
        char dd = 'f';
        String data = "";

        int height = 0;
        int width = 0;
        Boolean[][] floor;
        int terminalX;
        int terminalY;
        int destX;
        int destY;
        int[] temp;


        //wait for 'h'
        do {
            if (in.available() != 0) {
                dd = (char) in.read();

            }
        } while (dd != 'h');


        //start to receive height
        while (true) {
            if (in.available() != 0) {
                dd = (char) in.read();
                if (dd != 'e') {
                    data = data.concat(String.valueOf(dd));
                } else {
                    break;
                }
            }
        }
        height = Integer.valueOf(data).intValue();

        //wait for 'w'
        data = "";
        do {
            if (in.available() != 0) {
                dd = (char) in.read();
            }
        } while (dd != 'w');

        //start to receive width
        while (true) {
            if (in.available() != 0) {
                dd = (char) in.read();
                if (dd != 'e') {
                    data = data.concat(String.valueOf(dd));
                } else if (dd == 'e') {
                    break;
                }
            }
        }
        width = Integer.valueOf(data).intValue();

        //initialize the map with all true values
        floor = new Boolean[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                floor[x][y] = new Boolean(true);
            }
        }


        //wait for terminal coordinate
        do {
            if (in.available() != 0) {
                dd = (char) in.read();
            }
        } while (dd != 't');

        //the terminal point coordinate receiving
        temp = this.get_coordinate_data();
        terminalX = temp[0];
        terminalY = temp[1];


        //wait for destination coordinate
        do {
            if (in.available() != 0) {
                dd = (char) in.read();
            }
        } while (dd != 'd');

        //destination coordinate receiving
        temp = this.get_coordinate_data();
        destX = temp[0];
        destY = temp[1];

        //get the invalid coordinates

        //wait for start of 'i'
        do {
            if (in.available() != 0) {
                dd = (char) in.read();
            }
        } while (dd != 'i');

        //get the number of invalid coordinates
        do {
            if (in.available() != 0) {
                dd = (char) in.read();
            }
        } while (dd != 'n');

        while (true) {
            if (in.available() != 0) {
                dd = (char) in.read();
                if (dd != 'e') {
                    data = data.concat(String.valueOf(dd));
                } else {
                    break;
                }
            }
        }
        int n = Integer.valueOf(data).intValue();

        //start of geting coordinate
        for (int z = 0; z < n; z++) {
            temp = get_coordinate_data();
            floor[temp[0]][temp[1]] = new Boolean(false);
        }

        //wait for end of connection
        do {
            if (in.available() != 0) {
                dd = (char) in.read();
            }
        } while (dd != 'e');


        ComponentsLib.ENTC = new CartisianMap(floor, destX, destY, terminalX, terminalY);
        isMapInitialized = true;
    }
}
