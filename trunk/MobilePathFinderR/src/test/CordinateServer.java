/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author rajeevan
 */
import DataStructure.Map;
import java.util.Random;

public class CordinateServer implements Runnable {

    Map m;
    int currentX, currentY;
    boolean updated;
    
    public CordinateServer(int x, int y, Map m) {
        this.currentX = x;
        this.currentY = y;
        this.m = m;
    }

    public void run() {
        Random r = new Random(System.currentTimeMillis());
        while (true) {
            int x = (r.nextInt(3)-1) ;
            int y = (r.nextInt(3)-1) ;
            if(x==0&& y==0){
                continue;
            }
            if(!m.isBlocked(currentX+x, currentY+y).booleanValue()){
                currentX+=x;
                currentY+=y;
                updated=true;
                System.out.println("Message:"+currentX+" "+currentY+" "+Thread.currentThread());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    System.out.println("Waken up");
                }
            }
            

        }

    }

    public int[] getCoordinate() {
        synchronized (this) {
            updated=false;
            return new int[]{currentX, currentY};
            
        }
    }
    
    public boolean hasUpdate(){
        return this.updated;
    }
}
