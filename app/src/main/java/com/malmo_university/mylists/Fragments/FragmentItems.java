package com.malmo_university.mylists.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
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
import com.malmo_university.mylists.entities.Checklist;
import com.malmo_university.mylists.entities.Item;
import com.malmo_university.mylists.entities.Link;

import java.util.ArrayList;
import java.util.Map;

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
    // The ref_id of the checklist
    private String mChecklist_ref_id;
    // The checklist-reference of firebase
    private Firebase mFirebaseChecklist;

    //private HashMap<String, Item> mItemsMap;
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
        mChecklist_ref_id = args.getString(FRAGMENT_REF_ID);
        mFirebaseChecklist = new Firebase(FirebaseController.makeChecklistPath(args.getString(FRAGMENT_REF_ID)));

        Checklist checklist = mParentActivity.getChecklistsMap().get(mChecklist_ref_id);
        mItemsArray = checklist.getItems();

        // Create the Arraylist that will store our group-names from the list
        //mItemsArray = new ArrayList<Item>(50);
        //mItemsMap = new HashMap<String, Item>(100);

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

        // Register mITEMS_Listener
        Firebase firebase = new Firebase(FirebaseController.makeItemsPath(mChecklist_ref_id));
        FirebaseController.registerChildListener(firebase, mITEMS_Listener);


//        HashMap<String,String> values = new HashMap<String, String>();
//        values.put("NAME","hallo");
//        values.put("Creating Date", FirebaseController.getTimestamp());
//        int order = 0;
//        boolean state = false;
//        Item a = new Item("ref id", "checklistId", "lastModifiedBy", "date added", order,
//        "ItemTitle", "ItemNote", state);
//        mItemsArray.add(mItemsArray.size(),a);
//        mItemsArray.add(mItemsArray.size(),a);
//        state = true;
//        a = new Item("ref id", "checklistId", "lastModifiedBy", "date added", order,
//                "Itesdf sdmTitlef fs fsdf sdf sdf sdfsdf sdf", "Itef sdff mNotes fsf sdf sfd sdfsdf sf", state);
//        mItemsArray.add(mItemsArray.size(),a);
//        mItemsArray.add(mItemsArray.size(),a);
//        mListViewAdapter.notifyDataSetChanged();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (Globals.DEBUG_invocation)
            Log.w(TAG, "onCreateOptionsMenu");

        inflater.inflate(R.menu.menu_fragment_items, menu);

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
            case R.id.menu_item_add_item_fragmentItem:
                AlertDialogs.makeNewItemDialog(mChecklist_ref_id);
                return true;
            case R.id.menu_item_logout_fragmentItem:
                mParentActivity.logoutCleanUp();
                return true;
            case R.id.menu_item_close_fragmentItem:
                return true;
            case R.id.menu_item_exit_fragmentItem:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////


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
                viewHolder.title.setText("Empty checklist...");
                viewHolder.note.setText("");
                viewHolder.check.setVisibility(View.INVISIBLE);
            } else {
                /************  Set Model values     from Holder elements ***********/
                viewHolder.title.setText(tempValues.getTitle());
                viewHolder.note.setText(tempValues.getNote());
                if (tempValues.getChecked()){
                    viewHolder.check.setVisibility(View.VISIBLE);
                    viewHolder.title.setPaintFlags(viewHolder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolder.note.setPaintFlags(viewHolder.note.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }else {
                    viewHolder.check.setVisibility(View.INVISIBLE);
                    viewHolder.title.setPaintFlags( viewHolder.title.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    viewHolder.note.setPaintFlags( viewHolder.note.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }
                /******** Set Item Click Listener for LayoutInflater for each row *******/
                vi.setOnClickListener(onItemClickListener);
                vi.setOnLongClickListener(onItemLongClickListener);
            }
            return vi;
        }

        // One listener to rule them all
        private View.OnClickListener onItemClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onChecklistItemClicked(mListView.getPositionForView(v));
            }
        };

        // One listener to rule them all
        private View.OnLongClickListener onItemLongClickListener = new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                onChecklistItemLongClicked(mListView.getPositionForView(v));
                return true;
            }
        };

        /********* Create a holder Class to contain inflated xml file elements *********/
        public class ViewHolder{
            public TextView title;
            public TextView note;
            public ImageView check;
        }

    }

    private void onChecklistItemClicked(int positionForView) {
        Log.w(TAG,"onChecklistItemClicked");
        mItemsArray.get(positionForView).toggleChecked();
        mListViewAdapter.notifyDataSetChanged();
        Item item = mParentActivity.getChecklistsMap().get(mChecklist_ref_id).getItems().get(positionForView);
        boolean checked = item.getChecked();
        Log.e(TAG,""+checked);
        String checklist_ref_id = item.getChecklist_ref_id();
        Log.e(TAG,checklist_ref_id);
        String item_ref_id = item.getRef_id();
        Log.e(TAG,item_ref_id);

        // Needs items
        FirebaseController.checkItemOnChecklist(checklist_ref_id, item_ref_id, checked);
    }

    private void onChecklistItemLongClicked(int mPosition) {
        Log.w(TAG,"onChecklistItemLongClicked");
        // start dialog for long clicking an item in a checklist
        AlertDialogs.makeLongPressItemDialog();
    }


    protected void setLastItemVisible(boolean state){
        mLastItemVisible = state;
    }



    // LISTENERS /////////////////////////////////////////////////////////////////////////////

    private ChildEventListener mUSERS_REF_Listener = new ChildEventListener() {
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

    private ChildEventListener mVALUES_CHECKLIST_Listener = new ChildEventListener() {
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

    private ChildEventListener mITEMS_Listener = new ChildEventListener() {
        // todo

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (Globals.DEBUG_invocation) {
                Log.w(TAG, "onChildAdded");
            }
            // We do this only to be able to divide the string that we receive into variable
            // like "id" and "from".
            Map<String, Link> dataMap = (Map<String, Link>) dataSnapshot.getValue();
            // Extract data
            String checked = String.valueOf(dataMap.get("checked"));
            String checklist_ref_id = String.valueOf(dataMap.get("checklist_ref_id"));
            String creation_date = String.valueOf(dataMap.get("creation_date"));
            String lastModifiedBy = String.valueOf(dataMap.get("lastModifiedBy"));
            String note = String.valueOf(dataMap.get("note"));
            String order = String.valueOf(dataMap.get("order"));
            String ref_id = String.valueOf(dataMap.get("ref_id"));
            String title = String.valueOf(dataMap.get("title"));
            // DEBUG
            if (Globals.DEBUG_results) {
                Log.i(TAG, "Child checked: " + checked);
                Log.i(TAG, "Child checklist_ref_id: " + checklist_ref_id);
                Log.i(TAG, "Child creation_date: " + creation_date);
                Log.i(TAG, "Child lastModifiedBy: " + lastModifiedBy);
                Log.i(TAG, "Child note: " + note);
                Log.i(TAG, "Child order: " + order);
                Log.i(TAG, "Child ref_id: " + ref_id);
                Log.i(TAG, "Child title: " + title);
            }

            Item item = new Item(ref_id, checklist_ref_id, lastModifiedBy, creation_date, 0, title, note, checked.equals("true"));

            // todo psudo

            //Checklist checklist = mParentActivity.getChecklistsMap().get(checklist_ref_id);
            //checklist.getItems().add(checklist.getItems().size(), item);
            mItemsArray.add(mItemsArray.size(), item);

            mListViewAdapter.notifyDataSetChanged();

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

    //////////////////////////////////////////////////////////////////////////////////////////
}
