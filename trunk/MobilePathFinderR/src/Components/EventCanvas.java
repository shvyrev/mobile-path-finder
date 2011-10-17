/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import DataStructure.Key;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author rajeevan
 */
public class EventCanvas extends Canvas implements Runnable {

    Key lastKey;
    
    public static int U=-1;
    public static int D=-2;
    
    
    
    int startY=0;
    int stringY=getHeight()/6;
    String displayString = "";

    public EventCanvas(CommandListener c) {
        this.setCommandListener(c);
        this.lastKey=ComponentsLib.pressedKey;
        Thread painter=new Thread(this);
        painter.start();
    }

    public Key getPressedKey() {
        return lastKey;
    }

    protected void keyPressed(int keyCode) {
        super.keyPressed(keyCode);
        lastKey.setPressedKey(keyCode);
    }

    protected void paint(Graphics g) {
        System.out.println("canvas:"+displayString);
        g.setColor(0xffffff);
        g.fillRect(0, startY, getWidth(), getHeight()/3);
        g.setColor(0);
        g.drawString(displayString, getWidth() / 2, stringY, Graphics.BASELINE | Graphics.HCENTER);
    }

    public  void print(String s) {
        displayString = s;
        repaint();
    }
    public void append(String s){
        displayString+="\n"+s;
        repaint();
        //notify();
    }
    
    public  synchronized void print(String s,int position){
        displayString=s;
        startY=position*getHeight()/3;
        stringY=startY+getHeight()/6;
        notify();
    }
    
    public void printCoordinate(String s){
        this.print(s, 0);
    }
    public  void printMessage(String s){
        this.print(s,1);
    }
    public void printException(String s){
        this.print(s,2);
    }

   public void run() {
        synchronized (this) {
            while (true) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                repaint();
                
            }
        }
    }
}
