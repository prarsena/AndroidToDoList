package com.example.todolist;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText selection;
    private ListView listview;
    public ArrayList<ActivityData> items;
    CustomAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        getSupportActionBar();

        listview = (ListView)findViewById(R.id.list);
        listview.setOnItemClickListener(this);

        selection= (EditText) findViewById(R.id.listitem);

        items = new ArrayList<ActivityData>();
        items.add(new ActivityData(1, "Study"));
        items.add(new ActivityData(2, "Shop"));
        items.add(new ActivityData(3, "Sleep"));

        adapter = new CustomAdapter(this, items);

        listview.setAdapter(adapter);    //connect custom adapter to <ListView>
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        String text = items.get(position).getActivity();
        selection.setText(text);
        //toast.makeText(this, "List item selected " + items[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Toast.makeText(this, "Option item " + item + " selected. Size of array " + items.get(2).getActivity(), Toast.LENGTH_LONG).show();
                selection.getText();
                items.size();
                items.add(new ActivityData(items.size()+1, selection.getText().toString()));
                return true;

            case R.id.delete:
                return true;

            case R.id.update:
                return true;

            case R.id.save:
                return true;

            case R.id.exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}