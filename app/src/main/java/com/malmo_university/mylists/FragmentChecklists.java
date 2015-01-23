package com.malmo_university.mylists;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentChecklists extends Fragment {
    private static final String TAG = "FragmentChecklists";
    protected MainActivity mParentActivity;

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    private ArrayList<Checklist> mChecklistsArray;
    private HashMap<String, Checklist> mChecklistsMap;
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private ChecklistsAdapter mListViewAdapter;

    private ListView mListView;

    private boolean childListenerRegistered = false;

    // As default the list must scroll down lot the lowest item on the list
    private boolean mLastItemVisible = true;

    ////////////////////////////////////////////////////////////////////////////////////////

    // Not needed (the information is hardcoded since there is only one of its kind)
    public static FragmentChecklists newInstance() {
        if (Globals.DEBUG_invocation)
            Log.w(TAG, "newInstance");

        FragmentChecklists newFragmentChat = new FragmentChecklists();

        Bundle args = new Bundle();
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

        // fetch the cached data from the activity
        mChecklistsArray = mParentActivity.getChecklistsArray();
        mChecklistsMap = mParentActivity.getChecklistsMap();

        mListViewAdapter = new ChecklistsAdapter(mParentActivity,
                mChecklistsArray,
                getResources());
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
/*
        HashMap<String,String> values = new HashMap<String, String>();
        values.put("NAME","hallo");
        values.put("Creating Date", FirebaseController.getTimestamp());
        Checklist a = new Checklist("dasda",FirebaseController.getTimestamp(),
                "my checklist", values);
        mChecklistsArray.add(mChecklistsArray.size(),a);
        mChecklistsArray.add(mChecklistsArray.size(),a);
        mChecklistsArray.add(mChecklistsArray.size(),a);
        mChecklistsArray.add(mChecklistsArray.size(),a);
        mListViewAdapter.notifyDataSetChanged();
*/
        Firebase firebase = new Firebase(FirebaseController.makeUserPath(FirebaseController.getCurrentUser()));
        firebase = firebase.child(FirebaseController.CHECKLISTS_REF);
        FirebaseController.registerChildListener(firebase, mCHECKLISTS_REF_Listener);


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


    ////////////////////////////////////////////////////////////////////////////////////////

    private class ChecklistsAdapter extends BaseAdapter {
        private final String TAG = "ChecklistsAdapter";

        private final ArrayList<Checklist> listItems;
        private final Resources resources;
        //private final FragmentChecklists mFragmentChatRef;
        private final LayoutInflater inflater;

        /*************  CustomAdapter Constructor *****************/
        public ChecklistsAdapter(MainActivity context, ArrayList listItems, Resources resLocal) {
            /********** Take passed values **********/
            mParentActivity = context;
            this.listItems = listItems;
            resources = resLocal;
            //mFragmentChatRef = ref;
            /***********  Layout inflater to call external xml layout () ***********/
            inflater = ( LayoutInflater ) mParentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        /******** What is the size of Passed Arraylist Size ************/
        public int getCount() {
            return listItems.size();
        }

        public Object getItem(int position) {
            return listItems.get(position);
        }

        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View vi, ViewGroup parent) {
            //mListView.smoothScrollToPosition(0);
            if (Globals.DEBUG_results) {
                Log.w(TAG, "getView");
            }
            ViewHolder viewHolder;
            Checklist tempValues;

            // Our listview uses two views for its rows depending on who the sender is
            if(listItems.size() > 0) {
                tempValues = listItems.get(position);
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

            // Receiving a LINK but creating a CHECKLIST to save and list !!!!!!!!

            if (!mChecklistsMap.containsKey(reference)) {
                Log.w(TAG, "Adding a checklist to UserChecklistsMap and UserChecklistsArray");

                HashMap<String,String> values = new HashMap<String, String>();
                values.put(Checklist.REF_ID,ref_id);
                values.put(Checklist.CREATION_DATE,creation_date);
                values.put(Checklist.NAME,checklistName);

                Checklist checklist = new Checklist(ref_id, creation_date, checklistName, values);
                mChecklistsArray.add(mChecklistsArray.size(), checklist);
                mChecklistsMap.put(reference, checklist);

                mListViewAdapter.notifyDataSetChanged();
            }else{
                Log.w(TAG,"Checklist_REF link already existed");
            }

//            ThreadController.delay(10000);

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
            // Receiving a LINK but creating a CHECKLIST to save and list !!!!!!!!

            HashMap<String,String> values = new HashMap<String, String>();
            values.put(Checklist.REF_ID,ref_id);
            values.put(Checklist.CREATION_DATE,creation_date);
            values.put(Checklist.NAME,checklistName);

            // Create a new Checklist based on the changed data received
            Checklist newChecklist = new Checklist(ref_id, creation_date, checklistName, values);
            // place the old checklist in array
            Checklist oldChecklist = mChecklistsArray.set(findWithinListArray(newChecklist), newChecklist);
            // use the old chaklist to find it in map and remove it
            mChecklistsMap.remove(oldChecklist.getRef_id());
            // Put the updated checklist into the map
            mChecklistsMap.put(newChecklist.getRef_id(), newChecklist);

            mListViewAdapter.notifyDataSetChanged();

//            ThreadController.delay(10000);

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
            // Receiving a LINK but creating a CHECKLIST to save and list !!!!!!!!

            HashMap<String,String> values = new HashMap<String, String>();
            values.put(Checklist.REF_ID,ref_id);
            values.put(Checklist.CREATION_DATE,creation_date);
            values.put(Checklist.NAME,checklistName);

            // Create Checklist based on the changed data received
            Checklist newChecklist = new Checklist(ref_id, creation_date, checklistName, values);
            // place the old checklist in array
            mChecklistsArray.remove(findWithinListArray(newChecklist));
            // use the old checklist to find it in map and remove it
            mChecklistsMap.remove(newChecklist.getRef_id());

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
                Log.e(TAG, "onCancelled");
            mParentActivity.makeToast("onCancelled - ERROR");
            // ValueEventListener defines a onCancelled method (lines 12 - 15) that will be
            // called if the read is ever cancelled. A read would be cancelled if the client
            // doesn't have permission to read from a Firebase location. This method will be
            // passed a FirebaseError object indicating why the failure occurred.
            if (Globals.DEBUG_invocation)
                Log.e(TAG, " - ");
        }
    };

    // returns -1 if it was not found in the array
    protected int findWithinListArray(Checklist checklist){
        for (int i = 0 ; i < mChecklistsArray.size() ; i++) {
            if (mChecklistsArray.get(i).getRef_id().equals(checklist.getRef_id())){
                return i;
            }
        }
        return -1;
    }

}
