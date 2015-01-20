package com.malmo_university.mylists;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Martin on 19-01-2015.
 */
public class FragmentChecklists_2  extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentChecklists_2 newInstance(int sectionNumber) {
        FragmentChecklists_2 fragment = new FragmentChecklists_2();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w("fsdf", "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("fsdf", "onCreate");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w("fsdf", "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w("fsdf", "onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.w("fsdf", "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w("fsdf", "onDestroy");
    }
}
