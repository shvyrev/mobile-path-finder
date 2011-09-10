/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructure;

import java.util.Vector;
import DataStructure.MyComparable;

/**
 *
 * @author rajeevan
 */
public class Collections {

    static Vector collection;

    public static void sort(VectorSub list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        collection = list;
        //System.out.println("befreSort");
        //System.out.println(list);
        AssendingInsertionSort();
        //System.out.println("afterSort");
        //System.out.println(list);
    }

    private static void AssendingInsertionSort() {
        MyComparable temp;
        for (int i = 1; i < collection.size(); i++) {
            //System.out.println(collection.elementAt(i));
            temp = (MyComparable) collection.elementAt(i);
            for (int j = 0; j <= i-1; j++) {
                if (((MyComparable) collection.elementAt(j)).CompareTo(temp)< 0) {
                    continue;
                } else {
                   collection.removeElementAt(i);
                   collection.insertElementAt(temp, j);
                    break;
                }
            }
        }

    }
    
   
}
