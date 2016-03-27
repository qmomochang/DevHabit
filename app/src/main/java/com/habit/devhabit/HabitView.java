package com.habit.devhabit;

import android.content.Context;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by qmo-i7 on 2016/3/19.
 */
public class HabitView extends LinearLayout {
    Context mContext;
    View mThis;
    Item mItem;
    ViewGroup mContainer;
    PopupMenu mTargetPopupMenu;
    int mAccomplishedDays = 0;

    OnDataChangedListener mOnDataChangedListener;

    public interface OnDataChangedListener {
        void onDataChanged();
        void onDataDeleted();
    }

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
        mThis = this;
        mItem = item;
        mContainer = vg;
        setupHabitView(vg, item);
        setupHabitViewMenu(vg);
    }

    private void setupHabitViewMenu(ViewGroup container) {

        // set listener
        final ImageButton mTargetTitleButton = (ImageButton) container.findViewById(R.id.target_title_btn);
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

//                        ViewGroup parentView = (ViewGroup) getParent();
//                        if (parentView != null) {
//                            for (int i = 0;i<parentView.getChildCount();i++) {
//                                if (parentView.getChildAt(i) == mThis) {
//
//                                }
//                            }
//                        }

                        ((ViewGroup) getParent()).removeView(HabitView.this);

                        if (mOnDataChangedListener != null) {
                            mOnDataChangedListener.onDataDeleted();
                        }
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
        int startDay = c.get(Calendar.DAY_OF_WEEK);
        int day_of_year = c.get(Calendar.DAY_OF_YEAR);

        int[] arr = {R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7};
        for (int i = 0; i < 7; i++) {
            c.set(Calendar.DAY_OF_YEAR, day_of_year + i - 1);
            String dayString = convertDayOfWeekToString(c.get(Calendar.DAY_OF_WEEK));

            String sday_of_month = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
            String sday_of_year = Integer.toString(c.get(Calendar.DAY_OF_YEAR));
            setupButtonDayText(container, arr[i], dayString, sday_of_month, sday_of_year);
            setupButtonListener(container, arr[i]);
        }

        //
        Iterator it = mItem.getHash().entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            if (pair.getValue() == 2 || pair.getValue().equals("2") || pair.getValue().equals(2)) {
                mAccomplishedDays++;
            }
        }
        updateViewAccomplishedDays();
    }

    private void setupButtonDayText(ViewGroup vg, int resId, String day_of_week, String day_of_month, String day_of_year) {
        ViewGroup vg1 = (ViewGroup) vg.findViewById(resId);
        if (vg1 != null) {
            TextView tv = (TextView) vg1.findViewById(R.id.day_of_week);
            if (tv != null) {
                tv.setText(day_of_week);
            }
            Button btn = (Button) vg1.findViewById(R.id.date_btn);

            if (btn != null) {
                Calendar c = Calendar.getInstance();


                btn.setText(day_of_month);
                btn.setTag(day_of_year);

                if (mItem.getHash() != null && mItem.getHash().get(day_of_year) != null) {
                    //android.util.Log.v("MCLOG", "hash.get(" + day_of_year + ") = " + (int) mItem.getHash().get(day_of_year));
                    switch ((int) mItem.getHash().get(day_of_year)) {
                        case 0: // nothing
                            btn.setTextColor(Color.parseColor("#FF000000"));
                            btn.getBackground().setAlpha(0);
                            break;

                        case 1: // done
                            btn.setTextColor(Color.parseColor("#FFFFFFFF"));
                            btn.getBackground().setColorFilter(Color.parseColor("#FF00AA00"), PorterDuff.Mode.DST_IN);
                            break;

                        case 2: // failed
                            btn.setTextColor(Color.parseColor("#FFFFFFFF"));
                            btn.getBackground().setColorFilter(Color.parseColor("#FFFF0000"), android.graphics.PorterDuff.Mode.DST_IN);

                            break;

                        default:
                            btn.setTextColor(Color.parseColor("#FF000000"));
                            btn.getBackground().setAlpha(0);
                            break;
                    }
                    updateViewAccomplishedDays();
                } else {
                    btn.setTextColor(Color.parseColor("#FF000000"));
                    btn.getBackground().setAlpha(0);
                }
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
            final Button btn = (Button) vg1.findViewById(R.id.date_btn);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object obj = v.getTag();
                    String dayOfYear;
                    if (obj == null) {
                        dayOfYear = "0";
                    } else {
                        dayOfYear = (String) obj;
                    }
                    Object objStatus = mItem.getHash().get(dayOfYear);
                    int status;
                    if (objStatus == null) {
                        status = 0;
                    } else {
                        status = (int) objStatus;
                    }
                    switch (status) {
                        case 0: // never used
                            v.getBackground().setAlpha(255);
                            btn.setTextColor(Color.parseColor("#FFFFFFFF"));
                            v.getBackground().setColorFilter(Color.parseColor("#FF00AA00"), PorterDuff.Mode.SRC_IN);
                            mItem.getHash().put(dayOfYear, 2);
                            mAccomplishedDays++;
                            updateViewAccomplishedDays();

                            Calendar c = Calendar.getInstance();
                            if (dayOfYear.equals(Integer.toString(c.get(Calendar.DAY_OF_YEAR)))) {
                                new AlertDialog.Builder(mContext)
                                        .setTitle(R.string.app_name)
                                        .setMessage(R.string.complete_today_target)
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).show();
                            }
                            break;
//                        case 1: // done, green
//                            v.getBackground().setAlpha(255);
//                            btn.setTextColor(Color.parseColor("#FFFFFFFF"));
//                            v.getBackground().setColorFilter(Color.parseColor("#FFFF0000"), PorterDuff.Mode.SRC_IN);
//                            mItem.getHash().put(dayOfYear, 2);
//                            mAccomplishedDays--;
//                            updateViewAccomplishedDays();
//
//                            break;
                        case 2: // fail, red
                            v.getBackground().setAlpha(0);
                            btn.setTextColor(Color.parseColor("#FF000000"));
                            mItem.getHash().put(dayOfYear, 0);
                            mAccomplishedDays--;
                            updateViewAccomplishedDays();
                            break;
                        default:
                            btn.setTextColor(Color.parseColor("#FF000000"));
                            v.getBackground().setAlpha(0);
                            mItem.getHash().put(dayOfYear, 1);
                            break;
                    }
                    ItemDAO itemDAO = new ItemDAO(mContext);
                    itemDAO.update(mItem);
                    if (mOnDataChangedListener != null) {
                        mOnDataChangedListener.onDataChanged();
                    }
                }
            });
        }
    }

    public Item getItem() {
        return mItem;
    }

    private void updateViewAccomplishedDays() {
        if (mContainer != null) {
            TextView tx = (TextView) mContainer.findViewById(R.id.max_connected_days_now);
            tx.setText(Integer.toString(mAccomplishedDays));
        }
    }

    public void setOnDataChangedListener(OnDataChangedListener listener) {
        mOnDataChangedListener = listener;
    }
}
