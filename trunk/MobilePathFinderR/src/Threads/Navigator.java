/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import Components.MapLib;
import Components.Person;
import Components.SoundModule;
import DataStructure.Direction;
import DataStructure.Map;
import DataStructure.NavDirCommand;


/**
 *
 * @author rajeevan
 */
public class Navigator implements Runnable{
    
    NavDirCommand navDir;
    SoundModule s;
    Person p;
    Map m;
    
  public Navigator(Person p){
      m=MapLib.map;
      s=new SoundModule();
      this.p=p;
      navDir=new NavDirCommand(0);
  }
  

   public void run() {
       int count=0;
        while(true){
            synchronized(m){
                try {
                    m.wait();
                } catch (InterruptedException ex) {
                    System.out.println("NavigatorInterrupted");
                }
                updateCommandDirection();
                
                System.out.println("Navigator:"+count +"command updated");
            }
        }
        
    }
    
    private void updateCommandDirection(){
        synchronized(p){
        System.out.println(p);
        Direction currentDirection=p.getDirection();
        System.out.println("current person dir:="+currentDirection);
        Direction pathDirection=m.pathStartingDirection();
        System.out.println("current path starting dir:="+pathDirection);
        Direction difference=Direction.getDirection(currentDirection, pathDirection);
        navDir=NavDirCommand.convertDirection(difference);
        s.play_Sound(navDir);
        System.out.println(navDir);
        
        }
        
    }
    
    public NavDirCommand getNavDir(){
        return this.navDir;
    }
}
