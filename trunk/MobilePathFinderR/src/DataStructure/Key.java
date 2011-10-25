/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructure;

/**
 *
 * @author rajeevan
 */
public class Key {
    private int pressedKey;
    
    
    
    public synchronized void setPressedKey(int key){
        this.pressedKey=key;
        notify();
    }
    
    public synchronized int getKey(){
        try {
            wait();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return pressedKey;
    }
    
    public synchronized int getWaitKey(){
        try{
            wait(10000);
        }catch(InterruptedException e){
           return 12;
        }
        return pressedKey;
    }
}
