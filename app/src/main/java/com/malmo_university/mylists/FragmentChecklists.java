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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentChecklists extends Fragment {
    private static final String TAG = "FragmentChecklists";

    protected MainActivity mParentActivity;

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    //private ArrayList<String> mListViewMessages;
    public ArrayList<ChatMessage> mListViewMessages;
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private ChecklistsAdapter mListViewAdapter;

    // List of all ChatMessages that is received (not needed)
    private HashMap<String, ChatMessage> mMessageMap;//todo remove
    private ListView mListView;

    // The group-reference of firebase
    private Firebase mFirebaseChecklists;
    // The message-reference of the group
    private Firebase mFirebaseGroupMessages;
    private boolean childListenerRegistered = false;

    // As default the list must scroll down lot the lowest item on the list
    private boolean mLastItemVisible = true;

    private EditText text_footer_message;
    private ImageButton btn_footer_send;

    ////

    ArrayList<Checklist> mListChecklists;


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

        mListChecklists = mParentActivity.getChecklists();


//        mFirebaseChecklists = new Firebase(Globals.FIREBASE_DB_ROOT_URL).child(FirebaseController.);



        // Create the Arraylist that will store our group-names from the list
        mListViewMessages = new ArrayList<ChatMessage>(50);
        mMessageMap = new HashMap<String, ChatMessage>(100);

        mListViewAdapter = new ChecklistsAdapter(mParentActivity,
                mListViewMessages,
                getResources(),
                this);

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
        Log.w("fsdf", "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_checklists, container, false);




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
        Log.w("fsdf","onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.w("fsdf","onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w("fsdf","onDestroy");
    }


    ////////////////////////////////////////////////////////////////////////////////////////

    private class ChecklistsAdapter extends BaseAdapter {

        private final ArrayList listItems;
        private final Resources resources;
        private final FragmentChecklists mFragmentChatRef;
        private final LayoutInflater inflater;

        /*************  CustomAdapter Constructor *****************/
        public ChecklistsAdapter(MainActivity context, ArrayList listItems, Resources resLocal, FragmentChecklists ref) {
            /********** Take passed values **********/
            mParentActivity = context;
            this.listItems = listItems;
            resources = resLocal;
            mFragmentChatRef = ref;
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
            ViewHolder viewHolder;
            ChatMessage tempValues;
            boolean writeToRight;

            // Our listview uses two views for its rows depending on who the sender is
            if(listItems.size() > 0) {
                /***** Get each ChatMessage object from Arraylist ********/
                tempValues = (ChatMessage) listItems.get(position);
                // this will indicate which view to use
//            writeToRight = (tempValues.getFrom().equals(SharedPreferencesController.simpleReadPersistentString(Globals.USERNAME)));
                writeToRight = (tempValues.getFrom().equals(FirebaseController.getCurrentUser()));
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
                /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
                if (writeToRight){
                    vi = inflater.inflate(R.layout.listview_item_chat_right, null);
                    /****** View Holder Object to contain tabitem.xml file elements ******/
                    viewHolder.from    = (TextView) vi.findViewById(R.id.listview_view_right_from);
                    viewHolder.message = (TextView) vi.findViewById(R.id.listview_view_right_message);
                    viewHolder.writeToRight = true;
                }else {
                    vi = inflater.inflate(R.layout.listview_item_chat_left, null);
                    /****** View Holder Object to contain tabitem.xml file elements ******/
                    viewHolder.from    = (TextView) vi.findViewById(R.id.listview_view_left_from);
                    viewHolder.message = (TextView) vi.findViewById(R.id.listview_view_left_message);
                    viewHolder.writeToRight = false;
                }
                /************  Set viewHolder with LayoutInflater ************/
                vi.setTag( viewHolder );
            } else {
                // Identify the view that is received to see if it can be used or a new must be inflated
                if (((ViewHolder)vi.getTag()).writeToRight == writeToRight) {
                    if (Globals.DEBUG_results){
                        Log.i(TAG, "======== Correct view orientation ========");
                    }
                    // Fetch the ViewHolder and reuse the vi
                    viewHolder = (ViewHolder) vi.getTag();
                }else{
                    if (Globals.DEBUG_results) {
                        Log.i(TAG, "======== Incorrect view orientation ========");
                    }
                    // Do not reuse the vi, but reuse the contained ViewHolder
                    viewHolder = (ViewHolder) vi.getTag();

                    if (writeToRight){
                        vi = inflater.inflate(R.layout.listview_item_chat_right, null);
                        /****** View Holder Object to contain tabitem.xml file elements ******/
                        viewHolder.from    = (TextView) vi.findViewById(R.id.listview_view_right_from);
                        viewHolder.message = (TextView) vi.findViewById(R.id.listview_view_right_message);
                        viewHolder.writeToRight = true;
                    }else {
                        vi = inflater.inflate(R.layout.listview_item_chat_left, null);
                        /****** View Holder Object to contain tabitem.xml file elements ******/
                        viewHolder.from    = (TextView) vi.findViewById(R.id.listview_view_left_from);
                        viewHolder.message = (TextView) vi.findViewById(R.id.listview_view_left_message);
                        viewHolder.writeToRight = false;
                    }

                    /************  Set viewHolder with LayoutInflater ************/
                    vi.setTag( viewHolder );
                }
            }

            // Now that we have a viewholder, we can place new data inside its views
            if(tempValues == null){
                viewHolder.from.setText("");
                viewHolder.message.setText("Be the first to post a message in this group...");
            } else {
                /************  Set Model values     from Holder elements ***********/
                if(tempValues.getFrom().equals(FirebaseController.getCurrentUser())){
                    viewHolder.from.setText("You");
                    viewHolder.message.setTextColor(mParentActivity.getResources().getColor(R.color.blue_light));
                }else {
                    viewHolder.from.setText(tempValues.getFrom());
                    viewHolder.message.setTextColor(mParentActivity.getResources().getColor(R.color.orange));
                }
                viewHolder.message.setText(tempValues.getMessage());

                /******** Set Item Click Listener for LayoutInflater for each row *******/
                vi.setOnClickListener(new OnItemClickListener( position ));
            }
            // Set weather or not the last item has been shown
            if (position >= listItems.size() -2){
                if (Globals.DEBUG_results)
                    Log.w(TAG, "last item is visible");
                mFragmentChatRef.setLastItemVisible(true);
            }else {
                if (Globals.DEBUG_results)
                    Log.w(TAG, "last item is NOT visible");
                mFragmentChatRef.setLastItemVisible(false);
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
            public TextView from;
            public TextView message;
            public boolean writeToRight;
        }

    }

    private void onChecklistItemClicked(int mPosition) {
        //todo
    }

    protected void setLastItemVisible(boolean state){
        mLastItemVisible = state;
    }

}
