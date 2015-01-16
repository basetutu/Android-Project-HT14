package com.malmo_university.mylists;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Created by Saeed on 26-12-14.
 * <p/>
 * This class is responsible for all operations with the Firebase API. It holds all functions
 * necessary for operation of the chat messages and the groups.
 * <p/>
 * One object of this class can only be responsible for the operation of a single fragment.
 * <p/>
 * Listeners are not implemented here but must be implemented externally and since passed into this
 * the controller for either children or values of a given address.
 */
public class FirebaseController {
    private static final String TAG = "FirebaseController";

    // Authentication ///////////////////////////////////////////////////////////////////////


    // Button logic (buttons from various fragments under the control of this activity)

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

    public static boolean isAuthenticated(Firebase rootLocation) {
        return (rootLocation.getAuth() != null);
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

    protected static boolean logout(Firebase location, Activity activity) {
        if (location != null) {
            location.unauth();
            if (Globals.DEBUG_results) {
                Log.i(TAG, "Logout");
            }
            if (location.getAuth() == null) {
                Toast.makeText(activity, "You have been logged out", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
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
        String groupId = parentChild.push().getKey();
        return groupId;
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

    // Child/Subfolder or Value/data functions (write) ////////////////////////////////////
    // HIGH-LEVEL OPERATIONS FOR THE GROUP MANIPULATION ///////////////////////////////////

    /**
     * Creates a group at the specified location.
     *
     * @param mRootScopeFirebase The location the group must be created
     * @param groupName The groupName of the group
     * @return Firebase group reference for the newly created group
     */
    protected static Firebase createNewGroup(Firebase mRootScopeFirebase, String groupName) {
        //Create new group and returns the childID of the new group
        String groupId = createNewChild(mRootScopeFirebase);

        //mRootScopeFirebase.child(groupId).child("messages");// Creates a child/subfolder for messages to be used later

        // Initializes the new group with its id and name
        mRootScopeFirebase.child(groupId).setValue(new Group(groupId, groupName));
        return mRootScopeFirebase.child(groupId);
    }

    /**
     * Deletes a group at the specified location.
     *
     * @param mRootScopeFirebase The location the group must be created
     * @param groupName The name of the group to be deleted
     */
    protected static void deleteGroup(Firebase mRootScopeFirebase, String groupName) {
        // todo test
        deleteChild(mRootScopeFirebase.child(groupName));
    }

    /**
     * Renames a group at the specified location.
     *
     * @param mRootScopeFirebase The location the group must be created
     * @param newGroupName The new group name of the group
     * @return returns true if successful
     */
    protected static boolean renameGroup(Firebase mRootScopeFirebase, String newGroupName) {
        //todo test
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("name", newGroupName);
        return writeValues(mRootScopeFirebase, data);
    }

    // HIGH-LEVEL OPERATIONS FOR THE "FragmentChat" //////////////////////////////////////

    /**
     * Creates a new message fom the input variables and reserves a space in firebase for it to
     * be stored with a unique id.
     *
     * @param from    Alias of the username who is sending the message
     * @param message The message to be send
     * @return The ChatMessage object created from the input and a unique ID from firebase. Returns null if no group is set.
     */
    protected static ChatMessage sendNewMessage(Firebase messageLocation, String from, String message) {
        // Create new message
        if (messageLocation != null) {
            String msgId = messageLocation.push().getKey();
            ChatMessage msg = new ChatMessage(msgId, from, message, getTimestamp());
            messageLocation.child(msgId).setValue(msg);
            return msg;
        }
        return null;
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

    // OTHER FUNCTIONS //////////////////////////////////////////////////////////////////////


    // OTHER FUNCTIONS //////////////////////////////////////////////////////////////////////

}
