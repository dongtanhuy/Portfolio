package group20.flashcards.flashcards;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import group20.flashcards.flashcards.CardStructure.FlashCard;
import group20.flashcards.flashcards.CardStructure.Subject;
import group20.flashcards.flashcards.Database.DatabaseHandler;

public class ImportActivity extends AppCompatActivity {
    private ListView myListview;
    private File root;
    private File currentFile;
    private ArrayList<File> files;
    private ArrayList<String> fileName;
    private ArrayAdapter<String> adapter;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        root= new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        currentFile=root;
        files= new ArrayList<>();
        fileName= new ArrayList<>();
        getFileFromPath(root);
        adapter = new ArrayAdapter<>(this,R.layout.activity_import,R.id.path,fileName);
        myListview = (ListView) findViewById(R.id.list);
        myListview.setAdapter(adapter);
        myListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File f = files.get(position);
                currentFile = f;
                if (currentFile.getName().endsWith(".json")) {
                    //read file json

                    String s = currentFile.getName();
                    try {
                        File yourFile = new File(f.getParentFile(), s);
                        FileInputStream stream = new FileInputStream(yourFile);
                        String jsonStr = null;
                        try {
                            FileChannel fc = stream.getChannel();
                            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                            jsonStr = Charset.defaultCharset().decode(bb).toString();
                        }
                        finally {
                            stream.close();
                        }
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        String subjectjson =  jsonObj.getString("Subject");
                        //TODO: add subject

                        final DatabaseHandler db = new DatabaseHandler(ImportActivity.this);
                        //Subject subject = new Subject(subjectjson, null);
                        boolean success1 = db.createSubject(new Subject(subjectjson, null));
                        if (success1){
                            finish();
                            startActivity(getIntent());
                        } else {
                            Toast.makeText(ImportActivity.this, "ERROR CREATING SUBJECT", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        Subject subject = db.getSubjectByTitle(subjectjson);
                        JSONArray vocabulary  = jsonObj.getJSONArray("Vocabulary");
                        for (int i = 0; i < vocabulary.length(); i++) {
                            JSONObject c = vocabulary.getJSONObject(i);
                            String word = c.getString("word");
                            String meaning = c.getString("meaning");
                            //TODO: add flashcard
                            FlashCard newCard = new FlashCard(subject, word, word, meaning);
                            boolean success = db.createFlashCard(newCard);
                            Intent sendBack = new Intent();
                            sendBack.putExtra("ADD_CARD_STATUS", success);
                            setResult(RESULT_OK, sendBack);
                            finish();
                        }
                        Toast.makeText(ImportActivity.this,"Import Scessfull!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ImportActivity.this, MainActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(ImportActivity.this,"File Json fail!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                } else {
                    getFileFromPath(f);
                    adapter.notifyDataSetChanged();
                }
                if (f != null) {
                   // f.delete();
                }
            }
        });
    }
    public void onBackPressed() {
        if(!currentFile.equals(root)){
            File parent= currentFile.getParentFile();
            getFileFromPath(parent);
            adapter.notifyDataSetChanged();
            currentFile=parent;
            parent.delete();
        }else {
            super.onBackPressed();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_import, menu);
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
    private ArrayList<File> getFileFromPath(File dir){
        File listFiles[]= dir.listFiles();
        if(listFiles!=null && listFiles.length>0){
            files.clear();
            fileName.clear();
            for (File f:listFiles){
                if (f.isDirectory()){
                    files.add(f);
                    fileName.add(f.getName());
                }else{
                    String name= f.getName();
                    if (name.endsWith(".json")){
                        files.add(f);
                        fileName.add(f.getName());
                    }
                }
            }
        }else{
            AlertDialog.Builder alertBuider2= new AlertDialog.Builder(context);
            alertBuider2.setTitle("Alert!");
            alertBuider2.setMessage("Empty folder!");
            alertBuider2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert2 = alertBuider2.create();
            alert2.show();
        }
        return files;
    }
}
