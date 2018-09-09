package com.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Practice11PieChartView extends View {

    private Paint mPaint = new Paint();
    private Path mPath = new Path();
    private List<Data> mDatas = new ArrayList<>();
    private float mRadius;
    private float mAngleMargin;
    private float mOffsetFactor;
    private RectF mRect = new RectF();

    public Practice11PieChartView(Context context) {
        super(context);

        initConfig();
        getData();
        preExcuteData();
    }

    public Practice11PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initConfig();
        getData();
        preExcuteData();
    }

    public Practice11PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initConfig();
        getData();
        preExcuteData();
    }

    private void initConfig() {
        mRadius = 250f;
        mAngleMargin = 2.0f;
        mOffsetFactor = 0.1f;
    }

    private void getData() {
        Random random = new Random();
        mDatas.add(new Data("PIG", Color.GREEN, random.nextInt(100)));
        mDatas.add(new Data("ASS", Color.YELLOW, random.nextInt(100)));
        mDatas.add(new Data("GIRL", Color.RED, random.nextInt(100)));
        mDatas.add(new Data("BBBBBBBBB", Color.GRAY, random.nextInt(100)));
        mDatas.add(new Data("JJJJJJJJJJJJJJJ", Color.BLUE, random.nextInt(100)));
    }

    private void preExcuteData() {
        //set if need enlarge
        float maxValue = 0;
        int maxIdx = 0;
        for (int i = 0; i < mDatas.size(); i++) {
            Data d = mDatas.get(i);
            if (d.mValue > maxValue) {
                maxIdx = i;
                maxValue = d.mValue;
            }
        }
        mDatas.get(maxIdx).mNeedEnlarge = true;

        //get percents&angle
        float sum = 0;
        for (Data d : mDatas) {
            sum += d.mValue;
        }

        for (Data d : mDatas) {
            d.mPercent = d.mValue * 100f / sum;
            d.mAngle = d.mValue * 360f / sum;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // update Rect
        float centerX = canvas.getWidth() / 2f;
        float centerY = canvas.getHeight() / 2f;
        mRect.set(centerX - mRadius, centerY - mRadius, centerX + mRadius, centerY + mRadius);

        // draw pie chart
        float lastAngel = 0;
        for (Data d : mDatas) {
            mPaint.setColor(d.mColor);
            mPaint.setStyle(Paint.Style.FILL);

            if (d.mNeedEnlarge) {
                float tmpAngle = lastAngel + (d.mAngle / 2);
                float deltaX = (float) Math.cos(tmpAngle * Math.PI / 180f) * mRadius * mOffsetFactor;
                float deltaY = (float) Math.sin(tmpAngle * Math.PI / 180f) * mRadius * mOffsetFactor;

                canvas.drawArc(mRect.left + deltaX,
                        mRect.top + deltaY,
                        mRect.right + deltaX,
                        mRect.bottom + deltaY,
                        lastAngel, d.mAngle - mAngleMargin, true, mPaint);
            } else {
                canvas.drawArc(mRect, lastAngel, d.mAngle - mAngleMargin, true, mPaint);

            }
            lastAngel += d.mAngle;
        }

        //draw lines && text
        lastAngel = 0;
        for (Data d: mDatas) {
            mPaint.reset();
            mPath.reset();

            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(Color.WHITE);
            mPaint.setStrokeWidth(2f);

            float tmpAngle = lastAngel + (d.mAngle / 2);
            boolean isLeft = (tmpAngle > 90 && tmpAngle < 270);

            float startX;
            float startY;
            float endX;
            float endY;
            if (d.mNeedEnlarge) {
                float deltaX = (float) Math.cos(tmpAngle * Math.PI / 180f) * mRadius * mOffsetFactor;
                float deltaY = (float) Math.sin(tmpAngle * Math.PI / 180f) * mRadius * mOffsetFactor;
                startX = (float) Math.cos(tmpAngle * Math.PI / 180f) * mRadius + centerX + deltaX;
                startY = (float) Math.sin(tmpAngle * Math.PI / 180f) * mRadius + centerY + deltaY;
                endX = (float) Math.cos(tmpAngle * Math.PI / 180f) * mRadius * 1.1f + centerX + deltaX;
                endY = (float) Math.sin(tmpAngle * Math.PI / 180f) * mRadius * 1.1f + centerY + deltaY;
            } else {
                startX = (float) Math.cos(tmpAngle * Math.PI / 180f) * mRadius + centerX;
                startY = (float) Math.sin(tmpAngle * Math.PI / 180f) * mRadius + centerY;
                endX = (float) Math.cos(tmpAngle * Math.PI / 180f) * mRadius * 1.1f + centerX;
                endY = (float) Math.sin(tmpAngle * Math.PI / 180f) * mRadius * 1.1f + centerY;
            }

            // 1st line
            mPath.moveTo(startX, startY);
            mPath.lineTo(endX, endY);
            // 2nd line
            endX += 50 * (isLeft? -1 : 1);
            mPath.lineTo(endX, endY);
            canvas.drawPath(mPath, mPaint);

            // draw text
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setTextSize(25);
            canvas.drawText(d.mName, endX - (isLeft? mPaint.measureText(d.mName): 0), endY, mPaint);

            lastAngel += d.mAngle;
        }

        //draw text
    }


    public static class Data {

        String mName;
        int mColor;
        float mValue;
        boolean mNeedEnlarge;
        float mPercent;
        float mAngle;

        public Data(String name, int color, int value) {
            mName = name;
            mColor = color;
            mValue = value;
        }
    }
}
