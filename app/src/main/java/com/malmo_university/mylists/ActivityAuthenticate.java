package com.malmo_university.mylists;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.firebase.client.Firebase;


public class ActivityAuthenticate extends Activity {
    private static final String TAG = "ActivityAuthenticate";
    private ActionBar actionBar;
    private FragmentManager fm;

    // Important TODO
    // 1. In the fragment, save instance state by override onSaveInstanceState and restore on onActivityCreated
    // 2. And important point, in the activity, you have to save fragment's instance on onSaveInstanceState and restore on onCreate.


    ////////////////////////////////////////////////////////////////////////////////////
    ///////// LIFE-CYCLE ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment;
        setContentView(R.layout.activity_authenticate);

        // instantiate several application wide controllers
        Firebase.setAndroidContext(this);
        SharedPreferencesController.instantiate(this, Globals.SHARED_PREFERENCE_MY_LISTS);
        MyBroadcastController.setAndroidContext(this);

        fm = getFragmentManager();

        // Modify ActionBar
        actionBar = getActionBar();
        actionBar.setTitle(R.string.app_name);
        actionBar.setSubtitle(R.string.app_subtitle);

        // Alternative:
        // actionBar.setIcon(getResources().getDrawable(R.drawable.ic_launcher_2);
        // Keep i mind that there are two overloading functions called setIcon()
        // But only one function for setBackgroundDrawable() !
        actionBar.setIcon(R.drawable.ic_launcher);

        fragment = fm.findFragmentById(R.id.container_loginActivity);
        if (fragment == null) {
            fm.beginTransaction()
                    .replace(R.id.container_loginActivity, FragmentLogin.newInstance(Globals.FIREBASE_DB_ROOT_URL))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ///////// NAVIGATION ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////

    protected void changeFragment(int fragNbr) {
        Fragment fragment;
        switch (fragNbr) {
            case Globals.FRAGMENT_ABOUT:
                fragment = FragmentAbout.newInstance();
                break;
            case Globals.FRAGMENT_REGISTRATION:
                fragment = FragmentRegistration.newInstance(Globals.FIREBASE_DB_ROOT_URL);
                break;
            case Globals.FRAGMENT_LOGIN:
                fragment = FragmentLogin.newInstance(Globals.FIREBASE_DB_ROOT_URL);
                break;
            default:
                fragment = null;
        }
        fm.beginTransaction().replace(R.id.container_loginActivity, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }
}
