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
        while (true) {
            synchronized(p){
            try {
                System.out.println("waiting for p to notify");
                p.wait();
                System.out.println("pathprocessor notified");
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
            
            if (p.isUpdated()) {
                this.updatePath();
                p.setUsed();
                }
            
            }
                synchronized (m) {
                    m.notify();
                    System.out.println("map notified with pathprocessor");
                }
        }
    }

    private void updatePath() {
            pathFinder.findPath(p.getCurrentX(), p.getCurrentY(), DestX, DestY);
    }
}
