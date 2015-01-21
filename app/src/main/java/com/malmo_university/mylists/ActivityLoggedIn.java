package com.malmo_university.mylists;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.HashMap;


public class ActivityLoggedIn extends Activity {
    private static final String TAG = "ActivityLoggedIn";
    protected static final String RECEIVER_ACTION = "com.malmo_university.mylists.ActivityLoggedIn";
    // INSTANCES THAT ARE FREQUENTLY USED
    private FragmentManager fm;
    private ActionBar actionBar;

    // Stores the different openTabs i order
    private HashMap<String, ActionBar.Tab> openTabs;
    private HashMap<String, Fragment> fragments;//todo Consider using fm.putFragment();

    private String currentFragment = Globals.CHECKLISTS_FRAGMENT;
    private Bundle savedInstanceStateStored;

    private String userName;

    ////////////////////////////////////////////////////////////////////////////////////
    ///////// LIFE-CYCLE ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Globals.DEBUG_invocation)
            Log.w(TAG, "onCreate");
        setContentView(R.layout.delete_activity_logged_in);

        Firebase.setAndroidContext(this);
        SharedPreferencesController.instantiate(this, Globals.SHARED_PREFERENCE_MY_LISTS);
        MyBroadcastController.setAndroidContext(this);

        userName = SharedPreferencesController.simpleReadPersistentString(Globals.USERNAME);

        openTabs = new HashMap<String, ActionBar.Tab>(10);
        fragments = new HashMap<String, Fragment>(10);
        actionBar = getActionBar();
        fm = getFragmentManager();
        purgeBackStack();

        actionBar.setIcon(R.drawable.ic_launcher);

        // to be used at a later time
        savedInstanceStateStored = savedInstanceState;

        if (savedInstanceState != null) {
        }
        //actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        if (Globals.DEBUG_invocation)
            Log.w(TAG, " - onCreate");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) { // work in progress todo
        super.onSaveInstanceState(outState);
        if (Globals.DEBUG_invocation)
            Log.w(TAG, "onSaveInstanceState");

        if (Globals.DEBUG_invocation)
            Log.w(TAG, " - onSaveInstanceState");
    }

    protected void purgeBackStack(){
        if (fm.getBackStackEntryCount() > 0){
            Log.w(TAG, "BackStack is being purged...");
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                Log.i(TAG, "BackStack popped");
                fm.popBackStack();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (Globals.DEBUG_invocation)
            Log.w(TAG, "onBackPressed");
        if (!currentFragment.equals(Globals.CHECKLISTS_FRAGMENT)){
            currentFragment = Globals.CHECKLISTS_FRAGMENT;
            fm.beginTransaction()
                    .replace(R.id.container_chatActivity, fragments.get(Globals.CHECKLISTS_FRAGMENT))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }else{
            finish();
        }
        if (Globals.DEBUG_invocation)
            Log.w(TAG, " - onBackPressed");
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ///////// NAVIGATION ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////

    // Help functions
    protected void changeFragment(String fragmentKey, boolean addToBackStack) {
        currentFragment = fragmentKey;
        FragmentTransaction ft = fm.beginTransaction()
                .replace(R.id.container_chatActivity, fragments.get(currentFragment))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (addToBackStack) {
            ft.addToBackStack(currentFragment);
        }
        ft.commit();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////////// FUNCTIONAL BUTTONS //////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////

    // Used to simplify usage of toasts in fragments
    public void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    public void groupClickedInListview(String group) {
        // Check to see if the tab is already open in the actionbar openTabs
        if (openTabs.containsKey(group)){
            actionBar.selectTab(openTabs.get(group));
        }else {
            if (Globals.DEBUG_results)
                Log.d(TAG, "Tab is not already open");
            // Create a new tab and a new fragment to go with it
        }
    }

    protected HashMap<String, ActionBar.Tab> getOpenTabs(){
        return openTabs;
    }

    protected SharedPreferences getPersistentStorage(){
        final int MODE_PRIVATE = 0;
        SharedPreferences prefs = getApplication().getSharedPreferences(Globals.SHARED_PREFERENCE_MY_LISTS, MODE_PRIVATE);
        return prefs;
    }

    public void onChatItemClick(int mPosition) {
        if (Globals.DEBUG_invocation){
            Log.w(TAG,"onChatItemClick");
        }
        // todo give the choice to remove or copy the message
        if (Globals.DEBUG_invocation){
            Log.w(TAG," - onChatItemClick");
        }
    }

    public String getUserName() {
        return userName;
    }

    protected void logoutCleanUp(){
        // remove credentials from sharedPreferences
        Log.e(TAG, "Username: " + SharedPreferencesController.simpleDeletePersistentString(Globals.USERNAME));
        Log.e(TAG, "Password: " + SharedPreferencesController.simpleDeletePersistentString(Globals.PASSWORD));
        // Start ActivityAuthenticate and close ActivityLoggedIn
        Intent startIntent = new Intent(this, ActivityAuthenticate.class);
        startActivity(startIntent);
        finish();
    }
}
