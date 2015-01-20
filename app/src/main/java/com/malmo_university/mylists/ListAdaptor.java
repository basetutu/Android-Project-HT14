package com.malmo_university.mylists;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Meep on 2015-01-19.
 */
public class ListAdaptor extends ArrayAdapter<TestData> {
    private Context context;
    private int resource;
    private TestData[] objects;

    public ListAdaptor(Context context, int textViewResourceId, TestData[] objects) {
        super(context, textViewResourceId, objects);
        this.context=context;
        this.resource=textViewResourceId;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(resource,parent,false);

        TextView title  = (TextView) row.findViewById(R.id.test_left_tv);
        TextView number = (TextView) row.findViewById(R.id.test_right_tv);

        title.setText((CharSequence) objects[position].title);
        number.setText((CharSequence) (objects[position].description));

        return row;
    }
}
