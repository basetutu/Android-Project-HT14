package com.malmo_university.mylists;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.Firebase;

import java.util.Locale;


public class MainActivity extends Activity implements ActionBar.TabListener {
    private static final String TAG = "MainActivity";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG,"onCreate");

        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);

        //todo login

/*
        // TEST SECTION /////////////////////////////////////////////////////////////////////

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
        /////////////////////////////////////////////////////////////////////////////////////

        // Set up the action bar.
        mActionBar = getActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        mSectionsPagerAdapter.setPageCount(3);
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

    }

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

    public void allo(View v){
        mSectionsPagerAdapter.addPage();
        mSectionsPagerAdapter.notifyDataSetChanged();
        recreateTabs();
        //mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "onResume");

        recreateTabs();

        Log.w(TAG, " - onResume");
    }


    ////////////////////////////////////////////////////////////////////////////////////////

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
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private int pageCount;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return FragmentChecklists_2.newInstance(position + 1);
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
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                default:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            //return null;
        }
    }

}
