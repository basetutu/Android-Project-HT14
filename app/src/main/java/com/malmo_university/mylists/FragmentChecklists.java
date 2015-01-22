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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentChecklists extends Fragment {
    private static final String TAG = "FragmentChecklists";
    protected MainActivity mParentActivity;

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    private ArrayList<Checklist> mListViewChecklists;
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private ChecklistsAdapter mListViewAdapter;

    private ListView mListView;

    private boolean childListenerRegistered = false;

    // As default the list must scroll down lot the lowest item on the list
    private boolean mLastItemVisible = true;

    ////////////////////////////////////////////////////////////////////////////////////////

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
        mListViewChecklists = mParentActivity.getChecklists();


        // Create the Arraylist that will store our group-names from the list
        mListViewChecklists = new ArrayList<Checklist>(50);

        mListViewAdapter = new ChecklistsAdapter(mParentActivity,
                mListViewChecklists,
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
        mListView = (ListView) rootView.findViewById(R.id.listview_checklists);
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
        Checklist a = new Checklist("dasda",FirebaseController.getTimestamp(),
                "my checklist", values);
        mListViewChecklists.add(mListViewChecklists.size(),a);
        mListViewChecklists.add(mListViewChecklists.size(),a);
        mListViewChecklists.add(mListViewChecklists.size(),a);
        mListViewChecklists.add(mListViewChecklists.size(),a);
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
