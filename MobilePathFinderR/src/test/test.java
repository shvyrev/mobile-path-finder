/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;
import Algorithm.AStarPathFinder;
import DataStructure.Direction;
import DataStructure.Path;
import DataStructure.Map;
import Threads.GPSmodule;
import Threads.Person;
import javax.microedition.lcdui.Form;
/**
 *
 * @author rajeevan
 */
public class test {
    
    public static void run(Form frm){
        
        frm.append("starting..");
        frm.append("\nThe Floor:\n");
        
        
        Map m=RandomMapGen.randomMap(15, 15);
        frm.append(m.toString());
        
        CordinateServer c=new CordinateServer(0, 0, m);
        Thread Coordinateserver =new Thread(c,"coordinateServer");
        Coordinateserver.start();
        
        
        Person p=new Person(0,0,15,15,Direction.NORTHEAST);
        GPSmodule gps=new GPSmodule(p, c);
        Thread GPS=new Thread(gps,"gpsmodule");
        GPS.start();
        
        //Path path=new AStarPathFinder(m).findPath(0, 0, 9, 9,m);

        //frm.append("\nThe navigation Path:\n");

        //frm.append(m.toString());
    }

    
}
