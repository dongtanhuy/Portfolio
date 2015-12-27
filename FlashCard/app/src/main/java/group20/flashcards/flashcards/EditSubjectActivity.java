package group20.flashcards.flashcards;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.util.JsonWriter;
import android.widget.Toast;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.InterruptedException;

import group20.flashcards.flashcards.Adapter.CardsAdapter;
import group20.flashcards.flashcards.Adapter.SubjectsAdapter;
import group20.flashcards.flashcards.CardStructure.FlashCard;
import group20.flashcards.flashcards.CardStructure.Subject;
import group20.flashcards.flashcards.Database.DatabaseHandler;

public class EditSubjectActivity extends AppCompatActivity {

    TextView tvSubject;
    Button btnNew;
    Button btnExport;
    Button btnDelete;
    Toolbar toolbar;
    ArrayList<FlashCard> flashcards = new ArrayList<>();
    final int CARD_EDIT = 2;
    /* global variable for cancel exporting process */
    boolean cancelation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subject);

        toolbar = (Toolbar) findViewById(R.id.toolbar3);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get TextView and Buttons from activity
        tvSubject = (TextView) toolbar.findViewById(R.id.tv_subject_title);
        btnNew = (Button) findViewById(R.id.btn_new_card);
        btnExport = (Button) findViewById(R.id.btn_export);
        btnDelete = (Button) findViewById(R.id.btn_delete_subject);

        final Intent intent = getIntent();
        final String subjectTitle = intent.getStringExtra("SUBJECT");

        tvSubject.setText(subjectTitle + " Subject");

        final DatabaseHandler db = new DatabaseHandler(this);
        // Get subject from the database
        final Subject subject = db.getSubjectByTitle(subjectTitle);
        // Get List of card for the subject
        flashcards = db.getFlashCardsBySubjectID(subject.getId());

        // Show list of card in listview
        final ListAdapter list_card_adapter = new CardsAdapter(this, android.R.layout.simple_list_item_1, flashcards);
        ListView subjects_list = (ListView) findViewById(R.id.list_cards);
        subjects_list.setAdapter(list_card_adapter);
        subjects_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EditSubjectActivity.this, EditCardActivity.class);
                intent.putExtra("OPTION", "UPDATE");
                intent.putExtra("FLASHCARD_ID", flashcards.get(position).getId());
                intent.putExtra("SUBJECT_ID", subject.getId());
                intent.putExtra("EDIT_SUBJECT", subjectTitle);
                intent.putExtra("TITLE", flashcards.get(position).getTitle());
                intent.putExtra("CONTENT", flashcards.get(position).getContent());
                intent.putExtra("ANSWER", flashcards.get(position).getAnswer());
                startActivityForResult(intent, CARD_EDIT);
            }
        });

        // ADD NEW CARD ON CLICK
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditSubjectActivity.this, EditCardActivity.class);
                intent.putExtra("OPTION", "ADD");
                intent.putExtra("EDIT_SUBJECT", subjectTitle);
                startActivityForResult(intent, CARD_EDIT);
            }
        });
        // DELETE CARD ON CLICK
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = db.deleteSubject(subject.getId());
                Intent sendBack = new Intent();
                sendBack.putExtra("DELETE_STATUS", success);
                setResult(RESULT_OK, sendBack);
                finish();
            }
        });
        //EXPORT CLICK
        btnExport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder confirm = new AlertDialog.Builder(EditSubjectActivity.this);
                confirm.setMessage("Do you want export this subject?")
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    cancelation = false;
                                    writeSubject(subjectTitle);
                                } catch (IOException ex) {

                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                AlertDialog alert = confirm.create();
                alert.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            boolean addCardStatus = data.getBooleanExtra("ADD_CARD_STATUS", false);
            boolean updateCardStatus = data.getBooleanExtra("UPDATE_CARD_STATUS", false);
            boolean deleteCardStatus = data.getBooleanExtra("DELETE_CARD_STATUS", false);
            if (addCardStatus || updateCardStatus || deleteCardStatus) {
                finish();
                startActivity(getIntent());
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_subject, menu);
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
        } else if(id == android.R.id.home) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    //For json write subject
    public void writeSubject(final String subject) throws IOException {
        File newFolder;

        if (Environment.isExternalStorageEmulated() || Environment.isExternalStorageRemovable()) {
            newFolder = new File(Environment.getExternalStorageDirectory(), "FlashCard");
        } else {
            newFolder = new File("FlashCard");
        }

        if (!newFolder.exists()) {
            newFolder.mkdir();
        }

        String filename = subject + ".json";
        final File file = new File(newFolder, filename);

        FileWriter out = new FileWriter(file);
        final JsonWriter writer = new JsonWriter(out);

        /* Progress bar */
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Exporting Subject");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(false);
        progress.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                cancelation = true;
            }
        });
        progress.setCancelable(true);
        progress.setProgress(0);
        progress.setMax(flashcards.size());
        progress.show();

        /* begin writing */
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    writer.beginObject();
                    writer.name("Subject").value(subject);
                    writer.name("Vocabulary");
                    writeFlashCardsArray(writer, flashcards, progress);
                    writer.endObject();
                    writer.close();
                    progress.dismiss();

                    /* Show the message box */
                    if (!cancelation) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(EditSubjectActivity.this);
                                builder.setMessage("Export sucessful. Your file location at " + file.getPath())
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        });
                    }
                } catch (IOException ex) {

                }
                ;
            }
        };

        t.start();
    }

    //For json write array of flashcard
    public void writeFlashCardsArray(JsonWriter writer, List<FlashCard> flashcards, ProgressDialog progress) throws IOException {
        writer.beginArray();
        int i = 0;
        while (i < flashcards.size() && !cancelation) {
            writeFlashCash(writer, flashcards.get(i));
            progress.incrementProgressBy(1);
            i += 1;
        }

        writer.endArray();
    }

    //for json write single flashcard
    public void writeFlashCash(JsonWriter writer, FlashCard flashcard) throws IOException {
        writer.beginObject();
        writer.name("word").value(flashcard.getContent());
        writer.name("meaning").value(flashcard.getAnswer());
        writer.endObject();
    }

}
