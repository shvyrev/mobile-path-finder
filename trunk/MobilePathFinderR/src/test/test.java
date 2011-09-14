/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import test.utils.RandomMapGen;
import test.utils.CordinateServer;
import Algorithm.AStarPathFinder;
import Algorithm.PathFinder;
import Components.MapLib;
import DataStructure.Direction;
import DataStructure.Map;
import Threads.GPSmodule;
import Threads.PathProcessor;
import Components.Person;
import javax.microedition.lcdui.Form;

/**
 *
 * @author rajeevan
 */
public class test {

    public static void run(Form frm) {

        frm.append("starting..");
        frm.append("\nThe Floor:\n");


        Map m = RandomMapGen.randomMap(10, 10);
        frm.append(m.toString());
        MapLib.map = m;

        CordinateServer c = new CordinateServer(0, 0);
        Thread Coordinateserver = new Thread(c, "coordinateServer");
        Coordinateserver.start();


        Person p = new Person(0, 0, 9, 9, Direction.NORTHEAST);
        GPSmodule gps = new GPSmodule(p, c);
        Thread GPS = new Thread(gps, "gpsmodule");
        GPS.start();

        PathFinder pf = new AStarPathFinder();
        PathProcessor pathprocess = new PathProcessor(p, pf);
        Thread pathProcessThread = new Thread(pathprocess, "pathprocessor");
        pathProcessThread.start();

        frm.append("\nThe navigation Path:\n");
        while (true) {
            try {
                frm.deleteAll();
                frm.append(m.toString());
                frm.append(p.toString());
                Thread.currentThread().sleep(3000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
