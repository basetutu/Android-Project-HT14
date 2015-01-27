package com.malmo_university.mylists.Packaged_functions;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.malmo_university.mylists.Controllers.FirebaseController;
import com.malmo_university.mylists.Fragments.FragmentItems;
import com.malmo_university.mylists.MainActivity;
import com.malmo_university.mylists.R;
import com.malmo_university.mylists.entities.Item;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jonat_000 on 21/01/2015.
 */
public class AlertDialogs {
    private static final String TAG = "AlertDialogs";
    private static MainActivity mParentActivity;
    private static LayoutInflater mLayoutInflater;

    public static void init(MainActivity context, LayoutInflater layoutInflater) {
        mParentActivity = context;
        mLayoutInflater = layoutInflater;
    }

    public static void makeCloseChecklistDialog(final int index) {
        if (index != -1) {
            if (checkForNull()) {
                final AlertDialog.Builder newDialogBuilder = new AlertDialog.Builder(mParentActivity);
                newDialogBuilder.setTitle("Do you wish to close this checklist?");
                newDialogBuilder.setCancelable(true);

                newDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                newDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mParentActivity.closeChecklist(index);
                    }
                });

                AlertDialog newDialog = newDialogBuilder.create();
                newDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                newDialog.show();
            }
        }
    }

    public static void makeNewChecklistDialog() {
        if (checkForNull()) {
            final AlertDialog.Builder newDialogBuilder = new AlertDialog.Builder(mParentActivity);
            newDialogBuilder.setTitle("Create new checklist");
            newDialogBuilder.setCancelable(true);
            // Set an EditText view to get user input
            View alertDialogView = mLayoutInflater.inflate(R.layout.dialog_one_edit_text, null);
            final EditText editText = (EditText) alertDialogView.findViewById(R.id.alertDialog_one_editText);
            newDialogBuilder.setView(alertDialogView);

            newDialogBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    mParentActivity.newChecklist(editText.getText().toString());
                }
            });

            newDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });

            AlertDialog newDialog = newDialogBuilder.create();
            newDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            newDialog.show();
        }
    }

    public static void makeNewItemDialog(final String checklist_ref_id) {
        Log.w(TAG, "makeNewItemDialog - checklist_ref_id: " + checklist_ref_id);
        if (checkForNull()) {
            final AlertDialog.Builder newDialogBuilder = new AlertDialog.Builder(mParentActivity);
            newDialogBuilder.setTitle("Create a new item");
            newDialogBuilder.setCancelable(true);
            // Set two EditText views to get user input
            View alertDialogView = mLayoutInflater.inflate(R.layout.dialog_two_edit_texts, null);
            final EditText title = (EditText) alertDialogView.findViewById(R.id.alertDialog_2_upper_editText);
            final EditText note = (EditText) alertDialogView.findViewById(R.id.alertDialog_2_lower_editText);

            newDialogBuilder.setView(alertDialogView);

            newDialogBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    mParentActivity.newItem(checklist_ref_id, title.getText().toString(), note.getText().toString());
                }
            });

            newDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });

            AlertDialog newDialog = newDialogBuilder.create();
            newDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            newDialog.show();
        }
    }

    protected static void makeLongPressChecklistDialog(final String checklist_ref_id) {
        if (checkForNull()) {
            final AlertDialog.Builder newDialogBuilder = new AlertDialog.Builder(mParentActivity);
            newDialogBuilder.setTitle("Select action");
            newDialogBuilder.setCancelable(true);

            newDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });

            newDialogBuilder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    mParentActivity.deleteChecklist(checklist_ref_id);
                }
            });

            newDialogBuilder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    makeRenameChecklistDialog(checklist_ref_id);
                }
            });

            AlertDialog newDialog = newDialogBuilder.create();
            newDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            newDialog.show();
        }
    }

    protected static void makeRenameChecklistDialog(final String checklist_ref_id) {
        if (checkForNull()) {
            final AlertDialog.Builder newDialogBuilder = new AlertDialog.Builder(mParentActivity);
            newDialogBuilder.setTitle("Type the new name here");
            newDialogBuilder.setCancelable(true);
            // Set an EditText view to get user input
            View alertDialogView = mLayoutInflater.inflate(R.layout.dialog_one_edit_text, null);
            final EditText editText = (EditText) alertDialogView.findViewById(R.id.alertDialog_one_editText);
            newDialogBuilder.setView(alertDialogView);

            newDialogBuilder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //todo
                    FirebaseController.renameChecklist(checklist_ref_id, editText.getText().toString());

                }
            });

            newDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });

            AlertDialog newDialog = newDialogBuilder.create();
            newDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            newDialog.show();
        }
    }

    public static void makeEditItemDialog(final Item item) {
        Log.w(TAG, "makeNewItemDialog - checklist_ref_id: " + item.getChecklist_ref_id());
        if (checkForNull()) {
            final AlertDialog.Builder newDialogBuilder = new AlertDialog.Builder(mParentActivity);
            newDialogBuilder.setTitle("Create a new item");
            newDialogBuilder.setCancelable(true);
            // Create VIEW
            // Set two EditText views to get user input
            View alertDialogView = mLayoutInflater.inflate(R.layout.dialog_two_edit_texts, null);
            final EditText title = (EditText) alertDialogView.findViewById(R.id.alertDialog_2_upper_editText);
            final EditText note = (EditText) alertDialogView.findViewById(R.id.alertDialog_2_lower_editText);
            // set the old data to be edited
            title.setText(item.getTitle());
            note.setText(item.getNote());

            newDialogBuilder.setView(alertDialogView);

            // Set button listeners
            newDialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    mParentActivity.editItem(item, title.getText().toString(), note.getText().toString());
                }
            });

            newDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });

            AlertDialog newDialog = newDialogBuilder.create();
            newDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            newDialog.show();
        }
    }

    public static void makeLongPressItemDialog(final FragmentItems fragment, final int position, final Item item, final HashMap<String, Item> mItemsMap, final ArrayList<Item> mItemsArray) {
        if (checkForNull()) {
            final AlertDialog.Builder newDialogBuilder = new AlertDialog.Builder(mParentActivity);
            newDialogBuilder.setTitle("What do you wish to do with this item?");
            newDialogBuilder.setCancelable(true);

            newDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });

            newDialogBuilder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    fragment.deleteItem(position, item, mItemsArray, mItemsMap);
                }
            });

            newDialogBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    makeEditItemDialog(item);
                }
            });

            AlertDialog newDialog = newDialogBuilder.create();
            newDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            newDialog.show();
        }
    }

    private static boolean checkForNull() {
        if ((mParentActivity == null) || (mLayoutInflater == null)) {
            return false;
        }
        return true;
    }
}
