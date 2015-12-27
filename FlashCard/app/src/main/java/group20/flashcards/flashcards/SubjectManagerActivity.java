package group20.flashcards.flashcards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import group20.flashcards.flashcards.Adapter.SubjectsAdapter;
import group20.flashcards.flashcards.CardStructure.Subject;
import group20.flashcards.flashcards.Database.DatabaseHandler;

public class SubjectManagerActivity extends AppCompatActivity {

    EditText newSubject;
    Button addSubject;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_manager);

        newSubject = (EditText) findViewById(R.id.new_subject_title);
        addSubject = (Button) findViewById(R.id.add_subject_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get list subjects for display

        final DatabaseHandler db = new DatabaseHandler(this);
        final ArrayList<Subject> subjects = db.getSubjects();
        final int SUBJECT_MANAGER_RESULT = 1;
        final ListAdapter list_adapter = new SubjectsAdapter(this, android.R.layout.simple_list_item_1, subjects);
        final ListView subjects_list = (ListView) findViewById(R.id.list_manage_subjects);
        subjects_list.setAdapter(list_adapter);
        subjects_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SubjectManagerActivity.this, EditSubjectActivity.class);
                intent.putExtra("SUBJECT", subjects.get(position).getTitle());

                startActivityForResult(intent, SUBJECT_MANAGER_RESULT);
            }
        });

        // Create Subject and Add it to database
        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectTitle = newSubject.getText().toString();
                if (subjectTitle.isEmpty()){
                    Toast.makeText(SubjectManagerActivity.this, "Please Enter Subject's Title", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if we already have it or not
                    boolean success = db.createSubject(new Subject(subjectTitle,null));
                    if (success){
                        finish();
                        startActivity(getIntent());
                    } else {
                        Toast.makeText(SubjectManagerActivity.this, "ERROR CREATING SUBJECT", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            boolean deleteStatus = data.getBooleanExtra("DELETE_STATUS", false);
            if (deleteStatus) {
                finish();
                startActivity(getIntent());
            }
        }
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
        } else if(id == android.R.id.home) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
