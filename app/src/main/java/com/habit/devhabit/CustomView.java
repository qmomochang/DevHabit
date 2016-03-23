package com.habit.devhabit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by qmo-i7 on 2016/3/24.
 */
public class CustomView extends View {
    List<String> mDateList;
    List<Integer> mDataList;

    float mAccu = 0;
    float mMax;
    int mMaxIndex;
    float mMin;
    int mMinIndex;
    String[] yaxis;
    float[] xpts;
    float[] ypts;
    float[] drawLines;
    int mWidth;
    int mHeight;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setupList(List<String> dateList, List<Integer> dataList) {
        // setup date
        if (mDateList == null) {
            mDateList = new ArrayList<String>();
        } else {
            mDateList.clear();
        }
        mDateList.addAll(dateList);

        // setup data
        if (mDataList == null) {
            mDataList = new ArrayList<Integer>();
        } else {
            mDataList.clear();
        }
        mDataList.addAll(dataList);

        // found out max/min
        int value;
        for (int i = 0; i < 30; i++) {
            value = mDataList.get(i) / 2;  // done equals 2, normalize it
            mAccu += value;
        }
        if (mAccu == 0) {
            mAccu = 1;
        }
        mMax = mAccu;
        mMin = 0;

        float diff = (mMax - mMin) / 4;
        yaxis = new String[4];
        yaxis[0] = Float.toString(mMin);
        yaxis[1] = Float.toString(mMin + diff * 2);
        yaxis[2] = Float.toString(mMin + diff * 3);
        yaxis[3] = Float.toString(mMax);


        // setup timing to get width/height
        ViewTreeObserver observer = getViewTreeObserver();
        if (observer != null) {
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mWidth = getWidth();
                    mHeight = getHeight();

                    xpts = new float[30];
                    ypts = new float[30];
                    drawLines = new float[62];
                    float diffBy30 = mWidth / 30;

                    xpts[0] = 50;
                    ypts[0] = 30;

                    for (int i = 1; i < 30; i++) {
                        xpts[i] = 50 + diffBy30 * i;
                        if (mDataList.get(i).equals(2)) {
                            ypts[i] += ypts[i - 1] + mDataList.get(i) / 2 * mHeight / 4;
                        } else {
                            ypts[i] = 30;
                        }
                    }
                    for (int i = 0; i < 30; i++) {
                        ypts[i] = mHeight - ypts[i];
                        if (ypts[i] > mHeight - 40) {
                            ypts[i] = mHeight - 40;
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDataList != null && mDateList != null) {
            drawChart(canvas);
            drawData(canvas);
        }
    }

    private void drawChart(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#44888888"));
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.parseColor("#FF000000"));
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextAlign(Paint.Align.CENTER);//字水平居中
        textPaint.setTextSize(30);

        // FontMetrics fontMetrics = paint.getFontMetrics();计算字的高度


        // horizontal lines
        for (int i = 0; i < 4; i++) {
            canvas.drawLine(50,                         // x1
                    60 + mHeight / 4 * i,       // y1
                    mWidth - 50,                // x2
                    60 + mHeight / 4 * i,       // y2
                    paint);

            canvas.drawText(yaxis[3 - i], 25, 60 + mHeight / 4 * i, textPaint);
        }

        // vertical lines
        for (int i = 0; i < 6; i++) {
            canvas.drawLine(50 + mWidth / 6 * i,        // x1
                    40,                         // y1
                    50 + mWidth / 6 * i,        // x2
                    mHeight - 40,               // y2
                    paint);

            canvas.drawText(mDateList.get(25 - i * 5), 50 + mWidth / 6 * i, 30, textPaint);
        }
    }

    private void drawData(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#FF00AA00"));
        //paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);

        //canvas.drawLines(drawLines, paint);
        for (int i = 0; i < 29; i++) {
            canvas.drawLine(xpts[i], ypts[i], xpts[i + 1], ypts[i + 1], paint);
        }
    }
}