package com.yko.loading;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yko on 2018/11/18.
 */

public class Loading extends View {
    private final int BALL_COUNT = 10;
    private final int DURATION = 18000;
    private final double PROPORTION = 0.7;

    Context mContext;
    private int width = 0, height = 0;
    int[] logoPosition;
    Paint mPaint;
    private Bitmap pic = null;
    Ball[] balls = new Ball[BALL_COUNT];
    long startTime = System.currentTimeMillis();

    public Loading(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public Loading(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public Loading(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        for(int i = 0; i < BALL_COUNT; i++){
            int defaultDegree = 360 / BALL_COUNT * i;
            balls[i] = new Ball(defaultDegree);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        pic = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
        pic = zoomBitmap(pic, (int)(width * PROPORTION), (int)(height * PROPORTION));
        logoPosition = new int[2];
        logoPosition[0] = (int)(width * ((1 - PROPORTION) / 2));
        logoPosition[1] = (int)(height * ((1 - PROPORTION) / 2));
        int radius = (int)(width * ((1 - PROPORTION) / 4));
        int round = (int)(width * (PROPORTION / 2));
        for(int i = 0; i < BALL_COUNT; i++){
            balls[i].setRound(round).setRadius(radius);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(pic, logoPosition[0], logoPosition[1], mPaint);
        long time = System.currentTimeMillis();
        for(int i = 0; i < BALL_COUNT; i++){
            int duration = (int)(time - startTime)% DURATION;
            int degree = (duration * 360 / DURATION);
            balls[i].drawBall(canvas, degree);
        }
        if(animating) {
            invalidate();
        }
    }

    public void start(){
        animating = true;
        invalidate();
    }

    private boolean animating = true;
    public void stop(){
        animating = false;
    }

    public Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        Bitmap newbmp = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            float scaleWidht = ((float) w / width);
            float scaleHeight = ((float) h / height);
            matrix.postScale(scaleWidht, scaleHeight);
            newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }
        return newbmp;
    }

    public class Ball {
        private int radius = 5;
        private int round = 40;
        private Paint mPaint;
        private int defaultDegree;
        public Ball(int defaultDegree){
            this(Color.BLUE, defaultDegree);
        }

        public Ball(int color, int defaultDegree){
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(color);
            this.defaultDegree = defaultDegree;
        }

        public Ball setRound(int round){
            this.round = round;
            return this;
        }

        public Ball setRadius(int radius){
            this.radius = radius;
            return this;
        }

        public void drawBall(Canvas canvas, int degree){
            int newDegree = degree + defaultDegree;
            float[] location = new float[2];
            location[0] = (float)(round + radius * 2 + (round + radius) * Math.sin(Math.toRadians(newDegree)));
            location[1] = (float)(round + radius * 2 + (round + radius) * Math.cos(Math.toRadians(newDegree)));
            canvas.drawCircle(location[0], location[1], radius, mPaint);
        }
    }

}
