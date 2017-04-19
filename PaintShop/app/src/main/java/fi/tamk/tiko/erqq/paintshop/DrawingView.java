package fi.tamk.tiko.erqq.paintshop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;



/**
 * Created by Erqq on 19.4.2017.
 */

public class DrawingView extends View {


    private boolean straigth=false;
    public int width;
    public  int height;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    Context context;
    public Paint mPaint;
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    public DrawingView(Context c) {
        super(c);
        context=c;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.BEVEL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20);
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (straigth){
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawLine(startX, startY, endX, endY, mPaint);
        }else {
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, mPaint);
        }
    }



    private void touch_start(float x, float y) {
        if (straigth){

            mPath.moveTo(x, y);
            mPath.lineTo(x, y);
            startX =x;
            startY = y;
            mPath.reset();
        }else {
            mPath.reset();
            mPath.moveTo(x, y);
            mPath.lineTo(x, y);
            mX = x;
            mY = y;
        }
    }

    private void touch_move(float x, float y) {
        if (straigth){
            mPath.lineTo(endX,endY);
            endX = x;
            endY = y;
            mPath.reset();
        }else {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;

            }
        }
    }

    private void touch_up() {
        if (straigth){
            mPath.lineTo(mX, mY);
            mCanvas.drawLine(startX, startY, endX, endY, mPaint);
            mPath.reset();
        }else {
            mPath.lineTo(mX, mY);
            mCanvas.drawPath(mPath, mPaint);
            mPath.reset();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }
    public boolean isStraigth() {
        return straigth;
    }

    public void setStraigth(boolean straigth) {
        this.straigth = straigth;
    }
}
