/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import DataStructure.CartisianMap;
import DataStructure.Map;
import test.utils.RandomMapGen;

/**
 *
 * @author rajeevan
 */
public class MapLib {
    public static Map map=RandomMapGen.randomMap(36, 17);;

    static{
        Boolean[][] floor;
        floor=new Boolean[36][17];
        fillFloor(0, 0, 35, 16, floor, Boolean.TRUE);
        fillFloor(28, 0, 35, 10,floor,Boolean.FALSE);///wall and stonebench
        fillFloor(0, 11, 5,16, floor,Boolean.FALSE);// reception table
        fillFloor(10,6,17,8,floor,Boolean.FALSE); // virtual obstacle

       map=new CartisianMap(floor);

    }

    public MapLib(Map m){
        MapLib.map=m;
    }

    public MapLib(){
        map=RandomMapGen.randomMap(36, 17);
    }

    private static void fillFloor(int startx,int starty,int endx,int endy,Boolean[][] floor,Boolean value){
        for(int x=startx;x<=endx;x++){
            for(int y=starty;y<=endy;y++ ){
                floor[x][y]=value;
            }
        }
    }

}
