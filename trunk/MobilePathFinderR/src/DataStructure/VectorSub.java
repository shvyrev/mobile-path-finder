/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructure;

import java.util.Vector;

/**
 *
 * @author rajeevan
 */
public class VectorSub extends Vector {
    
    public void replace(int index,Object o){
        this.insertElementAt(o, index);
        this.removeElementAt(index+1);
    }
    
    public String toString(){
        String s="{";
        for(int x=0;x<this.size();x++){
            s+=this.elementAt(x).toString()+"\n";
        }
        s+="}\n";
        return s;
    }
}
