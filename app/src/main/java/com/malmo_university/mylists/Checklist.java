package com.malmo_university.mylists;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Saeed on 18-01-15.
 */
public class Checklist {
    private final String REF_ID = "REF_ID";
    private final String NAME = "NAME";
    private final String CREATION_DATE = "CREATION_DATE";

    String ref_id;
    String date_added;
    String name;
    HashMap<String,String> values;
    // email addresses
    ArrayList<String> users;
    // all items of the checklist
    ArrayList<Item> mItems = new ArrayList<Item>();

    //////////////////////////////////////////////////////////////////////////////////////////

    public Checklist(String ref_id, String date_added, String name, HashMap<String, String> values) {
        this.ref_id = ref_id;
        this.date_added = date_added;
        this.name = name;
        this.values = values;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, String> getValues() {
        return values;
    }

    public void setValues(HashMap<String, String> values) {
        this.values = values;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public ArrayList<Item> getmItems() {
        return mItems;
    }

    public void setmItems(ArrayList<Item> mItems) {
        this.mItems = mItems;
    }
}
