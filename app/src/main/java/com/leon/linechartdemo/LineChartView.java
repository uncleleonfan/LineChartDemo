package com.leon.linechartdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon on 2017/5/27.
 */

public class LineChartView extends View {


    private final Paint mAxisPaint;
    private final Paint mDotPaint;
    private final Paint mLinePaint;

    private int[] mDataList;
    private int mMax;
    private String[] mHorizontalAxis;
    private final int mRadius;



    private List<Dot> mDots = new ArrayList<Dot>();
    private Rect mTextRect;
    private RectF mTemp;
    private int mGap;
    private Path mPath;
    private int mStep;

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPath = new Path();

        mAxisPaint = new Paint();
        mAxisPaint.setAntiAlias(true);
        mAxisPaint.setTextSize(20);
        mAxisPaint.setTextAlign(Paint.Align.CENTER);

        mDotPaint = new Paint();
        mDotPaint.setAntiAlias(true);

        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        mTextRect = new Rect();
        mTemp = new RectF();
        mGap = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());


        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(3);
        mLinePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mDots.clear();
        int width = w - getPaddingLeft() - getPaddingRight();
        int height = h - getPaddingTop() - getPaddingBottom();
        mStep = width / (mDataList.length - 1);

        mAxisPaint.getTextBounds(mHorizontalAxis[0], 0, mHorizontalAxis[0].length(), mTextRect);
        int barHeight = height - mTextRect.height() - mGap;
        float heightRatio = barHeight / mMax;

        for (int i = 0; i < mDataList.length; i++) {

            Dot dot = new Dot();
            dot.value = mDataList[i];
            dot.transformedValue = (int) (dot.value * heightRatio);

            dot.x = mStep * i + getPaddingLeft();
            dot.y = getPaddingTop() + barHeight - dot.transformedValue;

            if (i == 0) {
                mPath.moveTo(dot.x, dot.y);
            } else {
                mPath.lineTo(dot.x, dot.y);
            }

            mDots.add(dot);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mLinePaint);
        for (int i = 0; i < mHorizontalAxis.length; i++) {
            String axis = mHorizontalAxis[i];
            int x = getPaddingLeft() + i * mStep;
            int y = getHeight()-getPaddingBottom();
            canvas.drawText(axis, x, y, mAxisPaint);
        }
    }

    public void setDataList(int[] dataList, int max) {
        mDataList = dataList;
        mMax= max;
    }

    public void setHorizontalAxis(String[] horizontalAxis) {
        mHorizontalAxis = horizontalAxis;
    }

    private class Dot {
        int x;
        int y;
        int value;
        int transformedValue;
    }
}
