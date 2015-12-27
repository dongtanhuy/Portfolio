package group20.flashcards.flashcards;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import group20.flashcards.flashcards.CardStructure.FlashCard;
import group20.flashcards.flashcards.CardStructure.Subject;

/**
 * Created by dangn on 10/24/2015.
 */
public class InsertManualWordActivity extends AppCompatActivity {
    EditText meaning;
    EditText word;
    TextView subject;
    Button insert;
    Button done;
    Subject subj;
    ListView listword;
    List<FlashCard> flashcards;
    ArrayAdapter<String> adapter;
    ArrayList<String> listItems = new ArrayList<String>();
    final Context context = this;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_word_manual);
        flashcards = new ArrayList<FlashCard>();
        adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, listItems);

        meaning = (EditText) findViewById(R.id.insert_manual_meaning);
        word = (EditText) findViewById(R.id.insert_manual_word);
        subject = (TextView) findViewById(R.id.insert_manual_label);
        insert = (Button) findViewById(R.id.insert_manual_insertbtn);
        done = (Button) findViewById(R.id.insert_manual_done);
        listword = (ListView) findViewById(R.id.insert_manual_listword);
        listword.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null ) {
            subject.setText(extras.getString("SUBJECT"));
            subj = new Subject(extras.getString("SUBJECT"), null);
        }

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWord(); // function when click insert
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneFunc(); // function when click insert
            }
        });
    }

    public void addWord() {
        if (!word.getText().toString().equals("") && !meaning.getText().toString().equals("")) {
            flashcards.add(new FlashCard(subj, word.getText().toString(), word.getText().toString(),
                    meaning.getText().toString()));
            listItems.add(word.getText().toString());
            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
        }
        else {
            // TODO alert box
            Toast.makeText(getApplicationContext(), "Please enter both word and meaning field", Toast.LENGTH_SHORT).show();
        }
    }

    public void doneFunc() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Are you done?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked
                        Intent intent = new Intent(InsertManualWordActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subject_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
