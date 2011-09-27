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
    
    public static final int TURNOFF=0;
    public static final int INITIATE = 1;
    public static final int HAND = 2;
    public static final int ELEV=3;
    
    public static final String HANDHELD="btspp://1886ACE07EA6:1;authenticate=false;encrypt=false;master=false";
    public static final String ELEVATOR="btspp://001106220300:1;authenticate=false;encrypt=false;master=false";
    

    public BluetoothModule(String btUrl) {
        this.btUrl = btUrl;
        this.coordinate =ComponentsLib.coordinate;
        this.f = ComponentsLib.f;
    }
    //remove after test
    Form f;
    //
    public void run() {
        
        while (true) {
                if(mode==INITIATE){
                    this.connect();
                }else if(mode==HAND){
                    this.receiveCoordinate();
                }else if(mode==ELEV){
                    
                }else{
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
            conn = (StreamConnection) Connector.open(btUrl, Connector.READ_WRITE);
            in = new DataInputStream(conn.openInputStream());
            out = new DataOutputStream(conn.openOutputStream());
            if(btUrl.equals(HANDHELD)){
                mode=HAND;
            }else if(btUrl.equals(ELEVATOR)){
                mode=ELEV;
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
            mode=INITIATE;
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

    public boolean sendExitSeq(){
        return this.senddata("q");
    }
    

    public void changeMode(String Url){
        if(!btUrl.equals(Url)){
            if(Url.equals(ELEVATOR)){
                btUrl=ELEVATOR;
            }else if(Url.equals(HANDHELD)){
                btUrl=HANDHELD;
            }
            mode=INITIATE;
        }
    }

    public int[] get_coordinate_data() {
        int i = 0;
        int j = 0;

        //int X_pos[] = new int[2];
       // int Y_pos[] = new int[2];
        int x, y;

        //do {
        this.senddata("x");
        x = Integer.valueOf(this.receiveData()).intValue();
        //i++;
        // } while (i < 2);

        // do {
        this.senddata("y");
        y = Integer.valueOf(this.receiveData()).intValue();
        //j++;
        //} while (j < 2);

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
                        f.append(data);
                    } else {
                        break;
                    }

                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
        f.append(data);
        return data;
    }

    public void receiveCoordinate() {
        int[] coord = this.get_coordinate_data();
        this.coordinate.setCoordinate(coord[0], coord[1]);
        f.deleteAll();
        f.append("x=" + coord[0] + "y=" + coord[1] + "return statument from person:");
    }

    public void elevatorControlFlow(){
        
    }


}
