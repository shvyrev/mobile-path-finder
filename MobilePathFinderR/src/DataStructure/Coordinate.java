/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructure;

import Components.ComponentsLib;

/**
 *
 * @author rajeevan
 */
public class Coordinate {

    private int x;
    private int y;
    private boolean updated;

    public synchronized int[] getCoordinate() {
        
        if (!updated) {
            try {
                wait();
            } catch (InterruptedException ex) {
                
            }
        }
        updated = false;    
        return new int[]{x, y};
    }

    public synchronized void setCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
        updated = true;
        notify();
    }

    public boolean hasUpdate() {
        return updated;
    }
}
