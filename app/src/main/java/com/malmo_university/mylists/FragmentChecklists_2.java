package com.malmo_university.mylists;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * Created by Martin on 19-01-2015.
 */
public class FragmentChecklists_2  extends Fragment {
    private static final String TAG = "FragmentChecklists";

    private MainActivity mParentActivity;





    // INIT /////////////////////////////////////////////////////////////////////////////////

    public static FragmentChecklists_2 newInstance() {
        FragmentChecklists_2 fragment = new FragmentChecklists_2();
        Bundle args = new Bundle();
        //args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG, "onCreate");

        mParentActivity = (MainActivity)getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w(TAG, "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w(TAG, "onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.w(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy");
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    private void makeDialogBoxForNewGroup(String message) {
        if (Globals.DEBUG_invocation) {
            Log.w(TAG, "makeDialogBoxForNewGroup");
        }
        final AlertDialog.Builder newGroupDialogBuilder = new AlertDialog.Builder(mParentActivity);
        newGroupDialogBuilder.setTitle("Create new Group");
        newGroupDialogBuilder.setCancelable(true);
        newGroupDialogBuilder.setMessage(message);
        //alert.setIcon(R.drawable.add_contact_icon);
        // Set an EditText view to get user input
        final EditText groupNameInputEditText = new EditText(mParentActivity);
        newGroupDialogBuilder.setView(groupNameInputEditText);

        newGroupDialogBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String newGroupName = groupNameInputEditText.getText().toString();

            }
        });

        newGroupDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        AlertDialog newGroupDialog = newGroupDialogBuilder.create();
        newGroupDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        newGroupDialog.show();

        if (Globals.DEBUG_invocation) {
            Log.w(TAG, " - makeDialogBoxForNewGroup");
        }
    }


}

