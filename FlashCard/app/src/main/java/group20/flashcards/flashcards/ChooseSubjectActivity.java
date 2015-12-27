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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import group20.flashcards.flashcards.Adapter.SubjectsAdapter;
import group20.flashcards.flashcards.CardStructure.Subject;
import group20.flashcards.flashcards.Database.DatabaseHandler;

public class ChooseSubjectActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_subject);

        toolbar = (Toolbar) findViewById(R.id.toolbar2);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // GET SUBJECTS LIST FROM DATABASE
        DatabaseHandler db = new DatabaseHandler(this);
        final ArrayList<Subject> subjects = db.getSubjects();


        //final ListAdapter list_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subjects);
        final ListAdapter list_adapter = new SubjectsAdapter(this, android.R.layout.simple_list_item_1, subjects);
        ListView subjects_list = (ListView) findViewById(R.id.list_subjects);
        subjects_list.setAdapter(list_adapter);
        subjects_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ChooseSubjectActivity.this, LearnActivity.class);
                intent.putExtra("SUBJECT",subjects.get(position).getTitle());
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_subject, menu);
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
        } else if (id == android.R.id.home) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
