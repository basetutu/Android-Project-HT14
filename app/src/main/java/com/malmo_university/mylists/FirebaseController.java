package com.malmo_university.mylists;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

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
    private static final String VALUES = "VALUES";

    // Standard child names of a user
    private static final String CHECKLISTS_REF = "CHECKLISTS_REF";
    private static final String CONTACTS_REF = "CONTACTS_REF";
    private static final String PROFILE = "PROFILE";
    private static final String AWAITING_ACCEPTANCE_REF = "AWAITING_ACCEPTANCE_REF";

    // Standard child names of a checklist
    private static final String ITEMS = "ITEMS";
    private static final String USERS_REF = "USERS_REF";
    private static final String REF_ID = "REF_ID";

    // Standard value names
    private static final String CREATION_DATE = "CREATION_DATE";
    private static final String NAME = "NAME";
    private static final String USERNAME = "USERNAME";
    private static final String EMAIL = "EMAIL";
    private static final String TLF = "TLF";

    private static String currentUser;
    private static MainActivity mParentActivity;

    // The values of the type attribute in the Link-class
    // It's simply indicating what the Link is linking to
    private static final String LINK_TYPE_CHECKLIST = "CHECKLIST";
    private static final String LINK_TYPE_CONTACT = "CONTACT";
    private static Firebase mFirebaseUSERS;
    private static Firebase mFirebaseCHECKLISTS;
    private static Firebase mFirebaseCURRENTUSER;

    // init /////////////////////////////////////////////////////////////////////////////////

    protected static void init(MainActivity context, String currentUserEmail){
        mFirebase = new Firebase(Globals.FIREBASE_DB_ROOT_URL);
        mFirebaseUSERS = mFirebase.child(DB_USERS);
        mFirebaseCHECKLISTS = mFirebase.child(DB_CHECKLISTS);
        setCurrentUser(currentUserEmail);
        mFirebaseCURRENTUSER = mFirebaseUSERS.child(getCurrentUserKey());
        mParentActivity = context;
    }

    // Help functions

    protected static String makeChecklistPath(String checklist_ref_id){
        return Globals.FIREBASE_DB_ROOT_URL + "/" + DB_CHECKLISTS + "/" + checklist_ref_id;
    }
    protected static String makeUserPath(String userEmail){
        return Globals.FIREBASE_DB_ROOT_URL + "/" + DB_USERS + "/" + Algorithms.transformEmailToKey(userEmail);
    }

    // Project functions ///////////////////////////////////////////////////////////////////

    protected static void createUser(String userEmail, String full_name, String tlf){
        HashMap<String,String> values = new HashMap<String, String>();

        userEmail = userEmail.toLowerCase();

        // Create user account
        values.put(USERNAME, userEmail);
        values.put(CREATION_DATE, getTimestamp());
        mFirebaseUSERS.child(Algorithms.transformEmailToKey(userEmail)).child(VALUES).setValue(values);

        values.clear();

        // Create profile
        values.put(EMAIL, userEmail);
        values.put(NAME, full_name);
        values.put(TLF, tlf);
        mFirebaseUSERS.child(Algorithms.transformEmailToKey(userEmail)).child(PROFILE).setValue(values);
    }

    protected static void updateProfile(Profile profile){
        // todo (need current data)
        // get current data
        // check for null in the profile object
        // replace the nulls in the new profile with the current data retrieved
        // set the new values in database using the next line
        mFirebaseCURRENTUSER.child(PROFILE).setValue(profile);
    }

    /**
     * This function links to a user to create contacts for the current user.
     *
     * @param userEmail
     */
    protected static void addContactToUserList(String userEmail){
        userEmail = userEmail.toLowerCase();
        Link link = new Link(Algorithms.transformEmailToKey(userEmail), getCurrentUser(),
                getTimestamp(), LINK_TYPE_CONTACT,
                makeUserPath(makeUserPath(Algorithms.transformEmailToKey(userEmail))));
        mFirebaseCURRENTUSER.child(CONTACTS_REF).child(Algorithms.transformEmailToKey(userEmail)).setValue(link);
    }

    /**
     * This function removes a contact from a user
     * @param userEmail
     */
    protected static void removeContactFromUserList(String userEmail){
        mFirebaseCURRENTUSER.child(CONTACTS_REF).child(Algorithms.transformEmailToKey(userEmail)).removeValue();
    }

    ///////////////////////////////////////////////////////////////////////////////

    protected static void createChecklist(String checklistName){
        // Create a checklist in firebase
        String checklist_ref_id = mFirebaseCHECKLISTS.push().getKey();
        // Create and initialize checklist
        HashMap<String, String> values = new HashMap<String, String>();
        values.put(NAME, checklistName);
        values.put(CREATION_DATE, getTimestamp());
        values.put(REF_ID, checklist_ref_id);
        mFirebaseCHECKLISTS.child(checklist_ref_id).child(VALUES).setValue(values);

        // add the checklist to the current logged in users references of checklists
        String link_ref_id = mFirebaseCURRENTUSER.child(CHECKLISTS_REF).push().getKey();
        Link link = new Link(link_ref_id, getCurrentUser(), getTimestamp(), LINK_TYPE_CHECKLIST, checklist_ref_id);
        mFirebaseCURRENTUSER.child(CHECKLISTS_REF).child(link_ref_id).setValue(link);

        // Add this user to the checklist's list of users who has a reference to it
        mFirebaseCHECKLISTS.child(checklist_ref_id).child(USERS_REF).child(getCurrentUserKey()).setValue(getCurrentUser());
        mFirebaseCHECKLISTS.child(checklist_ref_id).child(USERS_REF).child("alo").setValue(getCurrentUser());

    }

    protected static void shareChecklist(String toUserEmail, String checklist_ref_id){
        toUserEmail = toUserEmail.toLowerCase();
        String ref_id = mFirebaseUSERS.child(Algorithms.transformEmailToKey(toUserEmail)).child(AWAITING_ACCEPTANCE_REF).push().getKey();
        Link link = new Link(ref_id, getCurrentUser(), getTimestamp(), LINK_TYPE_CHECKLIST,
                makeChecklistPath(checklist_ref_id));
        mFirebaseUSERS.child(Algorithms.transformEmailToKey(toUserEmail)).child(AWAITING_ACCEPTANCE_REF).child(ref_id).setValue(link);
    }

    protected static void acceptSharedChecklist(){
        // check to see if the checklist still exists
        // then crete a link
        // place the link in this users references of checklists
        // add this user to the checklist's list of users
        //mFirebaseCURRENTUSER.child(AWAITING_ACCEPTANCE_REF).child()
    }

    protected static void removeChecklist(String checklist_id){
        //todo (needs a variable to check how many are listening of that checklist)
        // if the current user count of list of users with reference is equal to 1
        // then delete the checklist, otherwise remove this user from list

        // Remove the checklist entirely
//        mFirebaseCHECKLISTS.child(checklist_ref_id).removeValue();
        // delete this user from the checklist's list of users with reference to it
//        mFirebaseCHECKLISTS.child(checklist_ref_id).child(USERS_REF).child(getCurrentUserKey()).removeValue();
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    protected static void addItemToChecklist(String checklist_id, String title, String note){
        String ref_id = mFirebaseCHECKLISTS.child(checklist_id).child(ITEMS).push().getKey();
        Item item = new Item(ref_id, checklist_id, getCurrentUser(), getTimestamp(), 0, title, note, false);
        mFirebaseCHECKLISTS.child(checklist_id).child(ITEMS).child(ref_id).setValue(item);
        //todo test removing
        //mFirebaseCHECKLISTS.child(item.checklist_ref_id).child(item.ref_id).removeValue();
    }

    protected static void editItemOnChecklist(String title, String note, Item item){
        item.setTitle(title);
        item.setNote(note);
        mFirebaseCHECKLISTS.child(item.checklist_ref_id).child(item.ref_id).setValue(item);
    }

    protected static void checkItemOnChecklist(boolean state, Item item){
        item.checked = state;
        mFirebaseCHECKLISTS.child(item.checklist_ref_id).child(item.ref_id).setValue(item);
    }

    // todo not tested
    protected static void removeItemFromChecklist(Item item){
        mFirebaseCHECKLISTS.child(item.checklist_ref_id).child(item.ref_id).removeValue();
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

    protected static String getCurrentUserKey(){
        return Algorithms.transformEmailToKey(currentUser);
    }



    // AUTHENTICATION ///////////////////////////////////////////////////////////////////////

    /**
     * This function will handle user login.
     *
     * @param rootLocation The root location for the database
     * @param user The username (must be an email)
     * @param pass The password
     */
    public static void login(final Activity context, Firebase rootLocation, String user, String pass, final String receiverAction, final int responseOK, final int responseFail) {
        if (Globals.DEBUG_invocation)
            Log.w(TAG,"login");
        // Removes all accidental spaces from the user-string.
        user = Algorithms.removeAllSpacesBeforeAndAfterString(user);
        // Authenticate user
        final String finalUser = user;
        rootLocation.authWithPassword(user, pass, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Toast.makeText(context, context.getResources().getString(R.string.login_success) + " " + finalUser, Toast.LENGTH_SHORT).show();
                MyBroadcastController.sendBroadcast(receiverAction, responseOK);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                int code = firebaseError.getCode();
                String message = firebaseError.getMessage();

                Log.e(TAG, "Authentication errorcode: " + code);

                handleAuthError(context, code, message);

                if (code == FirebaseError.USER_DOES_NOT_EXIST) {
                    Log.e(TAG, "USER_DOES_NOT_EXIST !!!");
                    Toast.makeText(context, "Authentication failed: User does not exist!",
                            Toast.LENGTH_LONG).show();
                } else if (code == FirebaseError.INVALID_PASSWORD) {
                    Log.e(TAG, "INVALID_PASSWORD !!!");
                    Toast.makeText(context, "Authentication failed: Wrong password!",
                            Toast.LENGTH_LONG).show();
                }else if (code == FirebaseError.AUTHENTICATION_PROVIDER_DISABLED){
                    Log.e(TAG, "AUTHENTICATION_PROVIDER_DISABLED !!!");
                    Toast.makeText(context, "Authentication failed: Authentication provider is disabled",
                            Toast.LENGTH_LONG).show();
                }else if (code == FirebaseError.EMAIL_TAKEN){
                    Log.d(TAG, "EMAIL_TAKEN !!!");
                    Toast.makeText(context, "Authentication failed: This email is already taken",
                            Toast.LENGTH_LONG).show();
                }else if (code == FirebaseError.INVALID_EMAIL){
                    Log.d(TAG, "INVALID_EMAIL !!!");
                    Toast.makeText(context, "Authentication failed: This email is invalid. Please check your typing for spaces !",
                            Toast.LENGTH_LONG).show();
                }else{
                    Log.d(TAG, "Unknown Authentication Error !!!");
                    Toast.makeText(context, "Unknown Authentication Error",
                            Toast.LENGTH_LONG).show();
                }
                MyBroadcastController.sendBroadcast(receiverAction, responseFail);
            }
        });
        if (Globals.DEBUG_invocation)
            Log.w(TAG," - login");
    }

    private static void handleAuthError(Activity context, int code, String message){
        if (Globals.DEBUG_invocation)
            Log.w(TAG,"handleAuthError");

        if (code == FirebaseError.USER_DOES_NOT_EXIST) {
            Log.w(TAG, "USER_DOES_NOT_EXIST !!!");
            Toast.makeText(context, "Authentication failed: User does not exist!",
                    Toast.LENGTH_LONG).show();
        } else if (code == FirebaseError.INVALID_PASSWORD) {
            Log.w(TAG, "INVALID_PASSWORD !!!");
            Toast.makeText(context, "Authentication failed: Wrong password!",
                    Toast.LENGTH_LONG).show();
        }else if (code == FirebaseError.AUTHENTICATION_PROVIDER_DISABLED){
            Log.w(TAG, "AUTHENTICATION_PROVIDER_DISABLED !!!");
            Toast.makeText(context, "Authentication failed: Authentication provider is disabled",
                    Toast.LENGTH_LONG).show();
        }else if (code == FirebaseError.EMAIL_TAKEN){
            Log.w(TAG, "EMAIL_TAKEN !!!");
            Toast.makeText(context, "Authentication failed: This email is already taken",
                    Toast.LENGTH_LONG).show();
        }else if (code == FirebaseError.INVALID_EMAIL){
            Log.w(TAG, "INVALID_EMAIL !!!");
            Toast.makeText(context, "Authentication failed: This email is invalid. Please check your typing for spaces !",
                    Toast.LENGTH_LONG).show();
        }else if (code == FirebaseError.NETWORK_ERROR){
            Log.w(TAG, "NETWORK_ERROR !!!");
            Toast.makeText(context, "Authentication failed: Internet connection is lost !",
                    Toast.LENGTH_LONG).show();
        }else{
            Log.e(TAG, "Unknown Authentication Error !!!");
            Toast.makeText(context, "Unknown Authentication Error",
                    Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error message: " + message);
        }
        if (Globals.DEBUG_invocation)
            Log.w(TAG," - handleAuthError");
    }

    protected static void createNewUser(final Activity currentActivity, Firebase location, String user, final String pass){
        if (Globals.DEBUG_invocation)
            Log.w(TAG,"createNewUser");
        // Removes all accidental spaces from the user-string.
        user = Algorithms.removeAllSpaces(user);
        final String finalUser = user;
        location.createUser(user, pass, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                Toast.makeText(currentActivity, R.string.registration_success, Toast.LENGTH_SHORT).show();
                // todo Save credentials in shared library
                SharedPreferencesController.simpleWritePersistentString(Globals.USERNAME, finalUser);
                SharedPreferencesController.simpleWritePersistentString(Globals.PASSWORD, pass);

                Intent startIntent = new Intent(currentActivity, ActivityLoggedIn.class);
                currentActivity.startActivity(startIntent);
                currentActivity.finish();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                int code = firebaseError.getCode();
                String message = firebaseError.getMessage();

                handleAuthError(currentActivity, code, message);
            }
        });
        if (Globals.DEBUG_invocation)
            Log.w(TAG," - createNewUser");
    }

    // LISTENERS ///////////////////////////////////////////////////////////////////////////

    protected class ChildEventListenerChecklists implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    }

    // Listener registrations (for reading data) ////////////////////////////////////////////

    protected static void registerChildListener(Firebase location, ChildEventListener listener) {
        location.addChildEventListener(listener);
    }

    protected static void unregisterChildListener(Firebase location, ChildEventListener listener) {
        location.removeEventListener(listener);
    }

    protected static void registerValueListener(Firebase location, ValueEventListener listener) {
        location.addValueEventListener(listener);
    }

    protected static void unregisterValueListener(Firebase location, ValueEventListener listener) {
        location.removeEventListener(listener);
    }

    /////////////////////////////////////////////////////////////////////////////////////////


    // MOVE TO FragmentItems // todo

    private ChildEventListener mITEMS_Listener = new ChildEventListener() {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    };
    private ChildEventListener mUSERS_REF_Listener = new ChildEventListener() {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    };






}