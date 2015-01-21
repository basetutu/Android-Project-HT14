package com.malmo_university.mylists;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by jonat_000 on 21/01/2015.
 */
public class AlertDialogs {
    private static Activity mParentActivity;
    private static LayoutInflater mLayoutInflator;

    protected void init(Activity context, LayoutInflater layoutInflater){
        this.mParentActivity = context;
        this.mLayoutInflator = layoutInflater;
    }

    protected void dialogCloseChecklist(){
        if(checkForNull()){
            final AlertDialog.Builder newGroupDialogBuilder = new AlertDialog.Builder(mParentActivity);
            newGroupDialogBuilder.setTitle("Do you wish to close this checklist?");
            newGroupDialogBuilder.setCancelable(true);

            newGroupDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });

            newGroupDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });

            AlertDialog newGroupDialog = newGroupDialogBuilder.create();
            newGroupDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            newGroupDialog.show();
        }
    }

    protected void dialogMakeNewChecklist() {
        if(checkForNull()) {
            final AlertDialog.Builder newGroupDialogBuilder = new AlertDialog.Builder(mParentActivity);
            newGroupDialogBuilder.setTitle("Create new checklist");
            newGroupDialogBuilder.setCancelable(true);
            // Set an EditText view to get user input
            View alertDialogView = mLayoutInflator.inflate(R.layout.layout_one_edit_text, null);
            newGroupDialogBuilder.setView(alertDialogView);

            newGroupDialogBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
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
    }

    protected void dialogMakeNewItem(){
        if(checkForNull()) {
            final AlertDialog.Builder newGroupDialogBuilder = new AlertDialog.Builder(mParentActivity);
            newGroupDialogBuilder.setTitle("Create a new item");
            newGroupDialogBuilder.setCancelable(true);
            // Set two EditText views to get user input
            View alertDialogView = mLayoutInflator.inflate(R.layout.layout_two_edit_texts, null);
            newGroupDialogBuilder.setView(alertDialogView);

            newGroupDialogBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
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
    }


    protected void dialogMakeLongPressChecklist(){
        if(checkForNull()){
            final AlertDialog.Builder newGroupDialogBuilder = new AlertDialog.Builder(mParentActivity);
            newGroupDialogBuilder.setTitle("What do you wish to do with this checklist?");
            newGroupDialogBuilder.setCancelable(true);

            newGroupDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });

            newGroupDialogBuilder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });

            newGroupDialogBuilder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });

            AlertDialog newGroupDialog = newGroupDialogBuilder.create();
            newGroupDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            newGroupDialog.show();
        }
    }

    protected void dialogMakeLongPressItem(){
        if(checkForNull()){
            final AlertDialog.Builder newGroupDialogBuilder = new AlertDialog.Builder(mParentActivity);
            newGroupDialogBuilder.setTitle("What do you wish to do with this item?");
            newGroupDialogBuilder.setCancelable(true);

            newGroupDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });

            newGroupDialogBuilder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });

            newGroupDialogBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });

            AlertDialog newGroupDialog = newGroupDialogBuilder.create();
            newGroupDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            newGroupDialog.show();
        }
    }

    private boolean checkForNull() {
        if((mParentActivity == null) || (mLayoutInflator == null)){
            return false;
        }
        return true;
    }
}
