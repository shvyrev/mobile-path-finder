/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructure;

/**
 *
 * @author rajeevan
 */
public class Coordinate {

    private int x;
    private int y;
    private boolean updated;

    public synchronized int[] getCoordinate() {
        updated = false;
        if (!updated) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        return new int[]{x, y};
    }

    public synchronized void setCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
        updated = true;
        System.out.println("Coordinate: x="+x+"y="+y);
        notify();
    }

    public boolean hasUpdate() {
        return updated;
    }
}
