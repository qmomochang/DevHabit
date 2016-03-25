package com.habit.devhabit;

import android.app.Activity;
import android.content.Context;

import android.os.Handler;
import android.support.annotation.UiThread;
import android.test.UiThreadTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;


import java.util.List;

/**
 * Created by michael on 2016/3/18.
 */
public class TargetController {
    Context mContext;
    ViewGroup mHabitContainer;
    ViewGroup mHabitStatisticContainer;
    ViewGroup mHabitRootView;

    Handler mHandler;
    private ItemDAO mItemDAO;
    private List<Item> mItems;

    public TargetController(Context ctx) {
        mContext = ctx;
        mHandler = new Handler();
    }

    public void initHabitViewUi(ViewGroup container) {
        mHabitRootView = container;
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
                if (v instanceof HabitView) {
                    ((HabitView) v).setupView((ViewGroup) v, item);
                    final Item finalItem = item;
                    ((HabitView) v).setOnDataChangedListener(new HabitView.OnDataChangedListener() {
                        @Override
                        public void onDataChanged() {
                            if (mHabitStatisticContainer != null) {
                                View child;
                                for (int i = 0; i < mHabitStatisticContainer.getChildCount(); i++) {
                                    child = mHabitStatisticContainer.getChildAt(i);
                                    if (child != null && child instanceof HabitStatisticView && ((HabitStatisticView) child).getItem() != null) {
                                        if (finalItem.getTitle().equals(((HabitStatisticView) child).getItem().getTitle())) {
                                            ((HabitStatisticView) child).updateView();
                                        }
                                    }

                                }
                            }
                        }

                        @Override
                        public void onDataDeleted() {
                            if (mHabitStatisticContainer != null) {
                                View child;
                                for (int i = 0; i < mHabitStatisticContainer.getChildCount(); i++) {
                                    child = mHabitStatisticContainer.getChildAt(i);
                                    if (child != null && child instanceof HabitStatisticView && ((HabitStatisticView) child).getItem() != null) {
                                        if (finalItem.getTitle().equals(((HabitStatisticView) child).getItem().getTitle())) {
                                            ((HabitStatisticView) child).removeView();
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

    public void addHabit(ViewGroup habitContainer, ViewGroup habitStatisticContainer) {
        mHabitContainer = (ViewGroup) habitContainer.findViewById(R.id.habit_container);
        mHabitStatisticContainer = (ViewGroup) habitStatisticContainer.findViewById(R.id.habit_chart_container);

        Item item = mItemDAO.getLast();

        ((Activity) mContext).getLayoutInflater().inflate(R.layout.target_layout, mHabitContainer);
        if (mHabitContainer != null) {
            int i = mHabitContainer.getChildCount();
            View v = mHabitContainer.getChildAt(i - 1);

            if (v instanceof HabitView) {
                ((HabitView) v).setupView((ViewGroup) v, item);
                addHabitStatisticView(v,item);

                final ViewTreeObserver observer = mHabitContainer.getViewTreeObserver();
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                smoothScrollHabitView();
                            }
                        }, 1000);
                        observer.removeOnGlobalLayoutListener(this);
                    }
                });
            }
        }
    }

    private void addHabitStatisticView(View v, Item item) {
        final Item finalItem = item;
        ((HabitView) v).setOnDataChangedListener(new HabitView.OnDataChangedListener() {
            @Override
            public void onDataChanged() {
                if (mHabitStatisticContainer != null) {
                    View child;
                    for (int i = 0; i < mHabitStatisticContainer.getChildCount(); i++) {
                        child = mHabitStatisticContainer.getChildAt(i);
                        if (child != null && child instanceof HabitStatisticView && ((HabitStatisticView) child).getItem() != null) {
                            if (finalItem.getTitle().equals(((HabitStatisticView) child).getItem().getTitle())) {
                                ((HabitStatisticView) child).updateView();
                            }
                        }
                    }
                }
            }

            @Override
            public void onDataDeleted() {
                if (mHabitStatisticContainer != null) {
                    View child;
                    for (int i = 0; i < mHabitStatisticContainer.getChildCount(); i++) {
                        child = mHabitStatisticContainer.getChildAt(i);
                        if (child != null && child instanceof HabitStatisticView && ((HabitStatisticView) child).getItem() != null) {
                            if (finalItem.getTitle().equals(((HabitStatisticView) child).getItem().getTitle())) {
                                ((HabitStatisticView) child).removeView();
                            }
                        }

                    }
                }
            }
        });

        ((Activity) mContext).getLayoutInflater().inflate(R.layout.target_statistic_layout, mHabitStatisticContainer);
        int i2 = mHabitStatisticContainer.getChildCount();
        View v2 = mHabitStatisticContainer.getChildAt(i2 - 1);
        ((HabitStatisticView)v2).setupView(mHabitStatisticContainer, item);
    }

    public void smoothScrollHabitView() {
        View scrollView = mHabitRootView.findViewById(R.id.habit_scroll);
        if (scrollView != null && scrollView instanceof ScrollView) {
            ((ScrollView) scrollView).fullScroll(ScrollView.FOCUS_DOWN);
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
                if (v instanceof HabitStatisticView) {
                    ((HabitStatisticView) v).setupView((ViewGroup) v, item);
                }
            }
        }
    }
}
