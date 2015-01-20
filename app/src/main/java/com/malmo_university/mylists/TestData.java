package com.malmo_university.mylists;

/**
 * Created by Meep on 2015-01-19.
 */
public class TestData {
    public String title;
    public String description;

    public TestData(String title, String description) {
        super();
        this.title = title;
        this.description = description;
    }

    public String GetTitle(){
        return title;
    }

    public String GetDescription(){
        return description;
    }
}
