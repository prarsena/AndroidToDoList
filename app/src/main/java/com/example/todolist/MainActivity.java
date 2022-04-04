package com.example.todolist;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText selection;
    private ListView listview;
    private int pos;
    public ArrayList<ActivityData> items;
    CustomAdapter adapter;
    private final String file = "list.txt";
    private OutputStreamWriter out;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        /* Set the Action Bar for the menu options,
        * which are defined in layout/menu/menu.xml. */
        getSupportActionBar();

        /* Set a listener on the ListView object. */
        listview = (ListView)findViewById(R.id.list);
        listview.setOnItemClickListener(this);

        selection = (EditText) findViewById(R.id.listitem);

        /* Populate the initial ArrayList.
        *  The <ActivityData> type could be extended in the future. */
        items = new ArrayList<ActivityData>();
        //open stream for reading from file
        InputStream in = null;
        if (file != null){
            try {
                in = openFileInput(file);
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(isr);
                String str = null;

                int count = 0;
                while ((str = reader.readLine()) != null) {
                    count++; // count number of records read
                    items.add(new ActivityData(str));
                }
                // toast how many records read
                Toast.makeText(this,
                        Integer.valueOf(count) + " records read",
                        Toast.LENGTH_LONG).show();

                //close input stream
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            items.add(new ActivityData("Study"));
            items.add(new ActivityData("Shop"));
            items.add(new ActivityData("Sleep"));
        }

        /* Instantiate an instance of the CustomAdapter class. */
        adapter = new CustomAdapter(this, items);

        /* connect custom adapter to <ListView> */
        listview.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        String text = items.get(position).getActivity();
        selection.setText(text);

        /* Make the position number of the list item clicked available globally to pos.
        *  I'm still not sure HOW pos becomes available to onOptionsItemSelected,
        *  but I sure am thankful it is!  */
        pos = position;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                /* When you click the Add option, the text of the EditText widget
                *  is added to the items ArrayList. */
                items.add(new ActivityData(selection.getText().toString()));
                /* Then the UI is reloaded. */
                adapter.notifyDataSetChanged();
                /* EditText is cleared. */
                selection.setText("");
                Log.e("ArrayList", "Size: " + items.size()+ ". Last entry: " + items.get(items.size()-1).getActivity());
                return true;

            case R.id.delete:
                /* If ArrayList size is 1,
                *  don't allow that item to be deleted. */
                if (items.size() <= 1){
                    Toast.makeText(this, "Down to your last item. Cannae delete.", Toast.LENGTH_LONG).show();
                } else {
                    /* Otherwise, remove item by its arraylist position. */
                    items.remove(pos);
                    /* Reload the UI. */
                    adapter.notifyDataSetChanged();
                    /* Clear the text. */
                    selection.setText("");
                    Log.e("ArrayList", "Item #" + pos + ": "+ selection.getText() + " deleted.");
                }
                return true;

            case R.id.update:
                /* Get the position of the list item in <EditText>.
                *  Then replace its content with whatever is currently in <EditText>. */
                items.set(pos, new ActivityData(selection.getText().toString()));
                /* Reload the UI */
                adapter.notifyDataSetChanged();
                /* Clear the text. */
                selection.setText("");
                Log.e("ArrayList", "Item #" + pos + " updated.");
                return true;

            case R.id.save:
                adapter.notifyDataSetChanged();
                writeToListFile(items);
                return true;

            case R.id.exit:
                writeToListFile(items);
                /* Exit activity. */
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void writeToListFile(ArrayList<ActivityData> items) {
        try {
            int recordCount = 0;
            //open output stream
            out = new OutputStreamWriter(openFileOutput(file, MODE_PRIVATE)); // also try MODE_APPEND
            for (ActivityData line : items) {
                out.write(line.getActivity() + " \n");
                Log.e("File IO", "Wrote "+line.getActivity()+" to file.");
                recordCount++;
            }
            Toast.makeText(this, "Wrote " +
                    Integer.valueOf(recordCount) + " records to file.",
                    Toast.LENGTH_LONG).show();
            //close output stream
            out.close();
        } catch (IOException e) {
            Log.e("IOTest", e.getMessage());
        }
    }
}