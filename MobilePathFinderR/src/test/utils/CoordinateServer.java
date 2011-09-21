/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.utils;

/**
 *
 * @author rajeevan
 */
import Components.CoordinateServable;
import Components.MapLib;
import Components.Person;
import DataStructure.Map;
import Exceptions.WalkingDistanceError;
import java.util.Random;

public class CoordinateServer implements CoordinateServable {

    Person p;
    Map m;
    int currentX, currentY;



    public CoordinateServer(int x, int y,Person p) {
        this.p=p;
        this.currentX = x;
        this.currentY = y;
        this.m = MapLib.map;
    }

    public CoordinateServer(Person p){
        this(0,0,p);
    }

    public void run() {
        Random r = new Random(System.currentTimeMillis());
        Random s = new Random(System.currentTimeMillis());

 
        while (true) {
            int a = s.nextInt(100);
            int x, y;
            if (a <= 60) {
                x = (r.nextInt(2));
                y = (r.nextInt(2));
            } else {
                x = (r.nextInt(2) - 1);
                y = (r.nextInt(2) - 1);
            }

            if (x == 0 && y == 0) {
                continue;
            }
            //System.out.println(currentX+x);
           // System.out.println(currentY+y);
            if (!m.isBlocked(currentX + x, currentY + y).booleanValue()) {
                
                    currentX += x;
                    currentY += y;
                    synchronized(p){
                        this.updateCoordinate();
                        p.notify();
                    }
                try {
                    System.out.println("CordinateServer  x=" + currentX + " y=" + currentY);
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    //System.out.println("Waken up");
                }
            }

            
        }

    }

    public void updateCoordinate() {
        try {
            p.updatePosition(currentX, currentY);
        } catch (WalkingDistanceError ex) {
            System.out.println("error on updating coordinate");
            //ex.printStackTrace();
        }
    }

}
