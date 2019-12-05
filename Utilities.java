/*
 * File: Utilities.java
 * Author: David Hui
 * Description: Basic set of utilities that are used in this project, mostly to replicated JS functions
 */
public class Utilities{
    /**
     * Finds the index of an int in an array of int
     * @param array The array to search in
     * @param target The value that is being searched for
     * @return the index of the target in array (-1 if not found)
     */
    public static int indexOf(int[] array, int target){
        // Iterates through array, checking if value at the index matches target
        for(int i=0;i<array.length;i++){
            if(array[i] == target){
                return i;
            }
        }
        return -1;
    }
}