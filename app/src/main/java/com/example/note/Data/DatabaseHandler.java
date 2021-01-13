package com.example.note.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.note.Model.Note;
import com.example.note.R;
import com.example.note.Util.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private int sum;
    public DatabaseHandler(Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        sum = 0;
        String CREATE_CONTACT_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "("
                + Util.KEY_ID + " INTEGER PRIMARY KEY," + Util.KEY_NAME + " TEXT,"
                + Util.AMOUNT + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACT_TABLE); //creating our table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = String.valueOf(R.string.db_drop);
        db.execSQL(DROP_TABLE, new String[]{Util.DATABASE_NAME});

        //Create a table again
        onCreate(db);
    }
    public void addNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, note.getContent());
        values.put(Util.AMOUNT, note.getAmount());

        db.insert(Util.TABLE_NAME, null, values);

        Log.d("DBHandler", "addNote");
        db.close();
    }

    public List<Note> getAllNotes(){
        List <Note> noteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //Select all notes
        String selectAll = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);

        if(cursor.moveToFirst()){
            do{
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setContent(cursor.getString(1));
                note.setAmount(Integer.parseInt(cursor.getString(2)));
                sum += Integer.parseInt(cursor.getString(2));

                noteList.add(note);
            }while (cursor.moveToNext());
        }else{
            Log.d("DBHandler getAll", "it is empty");
        }
        return noteList;
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from "+ Util.TABLE_NAME);
    }

    public void delete(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("deleteAll", ""+note.getId());
        db.delete(Util.TABLE_NAME, Util.KEY_ID + "=?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public int update(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, note.getContent());
        values.put(Util.AMOUNT, note.getAmount());

        //update the row
        //update(tablename, values, where id = 43)
        return db.update(Util.TABLE_NAME, values, Util.KEY_ID + "=?",
                new String[]{String.valueOf(note.getId())});
    }

    public int getSum(){
        return sum;
    }

}
