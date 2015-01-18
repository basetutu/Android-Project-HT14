package com.malmo_university.mylists;

import com.firebase.client.Firebase;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Created by Saeed on 26-12-14.
 *
 * This class is responsible for all operations with the Firebase API. It holds all functions
 * necessary for operation of the chat messages and the groups.
 *
 * Listeners are not implemented here but must be implemented externally and since passed into this
 * the controller for either children or values of a given address.
 */
public class FirebaseController {
    private static final String TAG = "FirebaseController";

    // The point of operation of this class
    // (might be removed from here since this is a static class)
    private static Firebase mFirebase;

    // This controller works within two databases
    private static final String DB_CHECKLISTS = "CHECKLISTS";
    private static final String DB_USERS = "USERS";

    // A child with in every child is reserved for containing the root values of every child.
    // This will help listening only on the values possible so that the content of every child
    // within a given child is nor retuned for each value-change.
    // For this reason, the use of ValueEventListener is limited and generally discouraged.
    // This special child is called VALUES as below.
    private static String VALUES = "VALUES";

    // Standard child names of a user
    private static final String CHECKLIST_REF = "CHECKLIST_REF";
    private static final String CONTACTS_REF = "CONTACTS_REF";
    private static final String PROFILE = "PROFILE";
    private static final String AWAITING_ACCEPTANCE_REF = "AWAITING_ACCEPTANCE_REF";

    // Standard child names of a checklist
    private static final String ITEMS = "ITEMS";
    private static final String USERS_REF = "USERS_REF";

    // Standard value names
    private static final String CREATION_DATE = "CREATION_DATE";
    private static final String NAME = "NAME";
    private static final String USERNAME = "USERNAME";
    private static final String EMAIL = "EMAIL";
    private static final String TLF = "TLF";

    private static String currentUser;

    // The values of the type attribute in the Link-class
    // It's simply indicating what the Link is linking to
    private static final String TYPE_CHECKLIST = "CHECKLIST";
    private static final String TYPE_CONTACT = "CONTACT";

    // init /////////////////////////////////////////////////////////////////////////////////

    protected static void init(){
        mFirebase = new Firebase(Globals.FIREBASE_DB_ROOT_URL);
    }

    // Project functions ///////////////////////////////////////////////////////////////////

    protected static void createChecklist(String checklistName){
        mFirebase.child(DB_CHECKLISTS).child(checklistName).child(VALUES).setValue(NAME,checklistName);
        mFirebase.child(DB_CHECKLISTS).child(checklistName).child(VALUES).setValue(CREATION_DATE,getTimestamp());
        Link link = new Link(null, getCurrentUser(), getTimestamp(), TYPE_CHECKLIST, checklistName);
        addChecklistToUserList(link);
    }

    protected static void addChecklistToUserList(Link link){
        String ref_id = mFirebase.child(DB_USERS).child(getCurrentUser()).child(CHECKLIST_REF).push().getKey();
        link.setRef_id(ref_id);
        mFirebase.child(DB_USERS).child(getCurrentUser()).child(CHECKLIST_REF).child(ref_id).setValue(link);
    }

    protected static void createUser(String userEmail){
        mFirebase.child(DB_USERS).child(userEmail).child(VALUES).setValue(USERNAME,userEmail);
        mFirebase.child(DB_USERS).child(userEmail).child(VALUES).setValue(CREATION_DATE,getTimestamp());
        mFirebase.child(DB_USERS).child(userEmail).child(PROFILE).setValue(EMAIL,userEmail);
        mFirebase.child(DB_USERS).child(userEmail).child(PROFILE).setValue(NAME,"");
        mFirebase.child(DB_USERS).child(userEmail).child(PROFILE).setValue(TLF,"");
    }

    protected static void updateProfile(Profile profile){
        // todo
        // get current data
        // check for null in the profile object
        // replace the nulls in the new profile with the current data retrieved
        // set the new values in database using the next line
        mFirebase.child(DB_USERS).child(getCurrentUser()).child(PROFILE).setValue(profile);
    }

    protected void retrieveData(){
        // todo
        // retrieve data a single time
    }

    protected void shareChecklist(String receiverUserEmail, String checklistURL){
        String ref_id = mFirebase.child(DB_USERS).child(receiverUserEmail).child(AWAITING_ACCEPTANCE_REF).push().getKey();
        Link link = new Link(ref_id, getCurrentUser(), getTimestamp(), TYPE_CHECKLIST, checklistURL);
        mFirebase.child(DB_USERS).child(receiverUserEmail).child(AWAITING_ACCEPTANCE_REF).setValue(link);
    }

    protected void setContact(String userEmail){
        Firebase mFirebaseUsers = mFirebase.child(DB_USERS).child(getCurrentUser());
        String ref_id = mFirebaseUsers.child(CONTACTS_REF).push().getKey();
        Link link = new Link(ref_id, getCurrentUser(), getTimestamp(), TYPE_CONTACT, userEmail);
        mFirebaseUsers.child(CONTACTS_REF).child(ref_id).setValue(link);
    }


    // Atomic functions ////////////////////////////////////////////////////////////////////

    private static String getTimestamp() {
        long time = System.currentTimeMillis();
        Timestamp tsTemp = new Timestamp(time);
        return tsTemp.toString();
    }

    /**
     * This functions create a child at the specified location indicated by parentChild and returns
     * the Firebase instance for the newly created child.
     *
     * @param parentChild The location the child must be created in
     * @return Firebase instance for the newly created child
     */
    protected static String createNewChild(Firebase parentChild) {
        String childId = parentChild.push().getKey();
        return childId;
    }

    /**
     * This function writes data to a given location indicated by location. The values must be stored
     * in a Map object with key-value-pairs.
     *
     * @param location  The location at which the values must be added
     * @param dataSet A Map-object containing the key-value-paired data to be stored at the location
     * @return Returns true if there was any data to write
     */
    protected static boolean writeValues(Firebase location, HashMap<String, String> dataSet) {
        if (dataSet.size() == 0) {
            return false;
        }
        location.setValue(dataSet);
        return true;
    }

    /**
     * This function will delete all data at a given location. This includes all of the values and
     * children within that location.
     *
     * @param URL the URL address to the location to be deleted
     */
    protected static void deleteChild(String URL) {
        new Firebase(URL).removeValue();
    }

    /**
     * This function will delete all data at a given location. This includes all of the values and
     * children within that location.
     *
     * @param childRef A firebase instance for the location to be deleted
     */
    protected static void deleteChild(Firebase childRef) {
        childRef.removeValue();
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    protected static void setCurrentUser(String userEmail){
        currentUser = userEmail;
    }

    protected static String getCurrentUser(){
        return currentUser;
    }



}