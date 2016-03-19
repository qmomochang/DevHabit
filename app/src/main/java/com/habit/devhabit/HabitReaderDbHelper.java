package com.habit.devhabit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by qmo-i7 on 2016/3/19.
 */
public class HabitReaderDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "HabitReader.db";
    private static SQLiteDatabase mDatabase;


    public HabitReaderDbHelper(Context ctx, String name, CursorFactory factory, int version) {
        super(ctx, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public static SQLiteDatabase getDatabase(Context context) {
        if (mDatabase == null || !mDatabase.isOpen()) {
            mDatabase = new HabitReaderDbHelper(context, DATABASE_NAME,
                    null, DATABASE_VERSION).getWritableDatabase();
        }

        return mDatabase;
    }


    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ItemDAO.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        //db.execSQL("DROP TABLE IF EXISTS " + ItemDAO.TABLE_NAME);
        onCreate(db);
    }
}
