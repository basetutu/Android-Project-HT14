package com.malmo_university.mylists;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This adapter is  a custom implementation to serve as a custom listview used in
 * FragmentChat-layout.
 *
 * Created by Saeed on 08-01-15.
 */
/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class ChatAdapter extends BaseAdapter{
    private static final String TAG = "ChatAdapter";
    /*********** Declare Used Variables *********/
    private ActivityLoggedIn mParentActivity;
    private ArrayList listItems;
    private static LayoutInflater inflater = null;
    public Resources resources;
//    private ChatMessage tempValues = null;
    private int i=0;
    // general onclickListener for all rows on the list
    private OnItemClickListener onClickListener;
    private FragmentChat mFragmentChatRef;


    /*************  CustomAdapter Constructor *****************/
    public ChatAdapter(ActivityLoggedIn context, ArrayList listItems, Resources resLocal, FragmentChat ref) {
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


    /****** Depends upon data size called for each row , Create each ListView row *****/
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
            writeToRight = (tempValues.getFrom().equals(mParentActivity.getUserName()));
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
            if(tempValues.getFrom().equals(mParentActivity.getUserName())){
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
            ((ActivityLoggedIn)mParentActivity).onChatItemClick(mPosition);
        }
    }


    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{
        public TextView from;
        public TextView message;
        public boolean writeToRight;
    }

}