package com.malmo_university.mylists.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.malmo_university.mylists.ActivityAuthenticate;
import com.malmo_university.mylists.Controllers.FirebaseController;
import com.malmo_university.mylists.R;

/**
 * Created by Martin on 02-12-2014.
 */
public class FragmentRegistration extends Fragment {
    private static final String TAG = "FragmentRegistration";

    private EditText etUsernameField;
    private EditText etPasswordField;
    private EditText etPasswordField2;
    private Button btnRegister;

    private ActivityAuthenticate parentActivity;

//    private FirebaseController mFirebaseCont;
    private Firebase mFirebase;

    //////////////////////////////////////////////////////////////////////////////////////

    public static FragmentRegistration newInstance(String firebaseRootRef) {
        if (Globals.DEBUG_invocation)
            Log.w(TAG, "newInstance");

        FragmentRegistration newFragmentRegistration = new FragmentRegistration();

        Bundle args = new Bundle();
        args.putString("firebaseRootRef", firebaseRootRef);
        newFragmentRegistration.setArguments(args);

        if (Globals.DEBUG_invocation)
            Log.w(TAG, " - ");
        return newFragmentRegistration;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Globals.DEBUG_invocation)
            Log.w(TAG, "onCreate");

        Bundle args = getArguments();
        mFirebase = new Firebase(args.getString("firebaseRootRef"));

        if (Globals.DEBUG_invocation)
            Log.w(TAG, " - ");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Globals.DEBUG_invocation)
            Log.w(TAG,"onActivityCreated");

        setHasOptionsMenu(true);
        if (Globals.DEBUG_invocation)
            Log.w(TAG," - onActivityCreated");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (Globals.DEBUG_invocation)
            Log.w(TAG,"onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_registration, container, false);

        etUsernameField = (EditText) rootView.findViewById(R.id.et_registration_user);
        etPasswordField = (EditText) rootView.findViewById(R.id.et_registration_pass);
        etPasswordField2 = (EditText) rootView.findViewById(R.id.et_registration_pass2);

        btnRegister = (Button) rootView.findViewById(R.id.btn_registration_register);

        parentActivity = (ActivityAuthenticate) getActivity();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == btnRegister.getId()) {
                    if (etUsernameField.getText().toString().equals("")) {
                        Toast.makeText(parentActivity, "Requered field empty!", Toast.LENGTH_SHORT).show();
                    } else if (etPasswordField.getText().toString().length() < 2) {
                        Toast.makeText(parentActivity, "Password must be longer than 1 characters!", Toast.LENGTH_SHORT).show();
                    } else if (!etPasswordField.getText().toString().equals(etPasswordField2.getText().toString())) {
                        Toast.makeText(parentActivity, "Passwords to not match!", Toast.LENGTH_SHORT).show();
                    } else if (!etUsernameField.getText().toString().contains("@")) {
                        Toast.makeText(parentActivity, "Username must be an email-address!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(parentActivity, "Trying to register...", Toast.LENGTH_SHORT).show();
                        btnRegisterUserPressed(
                                etUsernameField.getText().toString(),
                                etPasswordField.getText().toString());
                    }
                }
            }
        });
        if (Globals.DEBUG_invocation)
            Log.w(TAG," - onCreateView");
        return rootView;
    }

    // MENU ///////////////

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (Globals.DEBUG_invocation)
            Log.w(TAG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_fragment_registeration, menu);
        if (Globals.DEBUG_invocation)
            Log.w(TAG, " - onCreateOptionsMenu");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (Globals.DEBUG_invocation) {
            Log.w(TAG, "onOptionsItemSelected");
            Log.w(TAG, " - onOptionsItemSelected");
        }
        // handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_reg_exit:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    protected void btnRegisterUserPressed(String user, String pass) {
        FirebaseController.createNewUser(getActivity(), mFirebase, user, pass);
    }
}
