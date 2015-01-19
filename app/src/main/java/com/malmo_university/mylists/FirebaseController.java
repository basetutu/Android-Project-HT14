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
    protected static final String DB_CHECKLISTS = "CHECKLISTS";
    private static final String DB_USERS = "USERS";

    // A child with in every child is reserved for containing the root values of every child.
    // This will help listening only on the values possible so that the content of every child
    // within a given child is nor retuned for each value-change.
    // For this reason, the use of ValueEventListener is limited and generally discouraged.
    // This special child is called VALUES as below.
    private static final String VALUES = "VALUES";

    // Standard child names of a user
    private static final String CHECKLIST_REF = "CHECKLIST_REF";
    private static final String CONTACTS_REF = "CONTACTS_REF";
    private static final String PROFILE = "PROFILE";
    private static final String AWAITING_ACCEPTANCE_REF = "AWAITING_ACCEPTANCE_REF";

    // Standard child names of a checklist
    private static final String ITEMS = "ITEMS";
    private static final String USERS_REF = "USERS_REF";
    private static final String CHECKLIST_ID = "CHECKLIST_ID";

    // Standard value names
    private static final String CREATION_DATE = "CREATION_DATE";
    private static final String NAME = "NAME";
    private static final String USERNAME = "USERNAME";
    private static final String EMAIL = "EMAIL";
    private static final String TLF = "TLF";

    private static String currentUser;

    // The values of the type attribute in the Link-class
    // It's simply indicating what the Link is linking to
    private static final String LINK_TYPE_CHECKLIST = "CHECKLIST";
    private static final String LINK_TYPE_CONTACT = "CONTACT";
    private static Firebase mFirebaseUSERS;
    private static Firebase mFirebaseCHECKLISTS;

    // DATA COLLECTIONS /////////////////////////////////////////////////////////////////////

    HashMap<String, Checklist> mChecklists = new HashMap<String, Checklist>();
    HashMap<String, Contact> mContacts = new HashMap<String, Contact>();

    // init /////////////////////////////////////////////////////////////////////////////////

    protected static void init(){
        mFirebase = new Firebase(Globals.FIREBASE_DB_ROOT_URL);
        mFirebaseUSERS = mFirebase.child(DB_USERS);
        mFirebaseCHECKLISTS = mFirebase.child(DB_CHECKLISTS);
    }

    // Help functions

    protected static String makeChecklistPath(String checklistName){
        return Globals.FIREBASE_DB_ROOT_URL + "/" + DB_CHECKLISTS + "/" + checklistName;
    }
    protected static String makeUserPath(String userEmail){
        return Globals.FIREBASE_DB_ROOT_URL + "/" + DB_USERS + "/" + userEmail;
    }

    private static String makeUniqueChecklistId(String checklistName){
        return checklistName.toUpperCase() + " " + getCurrentUser();
    }

    // Project functions ///////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////

    protected static void createUser(String userEmail){
        HashMap<String,String> values = new HashMap<String, String>();

        userEmail = userEmail.toLowerCase();

        values.put(USERNAME, userEmail);
        values.put(CREATION_DATE, getTimestamp());
        mFirebaseUSERS.child(Algorithms.transformEmailToKey(userEmail)).child(VALUES).setValue(values);

        values.clear();

        values.put(EMAIL, userEmail);
        values.put(NAME, "");
        values.put(TLF, "");
        mFirebaseUSERS.child(Algorithms.transformEmailToKey(userEmail)).child(PROFILE).setValue(values);
    }

    protected static void updateProfile(Profile profile){
        // todo
        // get current data
        // check for null in the profile object
        // replace the nulls in the new profile with the current data retrieved
        // set the new values in database using the next line
        mFirebaseUSERS.child(getCurrentUser()).child(PROFILE).setValue(profile);
    }

    /**
     * This function links to a user to create contacts for the current user.
     *
     * @param userEmail
     */
    protected static void addContactToUserList(String userEmail){
        userEmail = userEmail.toLowerCase();
        Firebase mFirebaseUser = mFirebaseUSERS.child(getCurrentUser());
        Link link = new Link(Algorithms.transformEmailToKey(userEmail), getCurrentUser(),
                getTimestamp(), LINK_TYPE_CONTACT,
                makeUserPath(Algorithms.transformEmailToKey(userEmail)));
        mFirebaseUser.child(CONTACTS_REF).child(Algorithms.transformEmailToKey(userEmail)).setValue(link);
    }

    protected static void removeContactFromUserList(){
        //todo
    }

    ///////////////////////////////////////////////////////////////////////////////

    protected static void createChecklist(String checklistName){
        String checklist_id = makeUniqueChecklistId(checklistName);
        // Create and initialize checklist
        HashMap<String, String> values = new HashMap<String, String>();
        values.put(NAME, checklistName);
        values.put(CREATION_DATE, getTimestamp());
        values.put(CHECKLIST_ID, checklist_id);

        mFirebaseCHECKLISTS.child(checklist_id).child(VALUES).setValue(values);

        // add the checklist to the current logged in users references of checklists
        String ref_id = mFirebaseUSERS.child(getCurrentUser()).child(CHECKLIST_REF).push().getKey();
        Link link = new Link(ref_id, getCurrentUser(), getTimestamp(), LINK_TYPE_CHECKLIST,
                makeChecklistPath(checklistName.toUpperCase()));
        mFirebaseUSERS.child(getCurrentUser()).child(CHECKLIST_REF).child(ref_id).setValue(link);

        // Add this user to the checklist's list of users who has a reference to it
        mFirebaseCHECKLISTS.child(checklist_id).child(USERS_REF).child(getCurrentUser()).setValue(getCurrentUser());
    }

    protected static void shareChecklist(String toUserEmail, String checklistName){
        toUserEmail = toUserEmail.toLowerCase();
        String ref_id = mFirebaseUSERS.child(Algorithms.transformEmailToKey(toUserEmail)).child(AWAITING_ACCEPTANCE_REF).push().getKey();
        Link link = new Link(ref_id, getCurrentUser(), getTimestamp(), LINK_TYPE_CHECKLIST,
                makeChecklistPath(checklistName.toUpperCase()));
        mFirebaseUSERS.child(Algorithms.transformEmailToKey(toUserEmail)).child(AWAITING_ACCEPTANCE_REF).child(ref_id).setValue(link);
    }

    protected static void removeChecklist(String checklist_id){
        mFirebaseCHECKLISTS.child(checklist_id).removeValue();
    }

    ////////////////////////////////////////////////////////////////////7

    protected static void addItemToChecklist(String checklistName, String title, String note){
        String ref_id = mFirebaseCHECKLISTS.child(checklistName).push().getKey();
        Item item = new Item(ref_id, checklistName, getCurrentUser(), getTimestamp(), 0, title, note, false);
        mFirebaseCHECKLISTS.child(checklistName).child(ref_id).setValue(item);
    }

    protected static void editItemOnChecklist(String title, String note, Item item){
        item.setTitle(title);
        item.setNote(note);
        mFirebaseCHECKLISTS.child(item.checklistName).child(item.ref_id).setValue(item);
    }

    protected static void checkItemOnChecklist(boolean state, Item item){
        item.state = state;
        mFirebaseCHECKLISTS.child(item.checklistName).child(item.ref_id).setValue(item);
    }

    protected static void removeItemFromChecklist(Item item){
        mFirebaseCHECKLISTS.child(item.checklistName).child(item.ref_id).removeValue();
    }

    ////////////////////////////////////////////////////////////////////7





    // Atomic functions ////////////////////////////////////////////////////////////////////

    protected static String getTimestamp() {
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
     * This function writes data to a given location indicated by location. The values must be
     * stored in a Map object with key-value-pairs. This will overwrite any existing value at the
     * given location.
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