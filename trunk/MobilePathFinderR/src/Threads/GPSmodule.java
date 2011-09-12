/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import Exceptions.WalkingDistanceError;
import test.CordinateServer;

/**
 *
 * @author rajeevan
 */
public class GPSmodule implements Runnable {

    CordinateServer server;
    Person p;

    public GPSmodule(Person p, CordinateServer c) {
        this.server = c;
        this.p = p;
    }

    public void run() {
        while (true) {
            if (server.hasUpdate()) {
                System.out.println("serverhas update");
                synchronized (p) {
                    /*try {
                        Thread.currentThread().sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }*/
                    int[] coordinate = server.getCoordinate();
                    System.out.println(coordinate[0]+" "+coordinate[1]);
                    try {
                        p.updatePosition(coordinate[0], coordinate[1]);
                    } catch (WalkingDistanceError ex) {
                    }
                }
            }
        }
    }
}
