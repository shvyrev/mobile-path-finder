/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

/**
 *
 * @author rajeevan
 */
public class Elevator {
    private int data;
    
    public final static int ARRIVED=0;
    public final static int UP=1;
    public final static int DOWN=2;
    public final static int CLOSE=3;
    
    
    public synchronized void setData(int data){
        this.data=data;
        notify();
    }
    public synchronized int getData(){
        try {
            wait();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return data;
    }
    
}
