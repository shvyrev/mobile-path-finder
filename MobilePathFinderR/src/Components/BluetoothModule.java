/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import DataStructure.Coordinate;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.Form;

/**
 *
 * @author rajeevan
 */
public class BluetoothModule implements CoordinateServable {

    Coordinate coordinate;
    // current bluetooth device
    private String btUrl = HANDHELD;
    // current connection
    private int mode = INITIATE;
    StreamConnection conn = null;
    DataInputStream in = null;
    DataOutputStream out = null;
    public static final int TURNOFF = 0;
    public static final int INITIATE = 1;
    public static final int HAND = 2;
    public static final int ELEV = 3;
    public static final String HANDHELD = "btspp://001106220296:1;authenticate=false;encrypt=false;master=false";
    public static final String ELEVATOR = "btspp://001106220300:1;authenticate=false;encrypt=false;master=false";
    
    //ajanthan pc:btspp://001FE1F85EB2:1;authenticate=false;encrypt=false;master=false
    //hanaheld:btspp://001106220300:1;authenticate=false;encrypt=false;master=false
    //extra:btspp://001106220296:1;authenticate=false;encrypt=false;master=false
    //elevator:
    

    public BluetoothModule(String btUrl) {
        this.btUrl = btUrl;
        this.coordinate = ComponentsLib.coordinate;
        this.f = ComponentsLib.f;
    }
    //remove after test
    Form f;
    //

    public void run() {

        while (true) {
            if (mode == INITIATE) {
                this.connect();
            } else if (mode == HAND) {
                f.append("start to receiving coordinate");
                this.receiveCoordinate();
            } else if (mode == ELEV) {
            } else {
                close();
                break;
            }
        }
    }

    private void connect() {
        //if(isConnected == false){  
        if (btUrl == null || (btUrl.trim().compareTo("") == 0)) {
            return;
        }
        try {
            f.deleteAll();
            f.append("trying to connect to " + btUrl);
            conn = (StreamConnection) Connector.open(btUrl, Connector.READ_WRITE);
            in = new DataInputStream(conn.openInputStream());
            out = new DataOutputStream(conn.openOutputStream());
            f.append("connected");
            if (btUrl.equals(HANDHELD)) {
                mode = HAND;
                f.append("bluetooth mode: HAND");
            } else if (btUrl.equals(ELEVATOR)) {
                mode = ELEV;
            }
        } catch (IOException e) {
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
            System.out.println(t);
        } finally {
            in = null;
            out = null;
            conn = null;
            mode = INITIATE;
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
            f.append("sent:" + comm);
            return true;
        } catch (IOException ex) {
        } catch (NullPointerException e) {
            System.out.println(e);
        }
        return false;
    }

    public boolean sendExitSeq() {
        return this.senddata("q");
    }

    public void changeMode(String Url) {
        if (!btUrl.equals(Url)) {
            if (Url.equals(ELEVATOR)) {
                btUrl = ELEVATOR;
            } else if (Url.equals(HANDHELD)) {
                btUrl = HANDHELD;
            }
            mode = INITIATE;
        }
    }

    public int[] get_coordinate_data() {
        char dd = 'f';
        int x = 0;
        int y = 0;

        String data = "";


        try {
            //senddata("start");
            //wait to get char 'x'
            do {
                if (in.available() != 0) {
                    dd = (char)in.read();
                    f.append(String.valueOf(dd));
                }
            } while (dd != 'x');
            senddata("s");
            //get the x coordinate and put it in to the variable x
            while (true) {
                if (in.available() != 0) {

                    dd = (char)in.read();
                    if (dd != 'e') {
                        data=data.concat(String.valueOf(dd));
                        f.append("concat:"+data);
                    } else {
                        break;
                    }
                }
            }

            try{
            x = Integer.valueOf(data).intValue();
            }catch(NumberFormatException e){
                f.append("number format exception "+data);
            }
            f.append("x received:" + x);
            data = "";


            //get the y coordinate and put it in to the variable y;

            do {
                if (in.available() != 0) {
                    dd = (char)in.read();
                }
            } while (dd != 'y');
            
            while (true) {
                if (in.available() != 0) {

                    dd = (char)in.read();
                    if (dd != 'e') {
                        data=data.concat(String.valueOf(dd));
                    } else {
                        break;
                    }
                }
            }

            y = Integer.valueOf(data).intValue();
            f.append("y received:" + y);
            data = "";

        } catch (IOException ex) {
            f.append("exception on reading data");
            System.out.println(ex);
        }


        f.append("the received  x:" + x + " y:" + y);

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
                System.out.println(ex);
            }
        }

        f.append("the received data:" + data);
        return data;
    }

    public void receiveCoordinate() {
        int[] coord = this.get_coordinate_data();
        this.coordinate.setCoordinate(coord[0], coord[1]);
        f.append("x=" + coord[0] + "y=" + coord[1] + "return statument from person:");
    }

    public void elevatorControlFlow() {
    }
}
