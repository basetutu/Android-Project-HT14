package com.malmo_university.mylists;

/**
 * This class contain all globally important string constants.
 *
 * Created by Saeed on 08-01-15.
 */
public class Globals {
    // Turn on or off debugging
    protected static final boolean DEBUG_invocation = true; // Used mostly for invocations
    protected static final boolean DEBUG_results = true; // Used mostly for results

    // Change the firebase database
    //protected static final String FIREBASE_GROUPS_URL = "https://glaring-fire-7274.firebaseio.com";
    protected static final String FIREBASE_GROUPS_URL = "https://da401a.firebaseio.com/";

    // String identifier for the group-fragment
    protected static final String GROUPS_FRAGMENT = "the_fragment_name_that_lists_all_the_groups";
    // The filename for the SharedPreference
    protected static final String SHARED_PREFERENCE_GROUPWORK = "groupwork";

    // Used in switches to choose a particular fragment
    protected static final int FRAGMENT_LOGIN = 0;
    protected static final int FRAGMENT_ABOUT = 1;
    protected static final int FRAGMENT_REGISTRATION = 2;

    // Message integers used in BroadcastReceivers for communication
    protected static final int MESSAGE_AUTHENTICATED = 3;
    protected static final int MESSAGE_NOT_AUTHENTICATED = 4;

    // String-keys for internal use within the program
    protected static final String USERNAME = "username";
    protected static final String PASSWORD = "password";

}
