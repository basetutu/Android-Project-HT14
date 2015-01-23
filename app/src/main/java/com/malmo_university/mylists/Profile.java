package com.malmo_university.mylists;

import com.malmo_university.mylists.entities.Item;

/**
 * Created by Saeed on 18-01-15.
 */
public class Profile {
    String name;
    String email;
    String tlf;

    public Profile(String name, String email, String tlf){
        this.name = name;
        this.email = email;
        this.tlf = tlf;
        Item item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTlf() {
        return tlf;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }
}
