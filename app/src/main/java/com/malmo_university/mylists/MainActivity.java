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

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class MainActivity extends Activity implements ActionBar.TabListener {
    private static final String TAG = "MainActivity";
    private static final CharSequence CHECKLISTS_FRAGMENT_TAB_NAME = "My Checklists";

    private ActionBar mActionBar;
    private FragmentManager fm;

    // The ViewPager that will host the section contents.
    private ViewPager mViewPager;
    // The FragmentAdapter for the ViewPager
    private SectionsPagerAdapter mSectionsPagerAdapter;

    // This is the fragment that will show the list of checklists
    private FragmentChecklists mFragmentChecklists;

    // This holds the fragment of all the open checklists (they hold their own data)
    private HashMap<String,FragmentItems> mFragmentItems;

    //////////////////////////////////////////////////////////
    // CHECKLISTS RELATED DATA                              //
    // This holds all the checklists as a form of caching   //
    private ArrayList<Checklist> mChecklistsArray;          //
    private HashMap<String, Checklist> mChecklistsMap;      //
    //////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////
    // USER-RELATED DATA                                    //
    // User Checklists                                      //
    ArrayList<Link> mUserChecklistsArray;                   //
    HashMap<String, Link> mUserChecklistsMap;               //
    // User Awaiting acceptance                             //
    ArrayList<Link> mUserAwaitingArray;                     //
    // User Contacts                                        //
    ArrayList<Contact> mUserContactsArray;                  //
    //////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG,"onCreate");

        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        SharedPreferencesController.init(this, Globals.SHARED_PREFERENCE_MY_LISTS);
        MyBroadcastController.init(this);
        AlertDialogs.init(this, getLayoutInflater());
        FirebaseController.init(this, SharedPreferencesController.simpleReadPersistentString(Globals.USERNAME).toLowerCase());

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
            mFragmentItems = new HashMap<String, FragmentItems>();
        }
        if (mChecklistsArray == null) {
            mChecklistsArray = new ArrayList<Checklist>();
            mChecklistsMap = new HashMap<String, Checklist>();
        }

        if (mUserChecklistsArray == null) {
            // USER-RELATED DATA
            // User Checklists
            ArrayList<Link> mUserChecklists = new ArrayList<Link>();
            HashMap<String, Link> mUserChecklistsMap = new HashMap<String, Link>();
            // User Awaiting acceptance
            ArrayList<Link> mUserAwaiting = new ArrayList<Link>();
            // User Contacts
            ArrayList<Contact> mUserContacts = new ArrayList<Contact>();
        }



        // TEST SECTION

        Log.w(TAG,"0");

        Log.w(TAG,"1");

        FirebaseController.createUser("smg@gmail.com", "Saeed Ghasemi", "0046763150074");
        FirebaseController.createUser("smg2006@gmail.com", "Tom Andersen", "0763212445");

        Log.w(TAG,"2");

        FirebaseController.createChecklist("shopping list");
        FirebaseController.createChecklist("remember these");

        Log.w(TAG,"3");

        FirebaseController.addContactToUserList("smg2006@gmail.com");

        Log.w(TAG,"4");

        FirebaseController.shareChecklist("smg2006@gmail.com","shopping list");

        Log.w(TAG,"5");

        FirebaseController.addItemToChecklist("shopping list", "title", "note");
    }

    protected void notifyAdapter(){
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    ///////////////////////////////////////////////////////////////////////////

    protected ArrayList<Link> getUserChecklistsArray(){
        return mUserChecklistsArray;
    }
    protected HashMap<String, Link> getUserChecklistsMap(){
        return mUserChecklistsMap;
    }
    protected ArrayList<Link> getUserAwaitingArray(){
        return mUserAwaitingArray;
    }
    protected ArrayList<Contact> getUserContactsArray(){
        return mUserContactsArray;
    }
    ///////////////////////////////////////////////////////////////////////////

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "onResume");
        recreateTabs();

        // TEST
        Checklist checklist = new Checklist("ref id", FirebaseController.getTimestamp(), "test", null);
        mChecklistsArray.add(mChecklistsArray.size(), checklist);

        onChecklistClicked(0);

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
    public ArrayList<Checklist> getChecklistsArray() {
        return mChecklistsArray;
    }

    // Get the checklists that where previously received
    public HashMap<String, Checklist> getChecklistsMap() {
        return mChecklistsMap;
    }


    @Override
    public void onBackPressed() {
        if (Globals.DEBUG_invocation)
            Log.w(TAG, "onBackPressed");
        if (!mActionBar.getSelectedTab().getText().equals(CHECKLISTS_FRAGMENT_TAB_NAME)){
            mActionBar.setSelectedNavigationItem(0);
        }else{
            purgeBackStack();
            finish();
        }
        if (Globals.DEBUG_invocation)
            Log.w(TAG, " - onBackPressed");
    }

    protected void onChecklistClicked(int mPosition) {
        Log.w(TAG,"onChecklistClicked");
        // get the checklist from mChecklistsArray
        Checklist checklist = mChecklistsArray.get(mPosition);
        // see if there is a corresponding FragmentItems for this in mFragmentItems
        if (mFragmentItems.get(checklist.getRef_id()) != null){
            Log.w(TAG,"fragment exists - not null");
            // if it is there must be a page for it too, select the page
            mViewPager.setCurrentItem(mPosition + 1);
        }else{
            Log.w(TAG,"fragment did not exist - null");
            // if NOT, initialize a new FragmentItems with checklist
            FragmentItems fragmentItems = FragmentItems.newInstance(checklist.getName(),checklist.getRef_id());
            // add it to mFragmentItems
            mFragmentItems.put(checklist.getRef_id(), fragmentItems);
            // Inform ViewPager that there is a new page added by invoking mSectionPageAdapter.fragmentAdded()
            mSectionsPagerAdapter.fragmentAdded();
            // select the new tab/page
            mViewPager.setCurrentItem(mPosition + 1);
        }
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

        public void fragmentAdded(){
            addPage();
            notifyDataSetChanged();
            recreateTabs();
            //mViewPager.setAdapter(mSectionsPagerAdapter);
        }

        @Override
        public Fragment getItem(int position) {
            Log.w(TAG, "getItem: " + position);
            if (position != 0) {
                position--;
                Checklist checklist = mChecklistsArray.get(position);
                String checklist_ref_id = checklist.getRef_id();
                //see if fragment already exists in mFragmentItems
                FragmentItems fragmentItems = mFragmentItems.get(checklist_ref_id);
                if(fragmentItems == null){
                    String checklistName = checklist.getName();
                    fragmentItems = FragmentItems.newInstance(checklistName, checklist_ref_id);
                    mFragmentItems.put(checklist_ref_id, fragmentItems);
                }
                return fragmentItems;
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
            Log.w(TAG, "getPageTitle: " + position);
            if (position != 0) {
                position--;
                return mChecklistsArray.get(position).getName();
            }else{
                return CHECKLISTS_FRAGMENT_TAB_NAME;
            }
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

    // LISTENERS ////////////////////////////////////////////////////////////////////////////

    private ChildEventListener mCHECKLISTS_REF_Listener = new ChildEventListener() {
        private final String TAG = "mUserChecklistsListener";
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (Globals.DEBUG_invocation) {
                Log.d(TAG, "onChildAdded");
            }
            // We do this only to be able to divide the string that we receive into variable
            // like "id" and "from".
            Map<String, Link> dataMap = (Map<String, Link>) dataSnapshot.getValue();
            // Extract data
            String date_added = String.valueOf(dataMap.get("creation_date"));
            String owner = String.valueOf(dataMap.get("owner"));
            String ref_id = String.valueOf(dataMap.get("ref_id"));
            String reference = String.valueOf(dataMap.get("reference"));
            String type = String.valueOf(dataMap.get("type"));
            // DEBUG
            if (Globals.DEBUG_results) {
                Log.d(TAG, "Child creation_date: " + date_added);
                Log.d(TAG, "Child owner: " + owner);
                Log.d(TAG, "Child ref_id: " + ref_id);
                Log.d(TAG, "Child reference: " + reference);
                Log.d(TAG, "Child type: " + type);
            }

            // Create new ChatMessage-object
            Link link = new Link(ref_id, owner, date_added, type, reference);

            if (!getUserChecklistsMap().containsKey(reference)) {
                getUserChecklistsMap().put(reference, link);
                //mListViewMessages.add(newChatMessage.getMessage());
                getUserChecklistsArray().add(link);
                notifyAdapter();
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            if (Globals.DEBUG_invocation)
                Log.d(TAG, "onChildChanged");
            makeToast("onChildChanged");
            //not needed
            // DEBUG_invocation
            if (Globals.DEBUG_invocation) {
                Log.d(TAG, String.valueOf(dataSnapshot.getValue()));
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            if (Globals.DEBUG_invocation)
                Log.d(TAG, "onChildRemoved");
            makeToast("onChildRemoved");
            //not needed
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            if (Globals.DEBUG_invocation)
                Log.d(TAG, "onChildMoved");
            makeToast("onChildMoved");
            // not needed
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            if (Globals.DEBUG_invocation)
                Log.e(TAG, "onCancelled");
            makeToast("onCancelled - ERROR");
            // ValueEventListener defines a onCancelled method (lines 12 - 15) that will be
            // called if the read is ever cancelled. A read would be cancelled if the client
            // doesn't have permission to read from a Firebase location. This method will be
            // passed a FirebaseError object indicating why the failure occurred.
            if (Globals.DEBUG_invocation)
                Log.e(TAG, " - ");
        }
    };

    private ChildEventListener mAWAITING_ACCEPTANCE_REF_Listener = new ChildEventListener() {

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

    private ChildEventListener mCONTACTS_REF_Listener = new ChildEventListener() {
        //todo

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

    private ChildEventListener mVALUES_USER_Listener = new ChildEventListener() {
        //todo

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

    /////////////////////////////////////////////////////////////////////////////////////////
}
