package com.malmo_university.mylists;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends Activity implements ActionBar.TabListener {
    private static final String TAG = "MainActivity";

    // The FragmentAdapter for the ViewPager
    private SectionsPagerAdapter mSectionsPagerAdapter;

    // This is the fragment that shows all the checklists
    private FragmentChecklists mFragmentChecklists;
    // This holds all the checklists
    private ArrayList<Checklist> mListChecklists;

    // This holds the fragment of all the open checklists
    private ArrayList<FragmentItems> mFragmentItems;

    // The ViewPager that will host the section contents.
    private ViewPager mViewPager;
    private ActionBar mActionBar;
    private FragmentManager fm;

    ////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG,"onCreate");

        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        SharedPreferencesController.instantiate(this, Globals.SHARED_PREFERENCE_MY_LISTS);
        MyBroadcastController.setAndroidContext(this);

        FirebaseController.setCurrentUser(SharedPreferencesController.simpleReadPersistentString(Globals.USERNAME));

        // Set up the action bar.
        mActionBar = getActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mActionBar.setIcon(R.drawable.ic_launcher);

        fm = getFragmentManager();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(fm);
        mSectionsPagerAdapter.setPageCount(1);
        mSectionsPagerAdapter.notifyDataSetChanged();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mActionBar.setSelectedNavigationItem(position);
            }
        });

        purgeBackStack();

        if (mFragmentChecklists == null){
            mFragmentChecklists = FragmentChecklists.newInstance();
            mSectionsPagerAdapter.setPageCount(1);
            mSectionsPagerAdapter.notifyDataSetChanged();
        }
        if (mFragmentItems == null) {
            mFragmentItems = new ArrayList<FragmentItems>();
        }
        if (mListChecklists == null) {
            mListChecklists = new ArrayList<Checklist>();
        }


/*
        // TEST SECTION

        Log.w(TAG,"0");

        // get the username and use it on the next line
        FirebaseController.init(Algorithms.transformEmailToKey("smg@gmail.com"));

        Log.w(TAG,"1");

        FirebaseController.createUser("smg@gmail.com");
        FirebaseController.createUser("smg2006@gmail.com");

        Log.w(TAG,"2");

        FirebaseController.createChecklist("shopping list");
        FirebaseController.createChecklist("remember these");

        Log.w(TAG,"3");

        FirebaseController.addContactToUserList("smg2006@gmail.com");

        Log.w(TAG,"4");

        FirebaseController.shareChecklist("smg2006@gmail.com","shopping list");

        Log.w(TAG,"5");

        FirebaseController.addItemToChecklist(FirebaseController.makeUniqueChecklistId("shopping list"), "title", "note");
*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "onResume");

        recreateTabs();

        Log.w(TAG, " - onResume");
    }

    @Override
    protected void onPause() {
        super.onDestroy();
        Log.w(TAG, "onPause");

        Log.w(TAG, " - onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy");

        Log.w(TAG, " - onDestroy");
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    private void recreateTabs(){
        mActionBar.removeAllTabs();
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            mActionBar.addTab(
                    mActionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        makeToast("tab is reselected");
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    // Used to simplify usage of toasts in fragments
    public void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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

    // Get the checklists that where previously received
    public ArrayList<Checklist> getChecklists() {
        return mListChecklists;
    }

    @Override
    public void onBackPressed() {
        if (Globals.DEBUG_invocation)
            Log.w(TAG, "onBackPressed");
        if (!mActionBar.getSelectedTab().getText().toString().equals(Globals.CHECKLISTS_FRAGMENT)){
            mActionBar.setSelectedNavigationItem(0);
        }else{
            finish();
        }
        if (Globals.DEBUG_invocation)
            Log.w(TAG, " - onBackPressed");
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private int pageCount;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void notifyPageAdded(){
            mSectionsPagerAdapter.addPage();
            mSectionsPagerAdapter.notifyDataSetChanged();
            recreateTabs();
            //mViewPager.setAdapter(mSectionsPagerAdapter);
        }

        @Override
        public Fragment getItem(int position) {
            Log.w(TAG, "getItem: " + position);
            if (position != 0) {
                position--;
                //todo
                //see if fragment already exists in mFragmentChecklists
                // return that if it does
                // use
                return FragmentItems.newInstance(mListChecklists.get(position).getName(), mListChecklists.get(position).getRef_id());
            }else{
                return mFragmentChecklists;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return pageCount;
        }
        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }
        public void addPage() {
            this.pageCount++;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            return "new page";
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    protected void purgeBackStack(){
        if (fm.getBackStackEntryCount() > 0){
            Log.w(TAG, "BackStack is being purged...");
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                Log.i(TAG, "BackStack popped");
                fm.popBackStack();
            }
        }
    }

}
