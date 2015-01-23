package com.malmo_university.mylists.entities;

/**
 * Created by Saeed on 18-01-15.
 */
public class Contact {
    String email;
    String name;
    String tlf;

    public Contact(String email, String name, String tlf) {
        this.email = email;
        this.name = name;
        this.tlf = tlf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTlf() {
        return tlf;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }
}
