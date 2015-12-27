package group20.flashcards.flashcards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rey.material.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button quit;
    Button learn;
    Button subjects;
    Button importjson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        quit = (Button) findViewById(R.id.menu_quit);
        learn = (Button) findViewById(R.id.menu_learn);
        subjects = (Button) findViewById(R.id.menu_subject);
        importjson = (Button) findViewById(R.id.menu_import);

          //Clearing the DB
//        DatabaseHandler db = new DatabaseHandler(this);
//        db.clearDB();

        // Exit application when user click EXIT
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(1);
            }
        });

        // Launch ChooseSubjectActivity when click LEARN and set option to learn
        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChooseSubjectActivity.class);
                startActivity(intent);
            }
        });

        // Launch ChooseSubjectActivity when click SUBJECTS and set option to subjects
        subjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SubjectManagerActivity.class);
                startActivity(intent);
            }
        });

        importjson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent importIntent = new Intent(MainActivity.this, ImportActivity.class);
                startActivity(importIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
