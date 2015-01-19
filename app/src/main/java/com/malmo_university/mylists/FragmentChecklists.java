package com.malmo_university.mylists;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentChecklists extends Fragment {
    String[] checklists;
    View[] checklists_v;
    TextView txtView;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentChecklists newInstance(int sectionNumber) {
        FragmentChecklists fragment = new FragmentChecklists();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        checklists = new String[] {"potatis","mjölk"};
        super.onCreate(savedInstanceState);
        Log.w("fsdf","onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w("fsdf", "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_checklists, container, false);
        View checklistItemLeftView = inflater.inflate(R.layout.listview_item_chat_left, container, false);
        View checklistItemRightView = inflater.inflate(R.layout.listview_item_chat_right, container, false);
        checklists_v = new View[] {checklistItemLeftView, checklistItemRightView};

        /////

        ArrayAdapter<View> adapter = new ArrayAdapter<View>(getActivity(),
                android.R.layout.simple_list_item_1, checklists_v);
        ListView list = (ListView) rootView.findViewById(R.id.listview_checklists);
        list.setAdapter(adapter);
        /*
        txtView = (TextView) rootView.findViewById(R.id.textView_GroupList1);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // google fragment new instance

                if(position == 0){
                    txtView.setText("potatis");
                }
                else {
                    Fragment fragment = new ChatScreen();
                    getFragmentManager().beginTransaction().replace(R.id.container2, fragment)
                            .addToBackStack(null).commit();
                }
            }
        });
        */
        ////

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
}
