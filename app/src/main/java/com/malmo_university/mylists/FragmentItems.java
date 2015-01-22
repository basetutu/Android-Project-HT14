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

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Saeed on 21-01-2015.
 */
public class FragmentItems extends Fragment{
    private static String TAG = "FragmentItems";
    private static final String FRAGMENT_REF_ID = "firebaseChecklistRef";
    private static final String CHECKLIST_NAME = "checklistName";

    private MainActivity mParentActivity;

    // This holds all the items of this checklists
    private ArrayList<Item> mItemsArray;
    // Holds the various links stored in firebase
    private ArrayList<Link> mChecklists;
    private ArrayList<Link> mContacts;
    private ArrayList<Link> mAwaiting_acceptance_links;
    // The adapter of the listview
    private ItemsAdapter mListViewAdapter;

    private ListView mListView;

    // The name of the checklist to operate in
    private String mChecklistName;
    // The checklist-reference of firebase
    private Firebase mFirebaseChecklist;

    private HashMap<String, Item> mItemsMap;
    private boolean mLastItemVisible;

    private boolean childListenerRegistered = false;


    ////////////////////////////////////////////////////////////////////////////////////////

    public static FragmentItems newInstance(String checklistName, String checklist_ref_id) {
        if (Globals.DEBUG_invocation)
            Log.w(TAG, "newInstance");
        TAG = "FragmentItems: " + checklistName;

        FragmentItems newFragmentItem = new FragmentItems();

        Bundle args = new Bundle();
        args.putString(CHECKLIST_NAME, checklistName);
        args.putString(FRAGMENT_REF_ID, checklist_ref_id);
        newFragmentItem.setArguments(args);

        if (Globals.DEBUG_invocation)
            Log.w(TAG, " - newInstance");
        return newFragmentItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Globals.DEBUG_invocation)
            Log.w(TAG, "onCreate");

        mParentActivity = (MainActivity)getActivity();


        Bundle args = getArguments();
        mChecklistName = args.getString(CHECKLIST_NAME);
        mFirebaseChecklist = new Firebase(args.getString(FRAGMENT_REF_ID));


        // Create the Arraylist that will store our group-names from the list
        mItemsArray = new ArrayList<Item>(50);
        mItemsMap = new HashMap<String, Item>(100);

        mListViewAdapter = new ItemsAdapter(mParentActivity,
                mItemsArray,
                getResources(),
                this);
        mListViewAdapter.notifyDataSetChanged();

        if (!childListenerRegistered) {
//            FirebaseController.registerValueListener(mFirebaseChecklist, mGroupValueListener);
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
    public void onPause() {
        super.onPause();
        Log.w("fsdf","onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w(TAG,"onResume");

        HashMap<String,String> values = new HashMap<String, String>();
        values.put("NAME","hallo");
        values.put("Creating Date", FirebaseController.getTimestamp());
        int order = 0;
        boolean state = false;
        Item a = new Item("ref id", "checklistId", "lastModifiedBy", "date added", order,
        "ItemTitle", "ItemNote", state);
        mItemsArray.add(mItemsArray.size(),a);
        mItemsArray.add(mItemsArray.size(),a);
        mItemsArray.add(mItemsArray.size(),a);
        mItemsArray.add(mItemsArray.size(),a);
        mListViewAdapter.notifyDataSetChanged();
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

    private class ItemsAdapter extends BaseAdapter {
        private final String TAG = "ItemsAdapter";

        private final ArrayList<Item> listItems;
        private final Resources resources;
        private final FragmentItems mFragmentItemRef;
        private final LayoutInflater inflater;

        /*************  CustomAdapter Constructor *****************/
        public ItemsAdapter(MainActivity context, ArrayList listItems, Resources resLocal, FragmentItems ref) {
            /********** Take passed values **********/
            mParentActivity = context;
            this.listItems = listItems;
            resources = resLocal;
            mFragmentItemRef = ref;
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
            if (Globals.DEBUG_results) {
                Log.w(TAG, "getView");
            }

            ViewHolder viewHolder;
            Item tempValues;
            boolean writeToRight = false;

            // Our listview uses two views for its rows depending on who the sender is
            if(listItems.size() > 0) {
                /***** Get each ChatMessage object from Arraylist ********/
                tempValues = listItems.get(position);
                // this will indicate which view to use
//            writeToRight = (tempValues.getFrom().equals(SharedPreferencesController.simpleReadPersistentString(Globals.USERNAME)));
                //writeToRight = (tempValues.getFrom().equals(FirebaseController.getCurrentUser()));
            }else{
                tempValues = null;
                writeToRight = false;
            }

            if (Globals.DEBUG_results) {
                Log.w(TAG, "writeToRight= " + writeToRight);
            }

            // If no recycled convertView has been returned, then inflate a new view and place a
            // ViewHolder in it as tag.
            // Otherwise reuse convertView by getting its ViewHolder.
            if(vi == null){
                viewHolder = new ViewHolder();
                vi = inflater.inflate(R.layout.row_item, null);
                /****** View Holder Object to contain tabitem.xml file elements ******/
                viewHolder.title = (TextView) vi.findViewById(R.id.row_item_title);
                viewHolder.note = (TextView) vi.findViewById(R.id.row_item_note);
                viewHolder.check = (ImageView) vi.findViewById(R.id.row_item_check);
                /************  Set viewHolder with LayoutInflater ************/
                vi.setTag( viewHolder );
            } else {
                // Fetch the ViewHolder and reuse the vi
                viewHolder = (ViewHolder) vi.getTag();
            }

            // Now that we have a viewholder, we can place new data inside its views
            if(tempValues == null){
                viewHolder.title.setText("Empty item title");
                viewHolder.note.setText("Empty item note");
                viewHolder.check.setVisibility(View.INVISIBLE);
            } else {
                /************  Set Model values     from Holder elements ***********/
                viewHolder.title.setText(tempValues.getTitle());
                viewHolder.note.setText(tempValues.getNote());
                /******** Set Item Click Listener for LayoutInflater for each row *******/
                vi.setOnClickListener(new OnItemClickListener( position ));
            }

            // Set weather or not the last item has been shown
            if (position >= listItems.size() -2){
                if (Globals.DEBUG_results)
                    Log.w(TAG, "last item is visible");
                setLastItemVisible(true);
            }else {
                if (Globals.DEBUG_results)
                    Log.w(TAG, "last item is NOT visible");
                setLastItemVisible(false);
            }

            return vi;
        }

        /********* Called when Item click in ListView ************/
        private class OnItemClickListener implements View.OnClickListener {
            private int mPosition;

            public OnItemClickListener(int position){
                mPosition = position;
            }
            @Override
            public void onClick(View arg0) {
                /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/
                onChecklistItemClicked(mPosition);
            }
        }

        /********* Create a holder Class to contain inflated xml file elements *********/
        public class ViewHolder{
            public TextView title;
            public TextView note;
            public ImageView check;
        }

    }

    private void onChecklistItemClicked(int mPosition) {
        //todo
        Log.w(TAG,"onChecklistItemClicked");
    }


    protected void setLastItemVisible(boolean state){
        mLastItemVisible = state;
    }

}
