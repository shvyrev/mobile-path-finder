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
        this.close();
        try {
            disp.printMessage("connecting.");
            conn = (StreamConnection) Connector.open(btUrl, Connector.READ_WRITE);
            in = new DataInputStream(conn.openInputStream());
            out = new DataOutputStream(conn.openOutputStream());
            state = CONNECTED;
            disp.printMessage("connected");
            Thread.sleep(100);
            
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            disp.printException("BT: IOEx connection");
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
            disp.printException("BT:IOEx");
        } catch (NullPointerException e) {
            disp.printException("BT:NulEx");
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
    }

    public int[] get_coordinate_data() throws NumberFormatException, IOException,InvaliedData {
        char dd = 'f';
        int x = 0;
        int y = 0;
        String data = "";

        do{
            if (in.available() != 0) {
                dd = (char) in.read();
            }
        } while(dd!='x') ;
        

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
                disp.printException("BT:IOEx receive Data");
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
                disp.printException("BT:IOEx receive chr");
            }
        }
        return dd;

    }

    public void receiveCoordinate() {
        try {
            int[] coord = this.get_coordinate_data();
            this.coordinate.setCoordinate(coord[0], coord[1]);
        } catch (NumberFormatException e) {
            disp.printException("BT:NFEx");
        } catch (IOException ex) {
            disp.printException("BT:IOEx");
        }catch(InvaliedData ex){
            disp.printException("BT:InvalidData");
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
                    disp.printCoordinate("LEFTIR");
                } else if (c == 'r') {
                    currentCommand.setCommand(Commands.RIGHTIR);
                    disp.printCoordinate("RIGHTIR");
                }
            }

        } else if (subMode == UPDOWN) {
        } else if (subMode == CLOSEDOOR) {
        }
    }

    private void sendOrientationRequest() {
        this.senddata("s");
    }
}
