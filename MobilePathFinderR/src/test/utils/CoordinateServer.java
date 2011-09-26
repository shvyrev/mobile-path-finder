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
import Components.ComponentsLib;
import DataStructure.Coordinate;
import DataStructure.Map;
import java.util.Random;

public class CoordinateServer implements CoordinateServable {

    Map m;
    int currentX, currentY;
    private Coordinate coordinate;

    public CoordinateServer(int x, int y, Coordinate coordinate) {
        this.currentX = x;
        this.currentY = y;
        this.m = ComponentsLib.map;
        this.coordinate=coordinate;
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
            System.out.println("CordinateServer  x=" + currentX + " y=" + currentY);

            if (!m.isBlocked(currentX + x, currentY + y).booleanValue()) {
             
                    currentX += x;
                    currentY += y;
                    coordinate.setCoordinate(currentX, currentY);

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
}
