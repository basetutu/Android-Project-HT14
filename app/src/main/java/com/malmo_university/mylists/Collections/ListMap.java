package com.malmo_university.mylists.Collections;

import com.malmo_university.mylists.entities.Checklist;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is a 2-in-1 collection that contains both an ArrayList and a HashMap in order to let
 * the user fetch data both by index and by key.
 *
 * This is useful when working with adapters in android in conjunction with Firebase or other
 * cloud-services that do not return indexes or positions in an array.
 *
 * Created by Martin on 27-01-2015.
 */
public class ListMap<K,V> {
    private HashMap<K,V> map;
    private ArrayList<V> list;

    public ListMap(){
        map = new HashMap<K, V>();
        list = new ArrayList<V>();
    }
    public ListMap(int size){
        map = new HashMap<K, V>(size);
        list = new ArrayList<V>(size);
    }

    public void delete(){

    }



    //////////

    // returns -1 if it was not found in the array
    protected int findWithinListArray(Checklist checklist){
        for (int i = 0 ; i < list.size() ; i++) {
            if (((Checklist)list.get(i)).getRef_id().equals(checklist.getRef_id())){
                return i;
            }
        }
        return -1;
    }



}
