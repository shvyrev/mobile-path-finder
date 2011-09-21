/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import Algorithm.PathFinder;
import DataStructure.Map;

/**
 *
 * @author rajeevan
 */
public class PathProcessor implements Runnable {

    private Person p;
    private int DestX;
    private int DestY;
    private Map m;
    private PathFinder pathFinder;

    public PathProcessor(Person p, PathFinder pf) {
        this.p = p;
        this.DestX = p.getDestX();
        this.DestY = p.getDestY();
        this.m = MapLib.map;
        this.pathFinder = pf;

    }

    public void run() {
        int count=0;
        while (true) {
            synchronized(p){
            try {
                p.wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
            
            if (p.isUpdated()) {
                this.updatePath();
                p.setUsed();
                }
            
            }
            //    System.out.println("PathProcessor "+count+" Path updated" );
                //System.out.println(m);
                synchronized (m) {
                    m.notify();
                }
           
            count++;
        }
    }

    private void updatePath() {
            pathFinder.findPath(p.getCurrentX(), p.getCurrentY(), DestX, DestY);
    }
}
