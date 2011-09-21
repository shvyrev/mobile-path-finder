/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author rajeevan
 */
public class BluetoothModule implements Runnable {

    public int mode = 0;
    public boolean isActive = false;
    public boolean isConnected = false;
    
    
    // current bluetooth device
    private String btUrl = "";
    private  String btName = "";
    public String error = "No Error";
    
    // current connection
    StreamConnection conn = null;
    DataInputStream in = null;
    DataOutputStream out = null;


    
    // state
    public final static byte STATE_SEARCH_SENTENCE_BEGIN = 0;
    public final static byte STATE_READ_DATA_TYPE = 1;
    public final static byte STATE_READ_SENTENCE = 2;


    
    

    public void start() {
        if (isActive) {
            stop();
        }
        connect();
        if (isConnected) {
            isActive = true;
            Thread t = new Thread(this);
            t.start();
        }
    }

    public void stop() {
        if (isActive) {
            isActive = false;
            try {
                while (isConnected) {
                    close();
                    Thread.sleep(100);
                }
            } catch (Throwable t) {
                error = "stop:" + t.toString();
                close();
            }
        }
    }

    public void run() {
        isActive = true;
        while (isActive) {
            try {
                // check if connection is still open
                if (!isConnected && isActive) {
                    // connect to gps device
                    connect();
                } else {
                }
            } catch (Throwable t) {
                error = "run:" + t.toString();
                close();
            }
        }
        close();
        isActive = false;
    }

    public void connect() {
        //if(isConnected == false){  
        if (btUrl == null || (btUrl.trim().compareTo("") == 0)) {
            isConnected = false;
            return;
        }
        try {
            conn = (StreamConnection) Connector.open(btUrl, Connector.READ_WRITE);
            in = new DataInputStream(conn.openInputStream());
            out = new DataOutputStream(conn.openOutputStream());
            isConnected = true;
            mode = 0;
            // coding for print the BTurl & BT name in hyperterminal. only for checking
            // this.senddata(btUrl);
            //this.senddata(btName);
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
            error = "close" + t.toString();
        } finally {
            in = null;
            out = null;
            conn = null;
        }
        isConnected = false;
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
            e.printStackTrace();
        }
        return false;
    }

    public void setURL(String name,String URL){
        this.btName=name;
        this.btUrl=URL;
    }
    
    public int[] get_coordinate() {
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
}
