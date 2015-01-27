package com.malmo_university.mylists.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.malmo_university.mylists.Controllers.FirebaseController;
import com.malmo_university.mylists.MainActivity;
import com.malmo_university.mylists.Packaged_functions.AlertDialogs;
import com.malmo_university.mylists.R;
import com.malmo_university.mylists.entities.Link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentChecklists extends Fragment {
    private static final String TAG = "FragmentChecklists";
    private static final String FRAGMENT_REF_ID = "firebaseChecklistRef";
    private static final String CHECKLIST_NAME = "checklistName";

    protected MainActivity mParentActivity;

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
//    private ArrayList<Checklist> mChecklistsArray;
//    private HashMap<String, Checklist> mChecklistsMap;

    private ChecklistsAdapter mListViewAdapter;
    private ListView mListView;

    private boolean childListenerRegistered = false;

    // Data collection for this fragment
    private ArrayList<Link> mUserChecklistArray;
    private HashMap<String, Link> mUserChecklistMap;

    private String mFragmentName;

    ////////////////////////////////////////////////////////////////////////////////////////

    // Not needed (the information is hardcoded since there is only one of its kind)
    public static FragmentChecklists newInstance(String checklistName) {
        if (Globals.DEBUG_invocation)
            Log.w(TAG, "newInstance");

        FragmentChecklists newFragmentChat = new FragmentChecklists();

        Bundle args = new Bundle();
        args.putString(CHECKLIST_NAME, checklistName);
        newFragmentChat.setArguments(args);

        if (Globals.DEBUG_invocation)
            Log.w(TAG, " - newInstance");
        return newFragmentChat;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Globals.DEBUG_invocation)
            Log.w(TAG, "onCreate");

        mParentActivity = (MainActivity)getActivity();

        Bundle args = getArguments();
        mFragmentName = args.getString(CHECKLIST_NAME);


        // fetch the cached data from the activity
//        mChecklistsArray = mParentActivity.getChecklistsArray();
//        mChecklistsMap = mParentActivity.getChecklistsMap();
        mUserChecklistArray = mParentActivity.getUserChecklistsArray();
        mUserChecklistMap = mParentActivity.getUserChecklistsMap();

        mListViewAdapter = new ChecklistsAdapter();
        mListViewAdapter.notifyDataSetChanged();

        if (!childListenerRegistered) {
//            FirebaseController.registerValueListener(mFirebaseChecklists, mGroupValueListener);
//            FirebaseController.registerChildListener(mFirebaseGroupMessages, mGroupMessageListener);
            childListenerRegistered = true;
        }
        if (Globals.DEBUG_invocation)
            Log.w(TAG, " - onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w(TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_listview, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listview);
        mListView.setDivider(null);
        mListView.setDividerHeight(0);

        mListView.setAdapter(mListViewAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w(TAG,"onResume");

        // Register mCHECKLISTS_REF_Listener
        Firebase firebase = new Firebase(FirebaseController.makeUserPath(FirebaseController.getCurrentUser()));
        firebase = firebase.child(FirebaseController.CHECKLISTS_REF);
        FirebaseController.registerChildListener(firebase, mCHECKLISTS_REF_Listener);

        // TEST SECTION

//        Log.w(TAG,"0");
//
        FirebaseController.createUser("smg@gmail.com", "Saeed Ghasemi", "0046763150074");
        FirebaseController.createUser("smg2006@gmail.com", "Tom Andersen", "0763212445");
        FirebaseController.createUser("meem@gmail.com", "Jonathan Bjarnason", "0738145244");
        FirebaseController.createUser("erik@yahoo.com", "Erik Trulsson", "0763212445");
        FirebaseController.createUser("andreas.Goransson@mah.se", "Andreas Goransson", "0763212445");
//        Log.w(TAG,"1");
//
//        FirebaseController.createChecklist("shopping list");
//        FirebaseController.createChecklist("remember these");
//        Log.w(TAG,"2");
//
        FirebaseController.addContactToUserList("smg2006@gmail.com");
        FirebaseController.addContactToUserList("andreas.Goransson@mah.se");
//        Log.w(TAG,"3");
//
        FirebaseController.shareChecklist("andreas.Goransson@mah.se","Shopping list","-Jsdfsdgsds45wef");
//        Log.w(TAG,"4");
//
//        FirebaseController.addItemToChecklist("shopping list", "title", "note");
//        Log.w(TAG,"5");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w(TAG,"onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.w(TAG,"onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG,"onDestroy");
    }

    public CharSequence getName() {
        return mFragmentName;
    }


    ////////////////////////////////////////////////////////////////////////////////////////

    private class ChecklistsAdapter extends BaseAdapter {
        private final String TAG = "ChecklistsAdapter";

        //private final FragmentChecklists mFragmentChatRef;
        private final LayoutInflater inflater;

        /*************  CustomAdapter Constructor *****************/
        public ChecklistsAdapter() {
            /***********  Layout inflater to call external xml layout () ***********/
            inflater = ( LayoutInflater ) mParentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        /******** What is the size of Passed Arraylist Size ************/
        public int getCount() {
            return mUserChecklistArray.size();
        }

        public Object getItem(int position) {
            return mUserChecklistArray.get(position);
        }

        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View vi, ViewGroup parent) {
            if (Globals.DEBUG_results) {
                Log.w(TAG, "getView");
            }
            ViewHolder viewHolder;
            Link tempValues;

            // Our listview uses two views for its rows depending on who the sender is
            if(mUserChecklistArray.size() > 0) {
                tempValues = mUserChecklistArray.get(position);
            }else{
                tempValues = null;
            }

            // If no recycled convertView has been returned, then inflate a new view and place a
            // ViewHolder in it as tag.
            // Otherwise reuse convertView by getting its ViewHolder.
            if(vi == null){
                viewHolder = new ViewHolder();
                vi = inflater.inflate(R.layout.row_checklist, null);
                /****** View Holder Object to contain tabitem.xml file elements ******/
                viewHolder.title = (TextView) vi.findViewById(R.id.row_checklist_title);
                viewHolder.check = (ImageView) vi.findViewById(R.id.row_checklist_check);
                /************  Set viewHolder with LayoutInflater ************/
                vi.setTag( viewHolder );
            } else {
                // Fetch the ViewHolder and reuse the vi
                viewHolder = (ViewHolder) vi.getTag();
            }

            // Now that we have a viewholder, we can place new data inside its views
            if(tempValues == null){
                viewHolder.title.setText("Empty checklist");
                viewHolder.check.setVisibility(View.INVISIBLE);
            } else {
                /************  Set Model values     from Holder elements ***********/
                viewHolder.title.setText(tempValues.getName());
                /******** Set Item Click Listener for LayoutInflater for each row *******/
                vi.setOnClickListener(onItemClickListener);
            }
            return vi;
        }

        // One listener to rule them all
        private View.OnClickListener onItemClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mParentActivity.onChecklistClicked(mListView.getPositionForView(v));
            }
        };

        /********* Create a holder Class to contain inflated xml file elements *********/
        public class ViewHolder{
            public TextView title;
            public ImageView check;
        }
    }

    // Listeners //////////////////////////////////////////////////////////////////////////

    private ChildEventListener mCHECKLISTS_REF_Listener = new ChildEventListener() {
        private final String TAG = "mCHECKLISTS_REF_Listener";
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (Globals.DEBUG_invocation) {
                Log.w(TAG, "onChildAdded");
            }
            // We do this only to be able to divide the string that we receive into variable
            // like "id" and "from".
            Map<String, Link> dataMap = (Map<String, Link>) dataSnapshot.getValue();
            // Extract data
            String creation_date = String.valueOf(dataMap.get("creation_date"));
            String owner = String.valueOf(dataMap.get("owner"));
            String ref_id = String.valueOf(dataMap.get("ref_id"));
            String reference = String.valueOf(dataMap.get("reference"));
            String type = String.valueOf(dataMap.get("type"));
            String checklistName = String.valueOf(dataMap.get("name"));
            // DEBUG
            if (Globals.DEBUG_results) {
                Log.i(TAG, "Child creation_date: " + creation_date);
                Log.i(TAG, "Child owner: " + owner);
                Log.i(TAG, "Child ref_id: " + ref_id);
                Log.i(TAG, "Child reference: " + reference);
                Log.i(TAG, "Child type: " + type);
                Log.i(TAG, "Child name: " + checklistName);
            }

            if (!mUserChecklistMap.containsKey(reference)) {
                Log.w(TAG, "Adding a checklist to UserChecklistsMap and UserChecklistsArray");

                Link link = new Link(ref_id, owner, creation_date, type, reference, checklistName);

                mUserChecklistArray.add(link);
                mUserChecklistMap.put(reference,link);

                mListViewAdapter.notifyDataSetChanged();
            }else{
                Log.w(TAG, "Checklist_REF link already existed");
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            if (Globals.DEBUG_invocation) {
                Log.w(TAG, "onChildChanged");
            }
            // We do this only to be able to divide the string that we receive into variable
            // like "id" and "from".
            Map<String, Link> dataMap = (Map<String, Link>) dataSnapshot.getValue();
            // Extract data
            String creation_date = String.valueOf(dataMap.get("creation_date"));
            String owner = String.valueOf(dataMap.get("owner"));
            String ref_id = String.valueOf(dataMap.get("ref_id"));
            String reference = String.valueOf(dataMap.get("reference"));
            String type = String.valueOf(dataMap.get("type"));
            String checklistName = String.valueOf(dataMap.get("name"));
            // DEBUG
            if (Globals.DEBUG_results) {
                Log.i(TAG, "Child creation_date: " + creation_date);
                Log.i(TAG, "Child owner: " + owner);
                Log.i(TAG, "Child ref_id: " + ref_id);
                Log.i(TAG, "Child reference: " + reference);
                Log.i(TAG, "Child type: " + type);
                Log.i(TAG, "Child name: " + checklistName);
            }

            Link checklistLink = new Link(ref_id, owner, creation_date, type, reference, checklistName);
            // place the old checklist in array
            Link oldChecklist = mUserChecklistArray.set(findWithinListArray(checklistLink), checklistLink);
            // use the old checklist to find it in map and remove it
            mUserChecklistMap.remove(oldChecklist.getReference());
            // Put the updated checklist into the map
            mUserChecklistMap.put(checklistLink.getReference(), checklistLink);

            mListViewAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            if (Globals.DEBUG_invocation) {
                Log.w(TAG, "onChildRemoved");
            }
            // We do this only to be able to divide the string that we receive into variable
            // like "id" and "from".
            Map<String, Link> dataMap = (Map<String, Link>) dataSnapshot.getValue();
            // Extract data
            String creation_date = String.valueOf(dataMap.get("creation_date"));
            String owner = String.valueOf(dataMap.get("owner"));
            String ref_id = String.valueOf(dataMap.get("ref_id"));
            String reference = String.valueOf(dataMap.get("reference"));
            String type = String.valueOf(dataMap.get("type"));
            String checklistName = String.valueOf(dataMap.get("name"));
            // DEBUG
            if (Globals.DEBUG_results) {
                Log.i(TAG, "Child creation_date: " + creation_date);
                Log.i(TAG, "Child owner: " + owner);
                Log.i(TAG, "Child ref_id: " + ref_id);
                Log.i(TAG, "Child reference: " + reference);
                Log.i(TAG, "Child type: " + type);
                Log.i(TAG, "Child name: " + checklistName);
            }

            Link checklistLink = new Link(ref_id, owner, creation_date, type, reference, checklistName);
            // place the old checklist in array
            mUserChecklistArray.remove(findWithinListArray(checklistLink));
            // use the old checklist to find it in map and remove it
            mUserChecklistMap.remove(checklistLink.getReference());

            mListViewAdapter.notifyDataSetChanged();

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            if (Globals.DEBUG_invocation)
                Log.i(TAG, "onChildMoved");
            mParentActivity.makeToast("onChildMoved");
            // not needed
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            if (Globals.DEBUG_invocation)
                Log.e(TAG, "onCancelled - ERROR");
            mParentActivity.makeToast("onCancelled - ERROR");
            // ValueEventListener defines a onCancelled method (lines 12 - 15) that will be
            // called if the read is ever cancelled. A read would be cancelled if the client
            // doesn't have permission to read from a Firebase location. This method will be
            // passed a FirebaseError object indicating why the failure occurred.
            if (Globals.DEBUG_invocation)
                Log.e(TAG, " - ");
        }
    };

    ////////////////////////////////// Menu //////////////////////////////////////////
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Globals.DEBUG_invocation)
            Log.w(TAG, "onActivityCreated");
        setHasOptionsMenu(true);
        setRetainInstance(false);
        if (Globals.DEBUG_invocation)
            Log.w(TAG, " - onActivityCreated");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (Globals.DEBUG_invocation)
            Log.w(TAG, "onCreateOptionsMenu");

        inflater.inflate(R.menu.menu_fragment_checklists, menu);

        if (Globals.DEBUG_invocation)
            Log.i(TAG, " - onCreateOptionsMenu");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (Globals.DEBUG_invocation)
            Log.w(TAG, "onOptionsItemSelected");
        if (Globals.DEBUG_invocation)
            Log.w(TAG, " - onOptionsItemSelected");
        // handle item selection
        switch (item.getItemId()) {
            case R.id.menu_checklists_add_checklist_fragmentChecklist:
                AlertDialogs.makeNewChecklistDialog();
                return true;
            case R.id.menu_item_logout_fragmentChecklist:
                mParentActivity.logoutCleanUp();
                return true;
            case R.id.menu_item_close_fragmentChecklist:
                return true;
            case R.id.menu_item_exit_fragmentChecklist:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //////////////////////////////// Menu end ///////////////////////////////////////////

    // returns -1 if it was not found in the array
    protected int findWithinListArray(Link checklistLink){
        for (int i = 0 ; i < mUserChecklistArray.size() ; i++) {
            if (mUserChecklistArray.get(i).getReference().equals(checklistLink.getReference())){
                return i;
            }
        }
        return -1;
    }

}
