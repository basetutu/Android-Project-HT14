package com.malmo_university.mylists;

import java.util.ArrayList;

/**
 * Created by Saeed on 18-01-15.
 */
public class Checklist {
    String name;
    String date_created;
    ArrayList<Item> mItems = new ArrayList<Item>();
    ArrayList<Profile> mUsers = new ArrayList<Profile>();

    public Checklist(String name) {
        this.name = name;
        this.date_created = FirebaseController.getTimestamp();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
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
