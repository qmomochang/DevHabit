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

        mAccu = 0;
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
        for (int i = 0; i < mDataList.size(); i++) {
            value = mDataList.get(i) / 2;  // done equals 2, normalize it
            mAccu += value;
        }
        if (mAccu == 0) {
            mAccu = 1;
        }
        mMax = mAccu;
        mMin = 0;

        float diff = mMax / 4;
        yaxis = new String[5];
        yaxis[0] = "0";
        yaxis[1] = Float.toString(diff);
        yaxis[2] = Float.toString(diff * 2);
        yaxis[3] = Float.toString(diff * 3);
        yaxis[4] = Float.toString(mMax);


        // setup timing to get width/height
        final ViewTreeObserver observer = getViewTreeObserver();
        if (observer != null) {
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mWidth = getWidth();
                    mHeight = getHeight();

                    xpts = new float[31];
                    ypts = new float[31];
                    drawLines = new float[62];
                    float diffBy30 = (mWidth - 100) / 30;

                    float yBase = mHeight - 40;   // yaxi is inverted, rename it for better recognition
                    float yHeight = mHeight - 80;

                    for (int i = 0; i < 31; i++) {
                        xpts[i] = 50 + diffBy30 * i;

                        if (mDataList.get(i).equals(2)) {
                            ypts[i] = yHeight / mMax;
                        } else {
                            ypts[i] = 0;
                        }
                        android.util.Log.v("MCLOG", "before convert x = " + xpts[i] + ", ypts = " + ypts[i]);
                    }

                    convertYPTs(ypts, yBase);

//                    for (int i = 0; i < 30; i++) {
//                        ypts[i] = mHeight - ypts[i];
//                        if (ypts[i] < 40) {
//                            ypts[i] = 40;
//                        }
//                    }
                    dumpXYpts();
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    invalidate();
                }
            });
        }

    }

    private void convertYPTs(float[] arr, float base) {

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] != 0) {
                arr[i] += arr[i - 1];
            }
        }

        arr[0] = base;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] == 0) {
                arr[i] = base;
            } else {
                arr[i] = base - arr[i];
            }
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
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(30);

        // FontMetrics fontMetrics = paint.getFontMetrics();计算字的高度
        // horizontal lines
        int yShift = (mHeight - 80) / 4;
        for (int i = 0; i < 5; i++) {
            canvas.drawLine(50,                         // x1
                    mHeight - 40 - yShift * i,       // y1
                    mWidth - 50,                // x2
                    mHeight - 40 - yShift * i,       // y2
                    paint);

            canvas.drawText(yaxis[4-i], 25, 60 + yShift * i, textPaint);
        }

        // vertical lines
        int xShift = (mWidth - 100) / 30;
        for (int i = 0; i < 31; i++) {
            if (i % 5 == 0) {
                canvas.drawLine(50 + xShift * i,        // x1
                        40,                         // y1
                        50 + xShift * i,        // x2
                        mHeight - 40,               // y2
                        paint);

                canvas.drawText(mDateList.get(i), 50 + xShift * i, 30, textPaint);
            }
        }
    }

    private void drawData(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#FF00AA00"));
        //paint.setColor(Color.BLACK);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);

        //canvas.drawLines(drawLines, paint);
        for (int i = 0; i < 30; i++) {
            canvas.drawLine(xpts[i], ypts[i], xpts[i + 1], ypts[i + 1], paint);
        }
    }

    private void dumpXYpts() {
        android.util.Log.v("MCLOG", "dumpXYpts xpts = " + xpts + ", ypts = " + ypts);
        if (xpts != null && ypts != null) {
            for (int i = 0; i < 30; i++) {
                android.util.Log.v("MCLOG", "xpts[" + i + "] = " + xpts[i] + ", ypts[" + i + "] = " + ypts[i]);
            }
        }
    }
}