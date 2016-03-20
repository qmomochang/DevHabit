package com.habit.devhabit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * Created by qmo-i7 on 2016/3/19.
 */
public class ItemDAO {

    public static final String KEY_ID = "entry_id";

    public static final String TABLE_NAME = "habit_record";
    public static final String COLUMN_NAME_HABIT_ID = "entry_id";
    public static final String COLUMN_HABIT_TITLE = "name";
    public static final String COLUMN_HABIT_DESC = "description";
    public static final String COLUMN_HABIT_REASON1 = "reason1";
    public static final String COLUMN_HABIT_REASON2 = "reason2";
    public static final String COLUMN_HABIT_REASON3 = "reason3";
    public static final String COLUMN_HABIT_START_DATE = "start_date";
    public static final String COLUMN_HABIT_SCHEDULE = "schedule";
    public static final String COLUMN_HABIT_DATE_STATUS = "date_status";

    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";
    public static final String BLOB = "BLOB";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_HABIT_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_HABIT_TITLE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_HABIT_DESC + TEXT_TYPE + COMMA_SEP +
                    COLUMN_HABIT_REASON1 + TEXT_TYPE + COMMA_SEP +
                    COLUMN_HABIT_REASON2 + TEXT_TYPE + COMMA_SEP +
                    COLUMN_HABIT_REASON3 + TEXT_TYPE + COMMA_SEP +
                    COLUMN_HABIT_START_DATE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_HABIT_SCHEDULE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_HABIT_DATE_STATUS + BLOB +
                    " )";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public ItemDAO(Context context) {
        db = HabitReaderDbHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    public Item insert(Item item) {
        ContentValues cv = new ContentValues();
        try {
            cv.put(COLUMN_HABIT_TITLE, item.getTitle());
            cv.put(COLUMN_HABIT_DESC, item.getDescription());
            cv.put(COLUMN_HABIT_REASON1, item.getReason1());
            cv.put(COLUMN_HABIT_REASON2, item.getReason2());
            cv.put(COLUMN_HABIT_REASON3, item.getReason3());
            cv.put(COLUMN_HABIT_START_DATE, item.getStartDate());
            cv.put(COLUMN_HABIT_SCHEDULE, item.getSchedule());
            cv.put(COLUMN_HABIT_SCHEDULE, item.getSchedule());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(item.getHash());
            byte[] bytes = baos.toByteArray();
            out.close();
            baos.close();
            cv.put(COLUMN_HABIT_DATE_STATUS, bytes);
        } catch (Exception e) {
            android.util.Log.v("ItemDAO", "e = " + e);
        }
        long id = db.insert(TABLE_NAME, null, cv);
        item.setId(id);
        return item;
    }

    public boolean update(Item item) {
        ContentValues cv = new ContentValues();

        try {
            cv.put(COLUMN_HABIT_TITLE, item.getTitle());
            cv.put(COLUMN_HABIT_DESC, item.getDescription());
            cv.put(COLUMN_HABIT_REASON1, item.getReason1());
            cv.put(COLUMN_HABIT_REASON2, item.getReason2());
            cv.put(COLUMN_HABIT_REASON3, item.getReason3());
            cv.put(COLUMN_HABIT_START_DATE, item.getStartDate());
            cv.put(COLUMN_HABIT_SCHEDULE, item.getSchedule());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(item.getHash());
            byte[] bytes = baos.toByteArray();
            out.close();
            baos.close();
            cv.put(COLUMN_HABIT_DATE_STATUS, bytes);
        } catch (Exception e) {
            android.util.Log.v("ItemDAO", "e = " + e);
        }
        String where = KEY_ID + "=" + item.getId();
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    public boolean delete(long id) {
        String where = KEY_ID + "=" + id;
        return db.delete(TABLE_NAME, where, null) > 0;
    }

    public List<Item> getAll() {
        List<Item> result = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    public Item get(long id) {
        // 準備回傳結果用的物件
        Item item = null;
        // 使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getRecord(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }

    public Item getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        Item result = new Item();

        result.setId(cursor.getLong(0));
        result.setTitle(cursor.getString(1));
        result.setDescription(cursor.getString(2));
        result.setReason1(cursor.getString(3));
        result.setReason2(cursor.getString(4));
        result.setReason3(cursor.getString(5));
        result.setStartDate(cursor.getString(6));
        result.setSchedule(cursor.getString(7));

        // 回傳結果
        return result;
    }

    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }

    public Item getLast() {
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        cursor.moveToLast();
        Item item = getRecord(cursor);

        cursor.close();
        return item;
    }

    public void sample() {
        Item item = new Item("ReadEng", "ReadEng", "to improve my english", "", "", "2016-04-01", "");
        Item item2 = new Item("Dance", "2", "r2", "", "", "2016-04-01", "everyday");
        Item item3 = new Item("Sing", "3", "r3", "", "", "2016-04-01", "every 2 day");
        Item item4 = new Item("Workout", "4", "r4", "", "", "2016-04-01", "");

        insert(item);
        insert(item2);
        insert(item3);
        insert(item4);
    }
}
