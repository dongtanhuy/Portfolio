package group20.flashcards.flashcards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import group20.flashcards.flashcards.CardStructure.FlashCard;
import group20.flashcards.flashcards.CardStructure.Subject;
import group20.flashcards.flashcards.Database.DatabaseHandler;

public class EditCardActivity extends AppCompatActivity {


    Button btnSave;
    Button btnDelete;
    TextView subjectTitle;
    EditText inputTitle;
    EditText inputContent;
    EditText inputAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        // Get elements from activity
        btnSave = (Button) findViewById(R.id.btn_save_card);
        btnDelete = (Button) findViewById(R.id.btn_delete_card);
        inputTitle = (EditText) findViewById(R.id.input_title);
        inputContent = (EditText) findViewById(R.id.input_content);
        inputAnswer = (EditText) findViewById(R.id.input_answer);
        subjectTitle = (TextView) findViewById(R.id.subject_title);

        //Get intent
        final Intent intent = getIntent();
        String option = intent.getStringExtra("OPTION");
        final String subject_title = intent.getStringExtra("EDIT_SUBJECT");
        subjectTitle.setText(subject_title);

        // DB handler
        final DatabaseHandler db = new DatabaseHandler(this);
        // Get subject from the database
        final Subject subject = db.getSubjectByTitle(subject_title);

        if (option.equals("ADD")) {
            // ADD NEW CARD TO THE DATABASE
            btnDelete.setVisibility(View.INVISIBLE);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = inputTitle.getText().toString();
                    String content = inputContent.getText().toString();
                    String answer = inputAnswer.getText().toString();
                    if (title.isEmpty() || content.isEmpty() || answer.isEmpty()) {
                        Toast.makeText(EditCardActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    } else {
                        FlashCard newCard = new FlashCard(subject, title, content, answer);
                        boolean success = db.createFlashCard(newCard);
                        Intent sendBack = new Intent();
                        sendBack.putExtra("ADD_CARD_STATUS", success);
                        setResult(RESULT_OK, sendBack);
                        finish();
                    }
                }
            });
        } else if (option.equals("UPDATE")) {
            inputTitle.setText(intent.getStringExtra("TITLE"));
            inputContent.setText(intent.getStringExtra("CONTENT"));
            inputAnswer.setText(intent.getStringExtra("ANSWER"));
            // UPDATE CARD IN DATABASE
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = inputTitle.getText().toString();
                    String content = inputContent.getText().toString();
                    String answer = inputAnswer.getText().toString();
                    if (title.isEmpty() || content.isEmpty() || answer.isEmpty()) {
                        Toast.makeText(EditCardActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    } else {
                        FlashCard newCard = new FlashCard(subject, title, content, answer);
                        newCard.setId(intent.getLongExtra("FLASHCARD_ID", -1));
                        boolean success = db.updateFlashcard(newCard);
                        Intent sendBack = new Intent();
                        sendBack.putExtra("UPDATE_CARD_STATUS", success);
                        setResult(RESULT_OK, sendBack);
                        finish();
                    }
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long flashcardId = intent.getLongExtra("FLASHCARD_ID", -1);
                    boolean success = db.deleteFlashCard(flashcardId);
                    Intent sendBack = new Intent();
                    sendBack.putExtra("DELETE_CARD_STATUS", success);
                    setResult(RESULT_OK, sendBack);
                    finish();
                }
            });
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
        getMenuInflater().inflate(R.menu.menu_edit_card, menu);
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
