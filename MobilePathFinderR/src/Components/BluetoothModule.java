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
    private String btUrl = "";
    // current connection
    StreamConnection conn = null;
    DataInputStream in = null;
    DataOutputStream out = null;

    public BluetoothModule(String btUrl, Coordinate coordinate) {
        this.btUrl = btUrl;
        this.coordinate = coordinate;
    }
    //remove after test
    Form f;

    public BluetoothModule(String btUrl, Coordinate coordinate, Form f) {
        this(btUrl, coordinate);
        this.f = f;
    }

    //
    public void run() {

        this.connect();
        while (true) {
            this.receiveCoordinate();
        }
    }

    public void connect() {
        //if(isConnected == false){  
        if (btUrl == null || (btUrl.trim().compareTo("") == 0)) {

            return;
        }
        try {
            conn = (StreamConnection) Connector.open(btUrl, Connector.READ_WRITE);
            in = new DataInputStream(conn.openInputStream());
            out = new DataOutputStream(conn.openOutputStream());

        } catch (IOException e) {
            close();
        }
    }

    public void close() {
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
            return true;
        } catch (IOException ex) {
        } catch (NullPointerException e) {
            System.out.println(e);
        }
        return false;
    }

    public void setURL(String URL) {
        this.btUrl = URL;
    }

    public int[] get_coordinate_data() {
        int i = 0;
        int j = 0;

        int X_pos[] = new int[2];
        int Y_pos[] = new int[2];
        this.senddata("ready");
        do {
            this.senddata(i + 1 + "th X");
            X_pos[i] = Integer.valueOf(this.getData()).intValue();
            i++;
        } while (i < 2);

        do {
            this.senddata(j + 1 + "th Y");
            Y_pos[j] = Integer.valueOf(this.getData()).intValue();
            j++;
        } while (j < 2);

        int posi[] = new int[2];
        posi[0] = X_pos[0] * 10 + X_pos[1];
        posi[1] = Y_pos[0] * 10 + Y_pos[1];
        return posi;
    }

    public String getData() {
        char dd = 'X';
        int num = 0;
        boolean status = false;

        while (!status) {
            try {

                if (in.available() != 0) {
                    num = in.read();

                    dd = (char) num;
                    status = true;
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
        return String.valueOf(dd);
    }

    public void receiveCoordinate() {
        int[] coordinate = this.get_coordinate_data();
        this.coordinate.setCoordinate(coordinate[0], coordinate[1]);
        f.deleteAll();
        f.append("x=" + coordinate[0] + "y=" + coordinate[1] + "return statument from person:");
    }
}
