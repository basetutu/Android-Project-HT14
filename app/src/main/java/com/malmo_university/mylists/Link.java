package com.malmo_university.mylists;

/**
 * Created by Saeed on 18-01-15.
 */
public class Link {
    // Links will be ordered by dates added using push(). This is the key of the push.
    // NULL if push() is yet not used at that point
    String ref_id;
    // The responsible user that created the link
    String owner;
    // The date and time this link was added
    String date_added;
    // what is this referring to
    String type;
    // A Firebase reference URL
    // (This must be a complete URL from the firebase root till the point of reference)
    String reference;

    public Link(String ref_id, String owner, String date_added, String type, String reference) {
        this.ref_id = ref_id;
        this.date_added = date_added;
        this.type = type;
        this.reference = reference;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
