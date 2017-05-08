package fi.tamk.tiko.erqq.paintshop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;


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
    private String loaded="";

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    Context context;
    public Paint mPaint;
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    public String getLoaded() {
        return loaded;
    }

    public void setLoaded(String loaded) {
        this.loaded = loaded;
    }

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
    public void setColor(int color){
        mPaint.setColor(color);
    }
    public void setSize(int size){
        mPaint.setStrokeWidth(size);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (!loaded.equals("")) {
            Uri uri = Uri.parse(loaded);

            try {
                Matrix matrix = new Matrix();
                matrix.postRotate(-90);
                Bitmap bit = getBitmapFromUri(uri);

                if (bit.getWidth()>bit.getHeight()){
                    Bitmap rotate=Bitmap.createScaledBitmap(
                            bit.copy(Bitmap.Config.ARGB_8888, true), h, w, false);
                    mBitmap=Bitmap.createBitmap(rotate,0,0,rotate.getWidth(),rotate.getHeight(),matrix,false);
                }else {
                    mBitmap = Bitmap.createScaledBitmap(
                            bit.copy(Bitmap.Config.ARGB_8888, true), w, h, false);
                }

                mCanvas=new Canvas(mBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {


        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mBitmap.setHasAlpha(true);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.WHITE);
    }
        super.onSizeChanged(w, h, oldw, oldh);


    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
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
        invalidate();
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
