package group20.flashcards.flashcards.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import group20.flashcards.flashcards.CardStructure.FlashCard;
import group20.flashcards.flashcards.CardStructure.Subject;


public class DatabaseHandler extends SQLiteOpenHelper {
    //define database constants
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "flashcards.db";
    public static final String _ID = "id"; // we need to use _ID because ID is already used by the system.
    public static final String TITLE = "title";
    /**
     * SUBJECT DATABASE TABLE
     */
    public static final String SUBJECT_TABLE = "subject";

    /**
     * FLASHCARDS DATABASE TABLE
     */
    public static final String FLASHCARD_TABLE = "flashcard";
    public static final String FLASHCARD_CONTENT = "content";
    public static final String FLASHCARD_ANSWER = "answer";
    public static final String SUBJECT_ID = "subject_id";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Will only Run if the Database does not exist
        final String SQL_CREATE_SUBJECT_TABLE = "CREATE TABLE " + SUBJECT_TABLE + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TITLE + " TEXT NOT NULL UNIQUE" +
                ");";

        final String SQL_CREATE_FLASHCARD_TABLE = "CREATE TABLE " + FLASHCARD_TABLE + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " TEXT NOT NULL, " +
                FLASHCARD_CONTENT + " TEXT NOT NULL , " +
                FLASHCARD_ANSWER + " TEXT NOT NULL, " +
                SUBJECT_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + SUBJECT_ID + ") REFERENCES " + SUBJECT_TABLE + " (" + _ID + ")" +
                ");";

        // Create tables if not exist
        db.execSQL(SQL_CREATE_SUBJECT_TABLE);
        db.execSQL(SQL_CREATE_FLASHCARD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + SUBJECT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FLASHCARD_TABLE);
        // Create tables again
        onCreate(db);
    }

    /**
     * Closes the Database Connection.
     */
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * CRUD OPERATIONS
     */

    /**
     * Clear DB
     */
    public void clearDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + SUBJECT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FLASHCARD_TABLE);
        onCreate(db);
    }

    /**
     * Create and Add subject to database
     */
    public boolean createSubject(Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase(); // We get an WritableDatabase so we can insert data
        ContentValues values = new ContentValues(); // Create a new content values with Key Value Pairs
        values.put(TITLE, subject.getTitle()); // Insert the TITLE for the Subject
        try {
            db.insert(SUBJECT_TABLE, null, values);
        } catch (Exception e) {
            return false;
        }
        closeDB();
        return true;
    }

    /**
     * Create and Add flash card to table
     */
    public boolean createFlashCard(FlashCard flashCard) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // add value to record
        values.put(TITLE, flashCard.getTitle());
        values.put(FLASHCARD_CONTENT, flashCard.getContent());
        values.put(FLASHCARD_ANSWER, flashCard.getAnswer());
        values.put(SUBJECT_ID, flashCard.getSubject().getId());
        try {
            db.insert(FLASHCARD_TABLE, null, values);
        } catch (Exception e){
            return false;
        }
        closeDB();
        return true;
    }

    /**
     * Update Subject
     */
    public long updateSubject(Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(_ID, subject.getId());
        values.put(TITLE, subject.getTitle());
        return db.update(SUBJECT_TABLE, values, _ID + "= ?", new String[]{String.valueOf(subject.getId())});
    }

    /**
     * Update FlashCard
     */
    public boolean updateFlashcard(FlashCard newCard) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(_ID, newCard.getId());
        values.put(TITLE, newCard.getTitle());
        values.put(FLASHCARD_CONTENT, newCard.getContent());
        values.put(FLASHCARD_ANSWER, newCard.getAnswer());
        values.put(SUBJECT_ID, newCard.getSubject().getId());
        try {
            db.update(FLASHCARD_TABLE, values, _ID + "=" + newCard.getId(), null);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Delete a FlashCard
     */
    public boolean deleteFlashCard(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(FLASHCARD_TABLE, _ID + " = " + id, null);
        } catch (Exception e){
            return false;
        }
        db.close();
        return true;
    }
    /**
     * Delete a Subject
     */
    public boolean deleteSubject(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(SUBJECT_TABLE, _ID + " = " + id, null);
        } catch (Exception e){
            return false;
        }
        db.close();
        return true;
    }
    /**
     * Get 1 Subject from database by id
     */
    public Subject getSubjectByID(long id) {
        SQLiteDatabase db = this.getReadableDatabase(); // We get a readable database
        String selectQuery = "SELECT * FROM " + SUBJECT_TABLE + " WHERE " + _ID + " = " + id + ";";

        Cursor cursor = db.rawQuery(selectQuery, null);

        //Cursor cursor1 = db.query(SUBJECT_TABLE,new String[] {_ID, TITLE}, _ID +"=?",new String[] { String.valueOf(id)},null,null,null,null);

        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            // Log the error for debug
            Log.i("ERROR", "Cursor is null!!");
            return null;
        }

        Subject subject = new Subject();
        subject.setId(cursor.getLong(cursor.getColumnIndex(_ID)));
        subject.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        // return the subject
        this.closeDB();
        cursor.close();
        return subject;
    }

    /**
     * Get 1 Subject from database by title
     */
    public Subject getSubjectByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase(); // We get a readable database
//        String selectQuery = "SELECT * FROM " + SUBJECT_TABLE + " WHERE " + TITLE + " = " + title + ";";

        Cursor cursor = db.query(SUBJECT_TABLE, new String[]{_ID, TITLE}, TITLE + "=?", new String[]{title}, null, null, null, null);

        //Cursor cursor1 = db.query(SUBJECT_TABLE,new String[] {_ID, TITLE}, _ID +"=?",new String[] { String.valueOf(id)},null,null,null,null);

        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            // Log the error for debug
            Log.i("ERROR", "Cursor is null!!");
            return null;
        }

        Subject subject = new Subject();
        subject.setId(cursor.getLong(cursor.getColumnIndex(_ID)));
        subject.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        // return the subject
        this.closeDB();
        cursor.close();
        return subject;
    }

    /**
     * Get all subjects from database
     */
    public ArrayList<Subject> getSubjects() {
        ArrayList<Subject> subjects = new ArrayList<Subject>();
        SQLiteDatabase db = this.getReadableDatabase(); // Get a readable database
        String selectQuery = "SELECT * FROM " + SUBJECT_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToNext()) {
            do {
                Subject subject = new Subject();
                subject.setId(cursor.getLong(cursor.getColumnIndex(_ID)));
                subject.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                subjects.add(subject);
            } while (cursor.moveToNext());
        }
        // return the subject list
        this.closeDB();
        cursor.close();
        return subjects;
    }

    /**
     * Get FlashCard by id
     */
    public FlashCard getFlashCardByID(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + FLASHCARD_TABLE + " WHERE " + _ID + " = " + id + ";";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            Log.i("ERROR", "Cursor is null!!");
            return null;
        }

        FlashCard flashCard = new FlashCard();
        flashCard.setId(cursor.getLong(cursor.getColumnIndex(_ID)));
        flashCard.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        flashCard.setContent(cursor.getString(cursor.getColumnIndex(FLASHCARD_CONTENT)));
        flashCard.setContent(cursor.getString(cursor.getColumnIndex(FLASHCARD_ANSWER)));

        Subject subject = getSubjectByID(cursor.getLong(cursor.getColumnIndex(SUBJECT_ID)));

        if (subject != null) flashCard.setSubject(subject);
        // return the result
        this.closeDB();
        cursor.close();
        return flashCard;
    }

    /**
     * Get all flash cards from a subject
     */
    public ArrayList<FlashCard> getFlashCardsBySubjectID(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<FlashCard> flashCards = new ArrayList<FlashCard>();
        String selectQuery = "SELECT * FROM " + FLASHCARD_TABLE + " WHERE " + SUBJECT_ID + " = " + id + ";";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                FlashCard flashCard = new FlashCard();
                flashCard.setId(cursor.getLong(cursor.getColumnIndex(_ID)));
                flashCard.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                flashCard.setContent(cursor.getString(cursor.getColumnIndex(FLASHCARD_CONTENT)));
                flashCard.setAnswer(cursor.getString(cursor.getColumnIndex(FLASHCARD_ANSWER)));
                flashCard.setSubject(getSubjectByID(id));
                flashCards.add(flashCard);
            } while (cursor.moveToNext());
        }
        // return the result
        this.closeDB();
        cursor.close();
        return flashCards;
    }
}
