package com.malmo_university.mylists;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

public class FragmentLogin extends Fragment {
    public static final String TAG = "FragmentLogin";
    //Variables for the broadcast receiver of this class
    protected static final String RECEIVER_ACTION = "com.malmo_university.mylists.FragmentLogin";

    private EditText etUsernameField;
    private EditText etPasswordField;
    private Button btnLogin;
    private Button btnRegister;
    private Button btnAbout;
    private Button btnExit;
    private ActivityAuthenticate mParentActivity;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int message = intent.getExtras().getInt("message");
            if (message == Globals.MESSAGE_AUTHENTICATED) {
                if (etUsernameField.getText().toString().length() != 0) {
                    // Save the username that is just authenticated into SharedPreferences
                    SharedPreferencesController.simpleWritePersistentString(
                            Globals.USERNAME,
                            Algorithms.removeAllSpaces(etUsernameField.getText().toString()));
                    SharedPreferencesController.simpleWritePersistentString(
                            Globals.PASSWORD,
                            etPasswordField.getText().toString());
                }
                Intent startIntent = new Intent(mParentActivity, MainActivity.class);
                startActivity(startIntent);

                mParentActivity.finish();
            } else if (message == Globals.MESSAGE_NOT_AUTHENTICATED) {
                Log.d(TAG, "NOT AUTHENTICATED !!!");
            }
            btnLogin.setEnabled(true);
        }
    };

    private Firebase mFirebase;

    ////////////////////////////////////////////////////////////////////////////////////////

    public static FragmentLogin newInstance(String firebaseRootRef) {
        Log.w(TAG, "newInstance");

        FragmentLogin newFragmentLogin = new FragmentLogin();

        Bundle args = new Bundle();
        args.putString("firebaseRootRef", firebaseRootRef);
        newFragmentLogin.setArguments(args);

        Log.w(TAG, " - newInstance");
        return newFragmentLogin;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG, "onCreate");

        mParentActivity = (ActivityAuthenticate) getActivity();

        Firebase.setAndroidContext(mParentActivity);

        Bundle args = getArguments();
        mFirebase = new Firebase(args.getString("firebaseRootRef"));

        Log.w(TAG, " - onCreate");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.w(TAG, "onActivityCreated");
        setHasOptionsMenu(true);
        Log.w(TAG, " - onActivityCreated");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        // Reference to views
        etUsernameField = (EditText) rootView.findViewById(R.id.et_user);
        etPasswordField = (EditText) rootView.findViewById(R.id.et_pass);
        btnLogin =        (Button)   rootView.findViewById(R.id.btn_login);
        btnRegister =     (Button)   rootView.findViewById(R.id.btn_reg);
        btnAbout =        (Button)   rootView.findViewById(R.id.btn_about_open);
        btnExit =         (Button)   rootView.findViewById(R.id.btn_exit);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == btnLogin.getId()) {
                    Toast.makeText(mParentActivity, "Trying to login...", Toast.LENGTH_SHORT).show();
                    btnLoginPressed(etUsernameField.getText().toString(), etPasswordField.getText().toString());
                }
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == btnAbout.getId()) {
                    btnAboutPressed();
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == btnRegister.getId()) {
                    btnRegisterPressed();
                }
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == btnExit.getId()) {
                    btnExitPressed();
                }
            }
        });
        Log.w(TAG, " - onCreateView");
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w(TAG, "onResume");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVER_ACTION);
        mParentActivity.registerReceiver(mReceiver, intentFilter);

        SoftInputController.hideSoftInputKeyboard(mParentActivity, etUsernameField);

        String user = SharedPreferencesController.simpleReadPersistentString(Globals.USERNAME);
        String pass = SharedPreferencesController.simpleReadPersistentString(Globals.PASSWORD);
        Log.w(TAG, "User: " + user);
        Log.w(TAG, "Pass: " + pass);
        if (user != null && pass != null){
            btnLogin.setEnabled(false);
            FirebaseController.login(mParentActivity,mFirebase, user, pass, RECEIVER_ACTION,
                    Globals.MESSAGE_AUTHENTICATED, Globals.MESSAGE_NOT_AUTHENTICATED);
        }

        Log.w(TAG, " - onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w(TAG, "onPause");
        mParentActivity.unregisterReceiver(mReceiver);

        SoftInputController.hideSoftInputKeyboard(mParentActivity, etUsernameField);

        Log.w(TAG, " - onPause");
    }

    // MENU ///////////////

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.w(TAG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_fragment_login, menu);
        Log.w(TAG, " - onCreateOptionsMenu");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.w(TAG, "onOptionsItemSelected");
        // handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_login_close:
                Log.w(TAG, " - onOptionsItemSelected");
                getActivity().finish();
            case R.id.menu_item_login_exit:
                Log.w(TAG, " - onOptionsItemSelected");
                getActivity().finish();
            default:
                Log.w(TAG, " - onOptionsItemSelected");
                return super.onOptionsItemSelected(item);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////////// FUNCTIONAL BUTTONS //////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////

    public void btnLoginPressed(String user, String pass) {
        Log.w(TAG, "btnLoginPressed");
        btnLogin.setEnabled(false);
        user = Algorithms.removeAllSpaces(user);
        if (mFirebase == null) {
            Toast.makeText(mParentActivity, "You have no internet connection, retrying...", Toast.LENGTH_SHORT).show();
            mFirebase = new Firebase(getArguments().getString("firebaseRootRef"));
            if (mFirebase != null) {
                Toast.makeText(mParentActivity, "... Connected", Toast.LENGTH_SHORT).show();
                FirebaseController.login(mParentActivity, mFirebase, user, pass, FragmentLogin.RECEIVER_ACTION,
                        Globals.MESSAGE_AUTHENTICATED, Globals.MESSAGE_NOT_AUTHENTICATED);
            } else {
                Toast.makeText(mParentActivity, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            FirebaseController.login(mParentActivity, mFirebase, user, pass, FragmentLogin.RECEIVER_ACTION,
                    Globals.MESSAGE_AUTHENTICATED, Globals.MESSAGE_NOT_AUTHENTICATED);
        }
        Log.w(TAG, " - btnLoginPressed");
    }

    protected void btnRegisterPressed() {
        Log.w(TAG, "btnRegisterPressed");
        mParentActivity.changeFragment(Globals.FRAGMENT_REGISTRATION);
        Log.w(TAG, " - btnRegisterPressed");
    }

    protected void btnAboutPressed() {
        Log.w(TAG, "btnAboutPressed");
        mParentActivity.changeFragment(Globals.FRAGMENT_ABOUT);
        Log.w(TAG, " - btnAboutPressed");
    }

    protected void btnExitPressed() {
        Log.w(TAG, "btnExitPressed");
        mParentActivity.finish();
        Log.w(TAG, " - btnExitPressed");
    }


}

