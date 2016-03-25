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
    ViewGroup mHabitStatisticContainer;

    private ItemDAO mItemDAO;
    private List<Item> mItems;

    public TargetController(Context ctx) {
        mContext = ctx;
    }

    public void initUi(ViewGroup container) {
        prepareHabits(container);
    }

    private void prepareHabits(ViewGroup container) {
        mItemDAO = new ItemDAO(mContext);
        if (mItemDAO.getCount() == 0) {
            mItemDAO.sample();
        }
        mItems = mItemDAO.getAll();
        mHabitContainer = (ViewGroup) container.findViewById(R.id.habit_container);
        int i = 0;
        for (Item item : mItems) {
            ((Activity) mContext).getLayoutInflater().inflate(R.layout.target_layout, mHabitContainer);
            if (mHabitContainer != null) {
                View v = mHabitContainer.getChildAt(i++);
                if ( v instanceof HabitView) {
                    ((HabitView) v).setupView((ViewGroup) v, item);
                    final Item finalItem = item;
                    ((HabitView) v).setOnDataChangedListener(new HabitView.OnDataChangedListener() {
                        @Override
                        public void onDataChanged() {
                            if (mHabitStatisticContainer != null) {
                                View child;
                                for(int i=0;i<mHabitStatisticContainer.getChildCount();i++) {
                                    child = mHabitStatisticContainer.getChildAt(i);
                                    if (child != null && child instanceof HabitStatisticView && ((HabitStatisticView)child).getItem() != null) {
                                        if (finalItem.getTitle().equals( ((HabitStatisticView)child).getItem().getTitle())) {
                                            ((HabitStatisticView) child).updateView();
                                        }
                                    }

                                }
                            }
                        }
                    });
                }
            }
        }
    }

    public void addHabit(ViewGroup container) {
        mHabitContainer = (ViewGroup) container.findViewById(R.id.habit_container);

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

    public void initStatisticUi(ViewGroup container) {
        prepareHabitsStatistics(container);
    }

    private void prepareHabitsStatistics(ViewGroup container) {
        mItemDAO = new ItemDAO(mContext);
        if (mItemDAO.getCount() == 0) {
            mItemDAO.sample();
        }
        mItems = mItemDAO.getAll();
        mHabitStatisticContainer = (ViewGroup) container.findViewById(R.id.habit_chart_container);
        int i = 0;
        for (Item item : mItems) {
            ((Activity) mContext).getLayoutInflater().inflate(R.layout.target_statistic_layout, mHabitStatisticContainer);
            if (mHabitStatisticContainer != null) {
                View v = mHabitStatisticContainer.getChildAt(i++);
                if ( v instanceof HabitStatisticView) {
                    ((HabitStatisticView) v).setupView((ViewGroup) v, item);
                }
            }
        }
    }
}
