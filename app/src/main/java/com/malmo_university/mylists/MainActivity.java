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
import com.malmo_university.mylists.Controllers.FirebaseController;
import com.malmo_university.mylists.Controllers.MyBroadcastController;
import com.malmo_university.mylists.Controllers.SharedPreferencesController;
import com.malmo_university.mylists.Fragments.FragmentChecklists;
import com.malmo_university.mylists.Fragments.FragmentItems;
import com.malmo_university.mylists.Fragments.Globals;
import com.malmo_university.mylists.Packaged_functions.AlertDialogs;
import com.malmo_university.mylists.entities.Contact;
import com.malmo_university.mylists.entities.Item;
import com.malmo_university.mylists.entities.Link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class MainActivity extends Activity implements ActionBar.TabListener {
    private static final String TAG = "MainActivity";
    private static final String CHECKLISTS_FRAGMENT_TAB_NAME = "My Checklists";

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
        mActionBar.setIcon(R.drawable.android);

        // Prepare SectionPageAdapter
        fm = getFragmentManager();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(fm);
        mSectionsPagerAdapter.notifyDataSetChanged();

        // Prepare data collections for use
        if (mUserChecklistsArray == null) {
            mUserChecklistsArray = new ArrayList<Link>();
            mUserChecklistsMap = new HashMap<String, Link>();
            mUserAwaitingArray = new ArrayList<Link>();
            mUserContactsArray = new ArrayList<Contact>();
        }
        if (mFragmentChecklists == null){
            mFragmentChecklists = FragmentChecklists.newInstance(CHECKLISTS_FRAGMENT_TAB_NAME);
            mSectionsPagerAdapter.notifyDataSetChanged();
        }
        if (mFragmentItems == null) {
            mFragmentItems = new HashMap<String, FragmentItems>();
            mSectionsPagerAdapter.notifyDataSetChanged();
        }

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (mActionBar.getSelectedNavigationIndex() != position) {
                    mActionBar.setSelectedNavigationItem(position);
                }
            }
        });

        purgeBackStack();
    }

    ///////////////////////////////////////////////////////////////////////////
    public ArrayList<Link> getUserChecklistsArray(){
        return mUserChecklistsArray;
    }
    public HashMap<String, Link> getUserChecklistsMap(){
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

        Log.w(TAG, " - onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
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
        mActionBar.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.lists));
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
        return super.onOptionsItemSelected(item);
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        Log.w(TAG, "onTabSelected");
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        Log.w(TAG, "onTabReselected");
        AlertDialogs.makeCloseChecklistDialog(tab.getPosition() -1);
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    // Used to simplify usage of toasts in fragments
    public void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void logoutCleanUp(){
        // remove credentials from sharedPreferences
        Log.e(TAG, "Username: " + SharedPreferencesController.simpleDeletePersistentString(Globals.USERNAME));
        Log.e(TAG, "Password: " + SharedPreferencesController.simpleDeletePersistentString(Globals.PASSWORD));
        new Firebase(Globals.FIREBASE_DB_ROOT_URL).unauth();
        // Start ActivityAuthenticate and close MailActivity
        Intent startIntent = new Intent(this, ActivityAuthenticate.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (Globals.DEBUG_invocation)
            Log.w(TAG, "onBackPressed");

        if (!(mActionBar.getSelectedTab().getPosition() == 0)){
            mActionBar.setSelectedNavigationItem(0);
        }else{
            purgeBackStack();
            finish();
        }
        if (Globals.DEBUG_invocation)
            Log.w(TAG, " - onBackPressed");
    }

    public void onChecklistClicked(int mPosition) {
        Log.w(TAG,"onChecklistClicked");

        Link checklistLink = mUserChecklistsArray.get(mPosition);
        Log.e(TAG, "ref_id" + checklistLink.getRef_id());
        Log.e(TAG, "Reference" + checklistLink.getReference());
        // see if there is a corresponding FragmentItems for this in mFragmentItems
        if (mFragmentItems.get(checklistLink.getReference()) != null){
            Log.w(TAG,"fragment exists - not null");
            // if it is there must be a page for it too, select the page
            mViewPager.setCurrentItem(mPosition + 1);
        }else{
            Log.w(TAG,"fragment did not exist - null");
            // if NOT, initialize a new FragmentItems with checklist
            FragmentItems fragmentItems = FragmentItems.newInstance(checklistLink.getName(), checklistLink.getReference());
            // add it to mFragmentItems
            mFragmentItems.put(checklistLink.getReference(), fragmentItems);
            // Inform ViewPager that there is a new page added by invoking mSectionPageAdapter.notifyFragmentAdded()
            mSectionsPagerAdapter.notifyFragmentSetChanged();
            // select the new tab/page
            mViewPager.setCurrentItem(mPosition + 1);
        }
    }

    // Functions invoked by dialog //////////////////////////////////////////////////////////

    public void closeChecklist(int index) {
        // Fetch the link
        Link link = mUserChecklistsArray.get(index);

        // Remove the fragment from the SectionPageAdapter's array
        mFragmentItems.remove(link.getReference());

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mSectionsPagerAdapter.notifyFragmentSetChanged();
        Log.w(TAG, "fragments remaining in array: " + mFragmentItems.size());
    }

    public void newChecklist(String newChecklistName) {
        FirebaseController.createChecklist(newChecklistName);
    }

    public void newItem(String checklist_ref_id, String title, String note) {
        FirebaseController.addItemToChecklist(checklist_ref_id, title, note);
    }

    public void deleteChecklist(String checklist_ref_id) {
        //todo - need listeners for getting USERS_REF
        // see how many users have access to this checklist
        // if it's only this user, then remove the link from this user
        // then delete the child of this checklist from CHECKLISTS
        //FirebaseController.
    }

    public void editItem(Item item, String title, String note) {
        // update the item object
        item.setTitle(title);
        item.setNote(note);
        FirebaseController.editItemOnChecklist(item);
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final String TAG = "SectionPagerAdapter";

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void notifyFragmentSetChanged(){
//            addPage();
            notifyDataSetChanged();
            recreateTabs();
        }

        @Override
        public Fragment getItem(int position) {
            Log.w(TAG, "getItem: " + position);
            if (position != 0) {
                position--;
                //todo
//                Checklist checklist = mChecklistsArray.get(position);
//                String checklist_ref_id = checklist.getItem_ref_id();

                Link link = mUserChecklistsArray.get(position);
                String checklist_ref_id = link.getReference();

                //see if fragment already exists in mFragmentItems
                FragmentItems fragmentItems = mFragmentItems.get(checklist_ref_id);
                if(fragmentItems == null){
                    Log.w(TAG, "Fragment not exist in array == null");
//                    String checklistName = link.getName();
//                    fragmentItems = FragmentItems.newInstance(checklistName, checklist_ref_id);
//                    mFragmentItems.put(checklist_ref_id, fragmentItems);
                }
                return fragmentItems;
            }else{
                return mFragmentChecklists;
            }
        }

        @Override
        public int getCount() {
            Log.w(TAG, "getCount");
            return mFragmentItems.size() +1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.w(TAG, "getPageTitle: " + position);
            if (position != 0) {
                position--;
                return mUserChecklistsArray.get(position).getName();
            }else{
                return mFragmentChecklists.getName();
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
            String checklistName = String.valueOf(dataMap.get("name"));

            // DEBUG
            if (Globals.DEBUG_results) {
                Log.d(TAG, "Child creation_date: " + date_added);
                Log.d(TAG, "Child owner: " + owner);
                Log.d(TAG, "Child ref_id: " + ref_id);
                Log.d(TAG, "Child reference: " + reference);
                Log.d(TAG, "Child type: " + type);
            }

            Link link = new Link(ref_id, owner, date_added, type, reference, checklistName);

            if (!getUserChecklistsMap().containsKey(reference)) {
                getUserChecklistsMap().put(reference, link);
                getUserChecklistsArray().add(link);
                mSectionsPagerAdapter.notifyDataSetChanged();
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
