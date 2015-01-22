package com.malmo_university.mylists;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Saeed on 18-01-15.
 */
public class Checklist {
    protected final String REF_ID = "REF_ID";
    protected final String NAME = "NAME";
    protected final String CREATION_DATE = "CREATION_DATE";

    String ref_id;
    String creation_date;
    String name;
    HashMap<String,String> values;
    // email addresses
    ArrayList<String> users;
    // all items of the checklist
    ArrayList<Item> mItems = new ArrayList<Item>();

    //////////////////////////////////////////////////////////////////////////////////////////

    public Checklist(String ref_id, String creation_date, String name, HashMap<String, String> values) {
        this.ref_id = ref_id;
        this.creation_date = creation_date;
        this.name = name;
        this.values = values;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
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
