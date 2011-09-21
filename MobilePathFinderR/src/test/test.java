/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import test.utils.RandomMapGen;
import test.utils.CoordinateServer;
import Algorithm.AStarPathFinder;
import Algorithm.PathFinder;
import Components.MapLib;
import DataStructure.Direction;
import DataStructure.Map;
import Components.PathProcessor;
import Components.Person;
import Components.Navigator;
import javax.microedition.lcdui.Form;

/**
 *
 * @author rajeevan
 */
public class test {

    public static void run(Form frm) {

        frm.append("starting..");
        frm.append("\nThe Floor:\n");


        Map m = RandomMapGen.randomMap(15, 15);
        frm.append(m.toString());
        MapLib.map = m;

        


        Person p = new Person(0, 0, 14, 14, Direction.d_315);
     //   GPSmodule gps = new GPSmodule(p, c);
     //   Thread GPS = new Thread(gps, "gpsmodule");
     //   GPS.start();
CoordinateServer c = new CoordinateServer(0, 0,p);
        Thread Coordinateserver = new Thread(c, "coordinateServer");
        Coordinateserver.start();
        PathFinder pf = new AStarPathFinder();
        PathProcessor pathprocess = new PathProcessor(p, pf);
        Thread pathProcessThread = new Thread(pathprocess, "pathprocessor");
        pathProcessThread.start();
        
        Navigator nav=new Navigator(p);
        Thread navThread=new Thread(nav);
        navThread.start();
        

        frm.append("\nThe navigation Path:\n");
        while (true) {
            try {
                frm.deleteAll();
                frm.append(p.toString());
                frm.append(nav.getNavDir().toString());
                Thread.currentThread().sleep(3000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
