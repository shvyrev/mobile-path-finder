/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.utils;

/**
 *
 * @author rajeevan
 */
import Components.MapLib;
import DataStructure.Map;
import java.util.Random;

public class CordinateServer implements Runnable {

    Map m;
    int currentX, currentY;
    boolean updated;
    
    public CordinateServer(int x, int y) {
        
        this.currentX = x;
        this.currentY = y;
        this.m = MapLib.map;
    }

    public void run() {
        Random r = new Random(System.currentTimeMillis());
        Random s= new Random(System.currentTimeMillis());
        
        int count=0;
        while (true) {
            int a=s.nextInt(100);
            int x,y;
            if(a<=60){
                x = (r.nextInt(2)) ;
                y = (r.nextInt(2)) ;
            }else{
                x=(r.nextInt(2)-1);
                y=(r.nextInt(2)-1);
            }
            
            if(x==0&& y==0){
                continue;
            }
            if(!m.isBlocked(currentX+x, currentY+y).booleanValue()){
                currentX+=x;
                currentY+=y;
                updated=true;
                
                try {
                    System.out.println("CordinateServer "+count+ " x="+currentX+" y="+currentY);
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    System.out.println("Waken up");
                }
            }
            
         count++;
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
