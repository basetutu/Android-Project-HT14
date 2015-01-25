package com.malmo_university.mylists.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.malmo_university.mylists.ActivityAuthenticate;
import com.malmo_university.mylists.R;

/**
 * Created by Martin on 02-12-2014.
 */
public class FragmentAbout extends Fragment {
    private static final String TAG = "FragmentAbout";
    Button btnGoBack;
    private ActivityAuthenticate parentActivity;

    public static FragmentAbout newInstance() {
        Log.w(TAG, "newInstance");

        FragmentAbout newFragmentAbout = new FragmentAbout();

        Log.w(TAG, " - ");
        return newFragmentAbout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        parentActivity = (ActivityAuthenticate) getActivity();

        btnGoBack = (Button) rootView.findViewById(R.id.btn_about_back);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == btnGoBack.getId()) {
                    btnGoBackPressed();
                }
            }
        });

        return rootView;
    }

    // MENU ///////////////

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_about, menu);
    }


    protected void btnGoBackPressed() {
        parentActivity.changeFragment(Globals.FRAGMENT_LOGIN);
    }

}
