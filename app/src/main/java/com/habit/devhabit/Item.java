package com.habit.devhabit;

import java.util.HashMap;

/**
 * Created by qmo-i7 on 2016/3/19.
 */
public class Item implements java.io.Serializable {
    public static final String TABLE_NAME = "habit_record";
    private long mEntryId;
    private String mTitle;
    private String mDescription;
    private String mReason1;
    private String mReason2;
    private String mReason3;

    private String mStartDate;
    private String mSchedule;
    private HashMap mHash;

    public Item() {
        mTitle = "";
        mDescription = "";
        mHash = new HashMap();
    }

    public Item(String title, String desc, String reason1, String reason2, String reason3, String startDate, String schedule) {
        mTitle = title;
        mDescription = desc;
        mReason1 = reason1;
        mReason2 = reason2;
        mReason3 = reason3;
        mStartDate = startDate;
        mSchedule = schedule;
        mHash = new HashMap();
    }

    public long getId() {
        return mEntryId;
    }

    public void setId(long id) {
        mEntryId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String desc) {
        mDescription = desc;
    }

    public String getReason1() {
        return mReason1;
    }

    public void setReason1(String reason) {
        mReason1 = reason;
    }

    public String getReason2() {
        return mReason2;
    }

    public void setReason2(String reason) {
        mReason2 = reason;
    }

    public String getReason3() {
        return mReason3;
    }

    public void setReason3(String reason) {
        mReason3 = reason;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String date) {
        mStartDate = date;
    }

    public String getSchedule() {
        return mSchedule;
    }

    public void setSchedule(String schedule) {
        mSchedule = schedule;
    }

    public HashMap getHash() {
        return mHash;
    }

    public void setHash(HashMap hash) {
        mHash.putAll(hash);
    }
}
