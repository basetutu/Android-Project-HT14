package com.malmo_university.mylists.entities;

/**
 * Created by Saeed on 18-01-15.
 */
public class Link {
    // Links will be ordered by dates added using push(). This is the key of the push.
    // NULL if push() is yet not used at that point
    String ref_id;
    // The responsible user that created the link
    String owner;
    // The date and time this link was created
    String creation_date;
    // what is this referring to
    String type;
    // A Firebase reference URL
    // (This must be a complete URL from the firebase root till the point of reference)
    String reference;
    // Name of the entry that is being linked to, for listing purposes
    private String name;

    public Link(String ref_id, String owner, String creation_date, String type, String reference, String name) {
        this.ref_id = ref_id;
        this.creation_date = creation_date;
        this.type = type;
        this.reference = reference;
        this.owner = owner;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
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
