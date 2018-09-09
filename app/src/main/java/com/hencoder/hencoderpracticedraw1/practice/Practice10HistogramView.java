package com.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Practice10HistogramView extends View {

    Path mPath = new Path();
    Paint mPaint = new Paint();
    List<Data> mData = new ArrayList<>();
    RectF mBarTmpRect = new RectF();
    float mMarginFactor;
    float mBlankAndBarWidthRatio;


    public Practice10HistogramView(Context context) {
        super(context);
    }

    public Practice10HistogramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        config();
        initData();
        preExcuteData();
    }

    public Practice10HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();
        mPaint.reset();

        // draw coordinate
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2.0f);
        mPaint.setColor(Color.WHITE);

        float startX = canvas.getWidth() * mMarginFactor;
        float startY = canvas.getWidth() * mMarginFactor;
        float chartWidth = canvas.getWidth() * (1 - 2 * mMarginFactor);
        float chartHeight = canvas.getHeight() * (1 - 2 * mMarginFactor);
        float originX = startX;
        float originY = startY + chartHeight;
        mPath.moveTo(startX, startY);
        mPath.rLineTo(0, chartHeight);
        mPath.rLineTo(chartWidth, 0);
        canvas.drawPath(mPath, mPaint);

        // draw histogram
        float tmpSize = (float) mData.size();
        float blankWidth = chartWidth / (tmpSize + 1f + tmpSize / mBlankAndBarWidthRatio);
        float barWidth = blankWidth / mBlankAndBarWidthRatio;

        for (int i = 0; i < mData.size(); i++) {
            Data d = mData.get(i);

            float left = originX + (i + 1f) * blankWidth + i * barWidth;
            float top = originY - chartHeight * d.mPercent;
            float right = left + barWidth;
            float bottom = originY;
            mBarTmpRect.set(left, top, right, bottom);

            mPaint.setColor(Color.GREEN);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(mBarTmpRect, mPaint);

            mPaint.setTextSize(20);
            mPaint.setColor(Color.WHITE);
            float spacing = mPaint.getFontSpacing();
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(d.mName, (left + right) / 2f, bottom + spacing, mPaint);
        }
    }

    private void config() {
        mMarginFactor = 0.15f;
        mBlankAndBarWidthRatio = 0.2f;
    }

    private void initData() {
        Random random = new Random();
        mData.add(new Data("111", random.nextInt(100)));
        mData.add(new Data("222", random.nextInt(100)));
        mData.add(new Data("333", random.nextInt(100)));
        mData.add(new Data("444", random.nextInt(100)));
        mData.add(new Data("555", random.nextInt(100)));
        mData.add(new Data("666", random.nextInt(100)));
    }

    private void preExcuteData() {
        //find max value
        float max = 0;
        for (Data d : mData) {
            if (d.mValue > max) {
                max = d.mValue;
            }
        }

        for (Data d : mData) {
            d.mPercent = d.mValue / max;
        }
    }

    public static class Data {

        public String mName;
        public float mValue;
        public float mPercent;

        public Data(String name, float value) {
            mName = name;
            mValue = value;
        }
    }
}
