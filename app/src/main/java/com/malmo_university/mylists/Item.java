package com.malmo_university.mylists;

/**
 * Created by Saeed on 18-01-15.
 */
public class Item {
    String ref_id;
    String checklistName;
    String creator;
    String date_added;
    int order;
    String title;
    String note;
    boolean state;

    public Item(String ref_id, String checklistName, String creator, String date_added, int order, String title, String note, boolean state) {
        this.ref_id = ref_id;
        this.creator = creator;
        this.date_added = date_added;
        this.order = order;
        this.title = title;
        this.note = note;
        this.state = state;
        this.checklistName = checklistName;
    }

    public String getChecklistName() {
        return checklistName;
    }

    public void setChecklistName(String checklistName) {
        this.checklistName = checklistName;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
