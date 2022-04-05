package com.example.todolist;

import android.graphics.drawable.ColorDrawable;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, OnInitListener {

    private EditText selection;
    private ListView listview;
    private int pos;
    public ArrayList<ActivityData> items;
    CustomAdapter adapter;
    private final String file = "list.txt";
    private OutputStreamWriter out;
    private TextToSpeech speaker;

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

        /* Check if the file exists to read from.
           If not, input a few default records.  */
        File checkIfFileExists = new File(this.getFilesDir()+"/list.txt");
        if (checkIfFileExists.exists()) {
            try {
                //open stream for reading from file
                InputStream in = openFileInput(file);
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(isr);
                String str = null;
                int count = 0;
                while ((str = reader.readLine()) != null) {
                    count++;
                    /* Add records to the items list to display on screen. */
                    items.add(new ActivityData(str));
                }
                /* toast how many records read from the file */
                Toast.makeText(this,
                        Integer.valueOf(count) + " records read",
                        Toast.LENGTH_LONG).show();
                /* close input stream */
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            items.add(new ActivityData("Study"));
            items.add(new ActivityData("Shop"));
            items.add(new ActivityData("Sleep"));
        }

        /* Instantiate an instance of the CustomAdapter class. */
        adapter = new CustomAdapter(this, items);

        /* connect custom adapter to <ListView> */
        listview.setAdapter(adapter);

        //Initialize Text to Speech engine (context, listener object)
        speaker = new TextToSpeech(this, this);
    }

    //speak methods will send text to be spoken
    public void speak(String output){
        speaker.speak(output, TextToSpeech.QUEUE_FLUSH, null, "Id 0");
    }

    // Implements TextToSpeech.OnInitListener.
    public void onInit(int status) {
        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // If a language is not be available, the result will indicate it.
            int result = speaker.setLanguage(Locale.US);
            //int result = speaker.setLanguage(Locale.FRANCE);

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Language data is missing or the language is not supported.
                Log.e("TTS", "Language is not available.");
            } else {
                // The TTS engine has been successfully initialized
                Log.i("TTS", "TTS Initialization successful.");
            }
        } else {
            // Initialization failed.
            Log.e("TTS", "Could not initialize TextToSpeech.");
        }
    }

    public void onDestroy(){
        // shut down TTS engine
        if(speaker != null){
            speaker.stop();
            speaker.shutdown();
        }
        super.onDestroy();
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
                String activityToAdd = selection.getText().toString().trim();
                items.add(new ActivityData(activityToAdd));

                // if speaker is talking, stop it
                if(speaker.isSpeaking()){
                    Log.i("TTS", "Speaker Speaking");
                    speaker.stop();
                    // else start speech
                } else {
                    Log.i("TTS", "Speaker Not Already Speaking");
                    speak(activityToAdd + " added.");
                }
                /* Then the UI is reloaded. */
                adapter.notifyDataSetChanged();
                /* EditText is cleared. */
                selection.setText("");
                Log.i("ArrayList", "Size: " + items.size()+ ". Last entry: " + items.get(items.size()-1).getActivity());
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

                    // if speaker is talking, stop it
                    if(speaker.isSpeaking()){
                        Log.i("TTS", "Speaker Speaking");
                        speaker.stop();
                        // else start speech
                    } else {
                        Log.i("TTS", "Speaker Not Already Speaking");
                        speak(selection.getText().toString() + " deleted.");
                    }

                    Log.i("ArrayList", "Item #" + pos + ": "+ selection.getText() + " deleted.");
                    /* Clear the text. */
                    selection.setText("");
                }
                return true;

            case R.id.update:
                /* Get the position of the list item in <EditText>.
                *  Then replace its content with whatever is currently in <EditText>. */
                items.set(pos, new ActivityData(selection.getText().toString().trim()));
                /* Reload the UI */
                adapter.notifyDataSetChanged();
                /* Clear the text. */
                selection.setText("");
                Log.i("ArrayList", "Item #" + pos + " updated.");
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
        /* Save contents of the activities list to file. */
        try {
            int recordCount = 0;
            /* open output stream */
            out = new OutputStreamWriter(openFileOutput(file, MODE_PRIVATE));
            for (ActivityData line : items) {
                out.write(line.getActivity() + " \n");
                Log.i("File IO", "Wrote "+line.getActivity()+" to file.");
                recordCount++;
            }
            Toast.makeText(this, "Wrote " +
                    Integer.valueOf(recordCount) + " records to file.",
                    Toast.LENGTH_LONG).show();
            /* close output stream */
            out.close();
        } catch (IOException e) {
            Log.e("IOTest", e.getMessage());
        }
    }
}