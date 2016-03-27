package com.habit.devhabit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by qmo-i7 on 2016/3/24.
 */
public class HabitStatisticView extends LinearLayout {
    Context mContext;
    Item mItem;
    ViewGroup mStatisticContainer;
    List<String> mDateList;
    List<Integer> mDataList;

    public HabitStatisticView(Context context) {
        super(context);
        mContext = context;
    }

    public HabitStatisticView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        mContext = context;
    }

    public HabitStatisticView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void updateView() {
        ItemDAO item = new ItemDAO(mContext);
        mItem = item.get(mItem.getId());
        setupView(mStatisticContainer, mItem);
    }
    public void setupView(ViewGroup vg, Item item) {
        mItem = item;
        mStatisticContainer = vg;
        prepareData(item);
        setupStatisticView(vg, item);
    }

    public Item getItem() {
        return mItem;
    }

    public void prepareData(Item item) {
        if (mDateList == null) {
            mDateList = new ArrayList<>();
        }
        mDateList.clear();
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.clear();

        Calendar c = Calendar.getInstance();
        int start_day_of_week = c.get(Calendar.DAY_OF_WEEK);
        int day_of_year = c.get(Calendar.DAY_OF_YEAR) - 29 + (7 - start_day_of_week);
        // return 30days
        c.set(Calendar.DAY_OF_YEAR, day_of_year);

        SimpleDateFormat df = new SimpleDateFormat("MM/dd");
        int doy = 0;
        for (int i = 0; i < 31; i++) {
            // setup date list
            mDateList.add(df.format(c.getTime()));

            // setup data list
            if (mItem.getHash().get(Integer.toString(day_of_year + i - 1)) == null) {
                doy = 0;
            } else {
                doy = (int) mItem.getHash().get(Integer.toString(day_of_year + i - 1));
            }
            mDataList.add(doy);

            // shift date
            c.set(Calendar.DAY_OF_YEAR, day_of_year + i);
        }

        CustomView v = (CustomView) findViewById(R.id.target_chart);
        v.setupList(mDateList, mDataList);

    }

    private void setupStatisticView(ViewGroup container, Item item) {
        // setup title
        ((TextView) findViewById(R.id.target_title)).setText(item.getTitle());
    }

    public void removeView() {
        ((ViewGroup)getParent()).removeView(this);
    }
}
