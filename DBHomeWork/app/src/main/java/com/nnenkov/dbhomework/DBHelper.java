package com.nnenkov.dbhomework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by nik on 30.09.16.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DBHomeWork";

    public static final int DATABASE_VERSION = 2;

    private static final String ITEMS_TABLE = "Items";

    public final static String ITEM_ID = "_id"; // id value for employee
    public final static String ITEM_NAME = "name";  // name of employee

    private static final String DATABASE_CREATE = "CREATE TABLE " + ITEMS_TABLE + " ( " +
            ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, " +
            ITEM_NAME + " TEXT);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Do nothing
    }


    public void InsertItem(Item item){
        SQLiteDatabase sqlDB = getWritableDatabase();
        ContentValues cv = new ContentValues();

        //cv.put(ITEM_ID,item.getId());
        cv.put(ITEM_NAME,item.getItemName());

        sqlDB.insert(ITEMS_TABLE,null,cv);

        sqlDB.close();

    }

    public ArrayList<Item> getAllItems(){
        ArrayList<Item> itemsList = null;
        SQLiteDatabase sqlDB = getReadableDatabase();

        Cursor cursor = sqlDB.query(ITEMS_TABLE,new String[] {ITEM_ID, ITEM_NAME},null,null,null,null,ITEM_ID);
        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            itemsList = new ArrayList<>();
            do {
                itemsList.add(new Item(cursor.getInt(cursor.getColumnIndex(ITEM_ID)),cursor.getString(cursor.getColumnIndex(ITEM_NAME))));
            } while (cursor.moveToNext());

        }

        cursor.close();
        sqlDB.close();
        return itemsList;
    }




}
