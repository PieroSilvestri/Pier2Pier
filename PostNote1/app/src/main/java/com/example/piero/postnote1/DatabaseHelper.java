package com.example.piero.postnote1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

/**
 * Created by Adriano on 16/05/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "Post.db";
    public static final String TABLE_NAME = "Post_table";
    public static final String COL_ID = "ID";
    public static final String COL_TITOLO = "TITOLO";
    public static final String COL_TESTO = "TESTO";
    public static final String COL_DATE = "DATE";


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS" + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITOLO TEXT, " +
                "TESTO TEXT, DATE TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXIST " + TABLE_NAME;
        db.execSQL(query);
        onCreate(db);
    }

    public boolean insertData(String titolo, String testo, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_TITOLO, titolo);
        contentValues.put(COL_TESTO, testo);
        contentValues.put(COL_DATE, date);

        long result = db.insert(TABLE_NAME, null, contentValues);

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
}
