package com.malmo_university.mylists;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentChecklists extends Fragment {
    String[] checklists;
    List<TestData> myTestData;
    View rootView;

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
        myTestData = new ArrayList<TestData>();
        super.onCreate(savedInstanceState);
        Log.w("fsdf","onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w("fsdf", "onCreateView");

        //checklists = new String[] {"potatis","mjölk"};

        rootView = inflater.inflate(R.layout.fragment_checklists, container, false);

        populateTestData();
        populateListView(inflater);

        //replaces R.layout... "android.R.layout.simple_list_item_1"

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
        //        R.layout.testlayout, R.id.test_left_tv, checklists);


        //ListAdaptor adapter = new ListAdaptor(getActivity(), R.layout.testlayout, myDataArray);

        //ListView list = (ListView) rootView.findViewById(R.id.listview_checklists);
        //list.setAdapter(adapter);

        ////

        return rootView;
    }

    private void populateTestData() {
        myTestData.add(new TestData("ShoppingList","Eggs,Milk"));
        myTestData.add(new TestData("TodoList","Training,Studying"));
        myTestData.add(new TestData("RememberList","Mom's birthday soon"));
    }

    private ListView populateListView(LayoutInflater inflater) {
        ArrayAdapter<TestData> adapter = new MyListAdapter(inflater);
        ListView list = (ListView) rootView.findViewById(R.id.listview_checklists);
        list.setAdapter(adapter);

        return list;
    }

    private class MyListAdapter extends ArrayAdapter<TestData> {
        LayoutInflater inflater;

        private MyListAdapter(LayoutInflater inflater) {
            super(getActivity(), R.layout.testlayout, myTestData);
            this.inflater = inflater;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(convertView == null){
                itemView = inflater.inflate(R.layout.testlayout, parent, false);
            }

            TestData currentPos = myTestData.get(position);

            TextView title = (TextView) itemView.findViewById(R.id.test_left_tv);
            title.setText(currentPos.GetTitle());

            TextView description = (TextView) itemView.findViewById(R.id.test_right_tv);
            description.setText(currentPos.GetDescription());

            return itemView;
        }
    }

    @Override
    public void onPause() {
        myTestData.clear();
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
        myTestData.clear();
        super.onStop();
        Log.w("fsdf","onStop");
    }

    @Override
    public void onDestroy() {
        myTestData.clear();
        super.onDestroy();
        Log.w("fsdf","onDestroy");
    }

}
