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

    /* This Custom Adapter sets a custom view for each row of the ArrayList.
    *  If I extend <ActivityData>, this view could be amended to show
    *  other attributes of each ActivityData object.
    *
    *  Currently, it sets the position number and name of the activity
    *  for each row. */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //call inflater to create a View from an xml layout file
        convertView = LayoutInflater.from(context).inflate(R.layout.row, parent, false);

        //get references for row widgets
        activityPosition = convertView.findViewById(R.id.activityPosition);
        activityName = convertView.findViewById(R.id.activityName);

        activityPosition.setText(" " + (position + 1));
        activityName.setText(arrayList.get(position).getActivity());
        return convertView;
    }
}
