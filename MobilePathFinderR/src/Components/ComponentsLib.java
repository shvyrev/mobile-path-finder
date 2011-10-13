/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import DataStructure.CartisianMap;
import DataStructure.Commands;
import DataStructure.Coordinate;
import DataStructure.Key;
import DataStructure.Map;
import test.utils.RandomMapGen;

/**
 *
 * @author rajeevan
 */
public class ComponentsLib {
    
       public static Key pressedKey;
    
   public static EventCanvas keyScanner;
   
   public static Commands currentCommand;
   public static SoundModule soundModule;
   
   public static Coordinate coordinate;
    public  static Map ENTC;
    public final static Map RANDOM;
    
    
    
    static{
        Boolean[][] floor;
        floor=new Boolean[36][17];
        fillFloor(0, 0, 35, 16, floor, Boolean.TRUE);
        fillFloor(28, 0, 35, 10,floor,Boolean.FALSE);///wall and stonebench
        fillFloor(0, 11, 5,16, floor,Boolean.FALSE);// reception table
        fillFloor(10,6,17,8,floor,Boolean.FALSE); // virtual obstacle
       ENTC=new CartisianMap(floor,3,4,2,2);
       
       
        
       pressedKey=new Key();
       currentCommand=new Commands(0);
       
       soundModule=new SoundModule();
       
       coordinate=new Coordinate();
       
       RANDOM=RandomMapGen.randomMap(5,5);
       
    }

    
    
   private static void fillFloor(int startx,int starty,int endx,int endy,Boolean[][] floor,Boolean value){
        for(int x=startx;x<=endx;x++){
            for(int y=starty;y<=endy;y++ ){
                floor[x][y]=value;
            }
        }
    }
   


}
