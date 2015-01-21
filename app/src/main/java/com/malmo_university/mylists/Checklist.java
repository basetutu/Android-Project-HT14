package com.malmo_university.mylists;

import java.util.ArrayList;

/**
 * Created by Saeed on 18-01-15.
 */
public class Checklist {
    String name;
    String date_added;
    private final String last_accessed;
    ArrayList<Item> mItems = new ArrayList<Item>();
    ArrayList<Profile> mUsers = new ArrayList<Profile>();

    public Checklist(String name, String date_added, String last_accessed) {
        this.name = name;
        this.date_added = date_added;
        this.last_accessed = last_accessed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public ArrayList<Item> getmItems() {
        return mItems;
    }

    public void setmItems(ArrayList<Item> mItems) {
        this.mItems = mItems;
    }

    public ArrayList<Profile> getmUsers() {
        return mUsers;
    }

    public void setmUsers(ArrayList<Profile> mUsers) {
        this.mUsers = mUsers;
    }
}
