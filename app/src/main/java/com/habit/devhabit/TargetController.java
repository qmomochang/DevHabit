package com.habit.devhabit;

import android.app.Activity;
import android.content.Context;

import android.view.View;
import android.view.ViewGroup;


import java.util.List;

/**
 * Created by michael on 2016/3/18.
 */
public class TargetController {
    Context mContext;
    ViewGroup mHabitContainer;

    private ItemDAO mItemDAO;
    private List<Item> mItems;

    public TargetController(Context ctx) {
        mContext = ctx;
        initUi();
    }

    private void initUi() {
        prepareHabits();
    }

    private void prepareHabits() {
        mItemDAO = new ItemDAO(mContext);
        if (mItemDAO.getCount() == 0) {
            mItemDAO.sample();
        }
        mItems = mItemDAO.getAll();
        mHabitContainer = (ViewGroup) ((Activity) mContext).findViewById(R.id.habit_container);
        int i = 0;
        for (Item item : mItems) {
            ((Activity) mContext).getLayoutInflater().inflate(R.layout.target_layout, mHabitContainer);
            if (mHabitContainer != null) {
                View v = mHabitContainer.getChildAt(i++);
                if ( v instanceof HabitView) {
                    ((HabitView) v).setupView((ViewGroup) v, item);
                }
            }
        }
    }

    public void addHabit() {
        mHabitContainer = (ViewGroup) ((Activity) mContext).findViewById(R.id.habit_container);

        Item item = mItemDAO.getLast();

        ((Activity) mContext).getLayoutInflater().inflate(R.layout.target_layout, mHabitContainer);
        if (mHabitContainer != null) {
            int i = mHabitContainer.getChildCount();
            View v = mHabitContainer.getChildAt(i-1);
            if (v instanceof HabitView) {
                ((HabitView) v).setupView((ViewGroup) v, item);
            }
        }

    }
}
