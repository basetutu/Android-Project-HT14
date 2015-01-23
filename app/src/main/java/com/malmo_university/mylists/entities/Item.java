package com.malmo_university.mylists.entities;

/**
 * Created by Saeed on 18-01-15.
 */
public class Item {
    String ref_id;
    String checklist_ref_id;
    String lastModifiedBy;
    String creation_date;
    int order;
    String title;
    String note;
    boolean checked;

    public Item(String ref_id, String checklist_ref_id, String lastModifiedBy, String creation_date, int order,
                String title, String note, boolean checked) {

        this.ref_id = ref_id;
        this.lastModifiedBy = lastModifiedBy;
        this.creation_date = creation_date;
        this.order = order;
        this.title = title;
        this.note = note;
        this.checked = checked;
        this.checklist_ref_id = checklist_ref_id;
    }

    public void toggleChecked(){
        this.checked = !this.checked;
    }

    public String getChecklist_ref_id() {
        return checklist_ref_id;
    }

    public void setChecklist_ref_id(String checklist_ref_id) {
        this.checklist_ref_id = checklist_ref_id;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
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

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
