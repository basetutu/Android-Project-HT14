package com.malmo_university.mylists;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Saeed on 18-01-15.
 */
public class User {
    // Firebase URL for the user root
    String userURL;

    // The user profile
    Profile profile;

    // contains data of the VALUES-child (username and date_added)
    HashMap<String,String> values = new HashMap<String, String>();

    // Various different Links
    ArrayList<Link> mAwaitingAcceptance = new ArrayList<Link>();
    ArrayList<Link> mChecklistRef = new ArrayList<Link>();
    ArrayList<Link> mContactsRef = new ArrayList<Link>();

}
