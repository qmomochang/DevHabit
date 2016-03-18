package com.simon.hellocoordinatorlayout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by michael on 2016/3/18.
 */
public class TargetController {
    Context mContext;
    public TargetController(Context ctx) {
        mContext = ctx;
        initUi();
    }

    private void initUi() {
        ViewGroup rootView = (ViewGroup) ((Activity)mContext).findViewById(R.id.rootView);
        Calendar c = Calendar.getInstance();
        int[] arr = {R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7};

        for (int i=0;i<7;i++) {
            int startDay = c.get(Calendar.DAY_OF_WEEK);
            String dayString = convertDayOfWeekToString(startDay + i);
            setupButtonDayText(rootView, arr[i], dayString);
            setupButtonListener(rootView, arr[i]);
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
        if (day_of_week>7) {
            day_of_week-=7;
        }
        switch (day_of_week) {
            case 1:
                return "Sun";
            //break;
            case 2:
                return "Mon";
            //break;
            case 3:
                return "Tue";
            //break;
            case 4:
                return "Wed";
            //break;
            case 5:
                return "Thu";
            //break;
            case 6:
                return "Fri";
            //break;
            case 7:
                return "Sat";
            //break;

            default:
                return "ERR";
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
                        status = (Object)1;
                    }
                    switch ((int)status) {
                        case 1: // done, green
                            v.getBackground().setColorFilter(Color.parseColor("#FF00FF00"), android.graphics.PorterDuff.Mode.MULTIPLY);
                            v.setTag((Object)2);
                            break;
                        case 2: // fail, red
                            v.getBackground().setColorFilter(Color.parseColor("#FFFF0000"), android.graphics.PorterDuff.Mode.MULTIPLY );
                            v.setTag((Object)3);
                            break;

                        case 3: // skip, blue
                            v.getBackground().setColorFilter(Color.parseColor("#FF0000FF"), android.graphics.PorterDuff.Mode.MULTIPLY);
                            v.setTag((Object)1);
                            break;
                    }
                }
            });
        }
    }
}
