package com.example.piero.postnote1;

import android.content.Context;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Adriano on 16/05/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABSE_NAME = "PostPiero.db";
    public static final String TABLE_NAME = "post_table";
    public static final String COL_ID = "ID";
    public static final String COL_TITOLO = "TITOLO";
    public static final String COL_TESTO = "TESTO";
    public static final String COL_DATE = "DATE";
    public static final String COL_AUDIO = "AUDIO";
    public static final String COL_IMMAGINE = "IMMAGINE";
    public static final String COL_FLAGGED = "FLAGGED";


    public DatabaseHelper(Context context) {
        super(context, DATABSE_NAME, null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITOLO TEXT, " +
                "TESTO TEXT, DATE TEXT, AUDIO INTEGER, IMMAGINE INTEGER, FLAGGED INTEGER)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(query);
        onCreate(db);
    }


    public boolean insertData(String titolo, String testo, String date, int audio, int immagine, int flagged) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();

        contentValue.put(COL_TITOLO, titolo);
        contentValue.put(COL_TESTO, testo);
        contentValue.put(COL_DATE, date);
        contentValue.put(COL_AUDIO, audio);
        contentValue.put(COL_IMMAGINE, immagine);
        contentValue.put(COL_FLAGGED, flagged);

        long result = db.insert(TABLE_NAME, null, contentValue);

        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }



    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor res = db.rawQuery(query, null);

        return res;
    }

    public boolean updateFlag(String id, int flagged){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(COL_FLAGGED, flagged);

        int isUpdate = db.update(TABLE_NAME, contentValue, "id = ?", new String[] {id});
        if(isUpdate != 1){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateData(String id, String titolo, String testo, int audio, int immagine, int flagged){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();

        contentValue.put(COL_ID, id);
        contentValue.put(COL_TITOLO, titolo);
        contentValue.put(COL_TESTO, testo);
        contentValue.put(COL_AUDIO, audio);
        contentValue.put(COL_IMMAGINE, immagine);
        contentValue.put(COL_FLAGGED, flagged);


        int isUpdate = db.update(TABLE_NAME, contentValue, "id = ?",new String[] { id });
        if(isUpdate != 1){
            return false;
        }else{
            return true;
        }
    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }
}
