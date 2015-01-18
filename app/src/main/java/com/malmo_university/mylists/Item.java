package com.malmo_university.mylists;

/**
 * Created by Saeed on 18-01-15.
 */
public class Item {
    String ref_id;
    String creator;
    String date_added;
    String order;
    String title;
    String subtitle;
    String state;

    public Item(String ref_id, String creator, String date_added, String order, String title, String subtitle, String state) {
        this.ref_id = ref_id;
        this.creator = creator;
        this.date_added = date_added;
        this.order = order;
        this.title = title;
        this.subtitle = subtitle;
        this.state = state;
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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
