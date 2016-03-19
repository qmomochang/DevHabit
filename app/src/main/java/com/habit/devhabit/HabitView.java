package com.habit.devhabit;

import android.app.Activity;
import android.content.Context;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by qmo-i7 on 2016/3/19.
 */
public class HabitView extends LinearLayout {
    Context mContext;
    Item mItem;
    PopupMenu mTargetPopupMenu;

    public HabitView(Context context) {
        super(context);
        mContext = context;
    }

    public HabitView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        mContext = context;
    }

    public HabitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void setupView(ViewGroup vg, Item item) {
        mItem = item;
        setupHabitView(vg, item);
        setupHabitViewMenu(vg);
    }

    private void setupHabitViewMenu(ViewGroup container) {
        // set listener
        ImageButton mTargetTitleButton = (ImageButton) container.findViewById(R.id.target_title_btn);
        if (mTargetTitleButton != null) {
            mTargetTitleButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mTargetPopupMenu.show();
                    return false;
                }
            });
        }
        //Creating the instance of PopupMenu
        mTargetPopupMenu = new PopupMenu(mContext, mTargetTitleButton);
        mTargetPopupMenu.getMenuInflater().inflate(R.menu.target_menu, mTargetPopupMenu.getMenu());

        //registering popup with OnMenuItemClickListener
        mTargetPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.modify:
                        break;
                    case R.id.delete:
                        ItemDAO itemDAO = new ItemDAO(mContext);
                        itemDAO.delete(mItem.getId());

                        ((ViewGroup)getParent()).removeView(HabitView.this);
                        break;
                }
                return true;
            }
        });
    }

    private void setupHabitView(ViewGroup container, Item item) {
        // setup title
        ((TextView) findViewById(R.id.target_title)).setText(item.getTitle());

        // setup date list
        Calendar c = Calendar.getInstance();
        int[] arr = {R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7};
        for (int i = 0; i < 7; i++) {
            int startDay = c.get(Calendar.DAY_OF_WEEK);
            String dayString = convertDayOfWeekToString(startDay + i);
            setupButtonDayText(container, arr[i], dayString);
            setupButtonListener(container, arr[i]);
        }
    }

    private void setupButtonDayText(ViewGroup vg, int resId, String day_of_week) {
        ViewGroup vg1 = (ViewGroup) vg.findViewById(resId);
        if (vg1 != null) {
            TextView tv = (TextView) vg1.findViewById(R.id.day_of_week);
            if (tv != null) {
                tv.setText(day_of_week);
            }
        }
    }

    private String convertDayOfWeekToString(int day_of_week) {
        if (day_of_week > 7) {
            day_of_week -= 7;
        }
        switch (day_of_week) {
            case 1:
                return getResources().getString(R.string.sunday);
            //break;
            case 2:
                return getResources().getString(R.string.monday);
            //break;
            case 3:
                return getResources().getString(R.string.tuesday);
            //break;
            case 4:
                return getResources().getString(R.string.wednesday);
            //break;
            case 5:
                return getResources().getString(R.string.thursday);
            //break;
            case 6:
                return getResources().getString(R.string.friday);
            //break;
            case 7:
                return getResources().getString(R.string.saturday);
            //break;

            default:
                return getResources().getString(R.string.error);
            //break;
        }
    }

    private void setupButtonListener(ViewGroup vg, int resId) {
        ViewGroup vg1 = (ViewGroup) vg.findViewById(resId);
        if (vg1 != null) {
            Button btn = (Button) vg1.findViewById(R.id.date_btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object status = v.getTag();
                    if (status == null) {
                        status = (Object) 1;
                    }
                    switch ((int) status) {
                        case 1: // done, green
                            v.getBackground().setColorFilter(Color.parseColor("#FF00FF00"), android.graphics.PorterDuff.Mode.MULTIPLY);
                            v.setTag((Object) 2);
                            break;
                        case 2: // fail, red
                            v.getBackground().setColorFilter(Color.parseColor("#FFFF0000"), android.graphics.PorterDuff.Mode.MULTIPLY);
                            v.setTag((Object) 3);
                            break;

                        case 3: // skip, blue
                            v.getBackground().setColorFilter(Color.parseColor("#FF0000FF"), android.graphics.PorterDuff.Mode.MULTIPLY);
                            v.setTag((Object) 1);
                            break;
                    }
                }
            });
        }
    }


}
