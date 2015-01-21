package com.malmo_university.mylists;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by jonat_000 on 21/01/2015.
 */
public class AlertDialogs {

    private void dialogMakeNewChecklist(Activity mParentActivity ,String message) {

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

    }

    protected void makeDialogLongPressChecklist(Activity context){


    }
}
