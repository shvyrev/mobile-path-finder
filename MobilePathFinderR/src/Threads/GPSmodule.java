/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import Components.Person;
import Exceptions.WalkingDistanceError;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.Form;
import test.utils.CordinateServer;

/**
 *
 * @author rajeevan
 */
public class GPSmodule implements Runnable {

    public boolean isActive = false;
    public boolean isConnected = false;

    // current bluetooth device
    private String btUrl = "";
    private String btName = "";
    public String error = "No Error";
    // state
    public final static byte STATE_SEARCH_SENTENCE_BEGIN = 0;
    public final static byte STATE_READ_DATA_TYPE = 1;
    public final static byte STATE_READ_SENTENCE = 2;
    // current connection
    StreamConnection conn = null;
    DataInputStream in = null;
    DataOutputStream out = null;
    Person p;
    Form f;

    public GPSmodule(Person p, String btUrl, String btName) {
        this.p = p;
        this.btName=btName;
        this.btUrl=btUrl;
    }

    public GPSmodule(Person p,String url){
        this(p,url,"");
    }
    public void init() {
    }

    public GPSmodule(Person p,String url,Form f){
        this(p,url);
        this.f=f;
    }

    public void run() {
        int count = 0;
        this.connect();
        while (true) {
            synchronized (p) {
                int[] coordinate = this.get_coordinate();
                System.out.println();
                try {
                    System.out.println("GPS: " + count + " start to update");
                    p.updatePosition(coordinate[0], coordinate[1]);
                    f.append("x="+coordinate[0]+"y="+coordinate[1]);
                    p.notify();
                    System.out.println("GPS: " + count + " Person updated " + coordinate[0] + " " + coordinate[1]);
                } catch (WalkingDistanceError ex) {
                }
                count++;
            }
            //}

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

//    public void run() {
//        isActive = true;
//        while (isActive) {
//            try {
//                // check if connection is still open
//                if (!isConnected && isActive) {
//                    // connect to gps device
//                    connect();
//                } else {
//                }
//            } catch (Throwable t) {
//                error = "run:" + t.toString();
//                close();
//            }
//        }
//        close();
//        isActive = false;
//    }

    public void connect() {
        if (btUrl == null || (btUrl.trim().compareTo("") == 0)) {
            isConnected = false;
            return;
        }
        try {
            conn = (StreamConnection) Connector.open(btUrl, Connector.READ_WRITE);
            in = new DataInputStream(conn.openInputStream());
            out = new DataOutputStream(conn.openOutputStream());
            isConnected = true;
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

    public void setURL(String name, String URL) {
        this.btName = name;
        this.btUrl = URL;
    }

    public int[] get_coordinate() {
        int i = 0;
        int j = 0;

        int X_pos[] = new int[2];
        int Y_pos[] = new int[2];
        this.senddata("ready");
        do {
            this.senddata(i + 1 + "th X");
            try{
            X_pos[i] = Integer.valueOf(this.getData()).intValue();
            i++;
            }catch(NumberFormatException e){
                this.senddata("error");
            }
        } while (i < 2);

        do {
            this.senddata(j + 1 + "th Y");
            try{
            Y_pos[j] = Integer.valueOf(this.getData()).intValue();
            j++;
            }catch(NumberFormatException e){
                this.senddata("error");
            }
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
