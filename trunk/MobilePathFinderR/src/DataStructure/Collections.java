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
    
//    
//    The steps are:
//
//    Pick an element, called a pivot, from the list.
//    Reorder the list so that all elements with values less than the pivot come before the pivot, while all elements with values greater than the pivot come after it (equal values can go either way). After this partitioning, the pivot is in its final position. This is called the partition operation.
//    Recursively sort the sub-list of lesser elements and the sub-list of greater elements.
//
//The base case of the recursion are lists of size zero or one, which never need to be sorted.
    private static void AssendingQuichSort(){
        
    }
    
    private static void Sort(int pivot){
        
    }
    
   
}
