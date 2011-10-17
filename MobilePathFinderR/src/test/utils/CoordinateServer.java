/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.utils;

/**
 *
 * @author rajeevan
 */
import Components.Communicatable;
import Components.ComponentsLib;
import DataStructure.Coordinate;
import DataStructure.Map;
import java.util.Random;

public class CoordinateServer implements Communicatable {

    Map m;
    int currentX, currentY;
    private Coordinate coordinate;


    public CoordinateServer(int x, int y,Map m) {
        this.currentX = x;
        this.currentY = y;
        this.m = m;
        this.coordinate=ComponentsLib.coordinate;
    }

    public void run() {
        Random r = new Random(System.currentTimeMillis());
        Random s = new Random(System.currentTimeMillis());


        while (true) {
            int a = s.nextInt(100);
            int x, y;
            if (a <= 80) {
                x = (r.nextInt(2));
                y = (r.nextInt(2));
            } else {
                x = (r.nextInt(2) - 1);
                y = (r.nextInt(2) - 1);
            }

            if (x == 0 && y == 0) {
                continue;
            }
            

            if (!m.isBlocked(currentX + x, currentY + y).booleanValue()) {
             
                    currentX += x;
                    currentY += y;
                    coordinate.setCoordinate(currentX, currentY);
                    //System.out.println("CordinateServer  x=" + currentX + " y=" + currentY);
                try {
                   
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    //System.out.println("Waken up");
                }
            }



        }

    }

    public void receiveCoordinate() {
    }

    public void changeMode(int mode) {
       
    }

    public void changeSubMode(int mode) {
        
    }

    public void elevatorControlFlow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public char receiveChar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String receiveData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean sendExitSeq() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean senddata(String comm) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void turnOff() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void changeMode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void changeSubMode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
