package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<ActivityData> arrayList;
    private TextView activityPosition, activityName;

    public CustomAdapter(Context context, ArrayList<ActivityData> arrayList) {
        super(context, 0, arrayList);
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //call inflater to create a View from an xml layout file
        convertView = LayoutInflater.from(context).inflate(R.layout.row, parent, false);

        //get references for row widgets
        activityPosition = convertView.findViewById(R.id.activityPosition);
        activityName = convertView.findViewById(R.id.activityName);

        activityPosition.setText(" " + arrayList.get(position).getPosition());
        activityName.setText(arrayList.get(position).getActivity());
        return convertView;
    }
}
