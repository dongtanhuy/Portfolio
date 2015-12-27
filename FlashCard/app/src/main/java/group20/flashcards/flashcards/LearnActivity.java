package group20.flashcards.flashcards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import group20.flashcards.flashcards.Database.DatabaseHandler;
import group20.flashcards.flashcards.CardStructure.FlashCard;
import group20.flashcards.flashcards.CardStructure.Subject;

public class LearnActivity extends AppCompatActivity {

    Button btnPrevious;
    //Button btnAnswer;
    Button btnNext;
    TextView tvSubject;
    TextView tvContent;
    TextView tvAnswer;
    RelativeLayout relativeLayout;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        // GET buttons and textviews from activity
        btnPrevious = (Button) findViewById(R.id.btn_previous);
        //btnAnswer = (Button) findViewById(R.id.btn_answer);
        btnNext = (Button) findViewById(R.id.btn_next);
        tvSubject = (TextView) findViewById(R.id.flashcard_subject);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvAnswer = (TextView) findViewById(R.id.tv_answer);
        relativeLayout = (RelativeLayout) findViewById(R.id.relatelive_layout1);

        //GET the subject send from the ChooseSubjectActivity
        Intent intent = getIntent();

        String subjectTitle = intent.getStringExtra("SUBJECT");
        DatabaseHandler db = new DatabaseHandler(this);
        // Get subject from the database
        Subject subject = db.getSubjectByTitle(subjectTitle);
        // Get List of card for the subject

        final ArrayList<FlashCard> flashcards = db.getFlashCardsBySubjectID(subject.getId());

        //Set the subject title
        tvSubject.setText(subject.getTitle());

        // check the subjects length
        if (flashcards.isEmpty()) {
            tvContent.setText("This Subject is empty");
            tvContent.setTextSize(12);
            btnNext.setVisibility(View.INVISIBLE);
            btnPrevious.setVisibility(View.INVISIBLE);
            //btnAnswer.setVisibility(View.INVISIBLE);
        } else {
            Toast.makeText(LearnActivity.this, "Press and hold screen to show the meaning.", Toast.LENGTH_LONG).show();
            //set btnprevious to invisible
            btnPrevious.setVisibility(View.INVISIBLE);

            tvContent.setText(flashcards.get(position).getContent());
            tvAnswer.setText(flashcards.get(position).getAnswer());
            // Move to the previous card, do nothing if that is the 1st card
            btnPrevious.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (position == flashcards.size() - 1) {
                        btnNext.setVisibility(View.VISIBLE);
                    }
                    position = (position > 0) ? position - 1 : position;
                    tvAnswer.setVisibility(View.INVISIBLE);
                    tvContent.setVisibility(View.VISIBLE);
                    tvContent.setText(flashcards.get(position).getContent());
                    tvAnswer.setText(flashcards.get(position).getAnswer());
                    if (position == 0) {
                        btnPrevious.setVisibility(View.INVISIBLE);
                    }
                }
            });
            //Move to the next card, do nothing if that is the last card
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 0) {
                        btnPrevious.setVisibility(View.VISIBLE);
                    }
                    position = (position + 1 < flashcards.size()) ? position + 1 : position;
                    tvAnswer.setVisibility(View.INVISIBLE);
                    tvContent.setVisibility(View.VISIBLE);
                    tvContent.setText(flashcards.get(position).getContent());
                    tvAnswer.setText(flashcards.get(position).getAnswer());
                    if (position == flashcards.size() - 1) {
                        btnNext.setVisibility(View.INVISIBLE);
                    }

                }
            });
            // Set visible and invisible to tvAnswer and tvContent
//            btnAnswer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (tvAnswer.getVisibility() == View.VISIBLE) {
//                        tvAnswer.setVisibility(View.INVISIBLE);
//                        tvContent.setVisibility(View.VISIBLE);
//                    } else if (tvAnswer.getVisibility() == View.INVISIBLE) {
//                        tvAnswer.setVisibility(View.VISIBLE);
//                        tvContent.setVisibility(View.INVISIBLE);
//                    }
//                }
//            });

            relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (tvAnswer.getVisibility() == View.VISIBLE) {
                        tvAnswer.setVisibility(View.INVISIBLE);
                        tvContent.setVisibility(View.VISIBLE);
                        return true;
                    } else if (tvAnswer.getVisibility() == View.INVISIBLE) {
                        tvAnswer.setVisibility(View.VISIBLE);
                        tvContent.setVisibility(View.INVISIBLE);
                        return true;
                    }
                    return false;
                }
            });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_learn, menu);
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
