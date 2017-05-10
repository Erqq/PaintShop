package fi.tamk.tiko.erqq.paintshop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;


/**
 * The DrawingView View enables the drawing canvas on which the user can draw.
 *
 * @author Eerik Timonen
 * @version 1.0
 * @since 1.0
 */

public class DrawingView extends View {

    private boolean straight = false;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    private String loaded = "";
    public Context context;
    public Paint mPaint;
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    /**
     * Defines the mPaint and mBitmapPaint options.
     *
     * @param c context of the view
     */
    public DrawingView(Context c) {
        super(c);
        context = c;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.BEVEL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20);
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    }

    /**
     * Creates new bitmap or a bitmap with a picture and creates the canvas with that bitmap.
     *
     * @param w    the width of the bitmap
     * @param h    the height of the bitmap
     * @param oldw the old width of the bitmap
     * @param oldh the old height of the bitmap
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (!loaded.equals("")) {
            Uri uri = Uri.parse(loaded);

            try {
                Matrix matrix = new Matrix();
                matrix.postRotate(-90);
                Bitmap bit = getBitmapFromUri(uri);
                if (bit!=null) {
                    if (bit.getWidth() > bit.getHeight()) {
                        Bitmap rotate = Bitmap.createScaledBitmap(
                                bit.copy(Bitmap.Config.ARGB_8888, true), h, w, false);
                        mBitmap = Bitmap.createBitmap(rotate, 0, 0, rotate.getWidth(),
                                rotate.getHeight(), matrix, false);
                    } else {
                        mBitmap = Bitmap.createScaledBitmap(
                                bit.copy(Bitmap.Config.ARGB_8888, true), w, h, false);
                    }
                    mCanvas = new Canvas(mBitmap);
                }else {
                    Toast.makeText(context, "There was something wrong with the image. " +
                                    "Try another one."
                            , Toast.LENGTH_LONG).show();
                }

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

    /**
     * Gets the Bitmap of the image from the Uri that is given in the intent extras.
     *
     * @param uri the uri of the image
     * @return image the bitmap of the image that is loaded
     * @throws IOException if Uri is not found
     */
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    /**
     * Draws the paths and bitmaps to the canvas.
     *
     * @param canvas the canvas that you draw on
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (straight) {
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawLine(startX, startY, endX, endY, mPaint);
        } else {
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

            canvas.drawPath(mPath, mPaint);
        }
    }

    /**
     * Gets the coordinates of the finger when it touches the screen first time and gives them to
     * the mPath. This is a helper class for the onTouchEvent methods MotionEvent.ACTION_DOWN case.
     *
     * @param x the x coordinate of the finger
     * @param y the y coordinate of the finger
     */
    private void touch_start(float x, float y) {
        if (straight) {

            mPath.moveTo(x, y);
            mPath.lineTo(x, y);
            startX = x;
            startY = y;
            mPath.reset();
        } else {
            mPath.reset();
            mPath.moveTo(x, y);
            mPath.lineTo(x, y);
            mX = x;
            mY = y;
        }
    }

    /**
     * Gets the coordinates of the finger when it moves on the screen and gives them to the mPath.
     * This is a helper class for the onTouchEvent methods MotionEvent.ACTION_MOVE case.
     *
     * @param x the x coordinate of the finger
     * @param y the y coordinate of the finger
     */
    private void touch_move(float x, float y) {
        if (straight) {
            mPath.lineTo(endX, endY);
            endX = x;
            endY = y;
            mPath.reset();
        } else {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
        }
    }

    /**
     * Gets the coordinates of the finger when it leaves the screen and gives them to the mPath.
     * This is a helper class for the onTouchEvent methods MotionEvent.ACTION_UP case.
     */
    private void touch_up() {
        if (straight) {
            mPath.lineTo(mX, mY);
            mCanvas.drawLine(startX, startY, endX, endY, mPaint);
            mPath.reset();
        } else {
            mPath.lineTo(mX, mY);
            mCanvas.drawPath(mPath, mPaint);
            mPath.reset();

        }
        invalidate();
    }

    /**
     * Gets the fingers movement on the screen.
     *
     * @param event the finger movement on the screen
     * @return boolean true
     */
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

    /**
     * Sets the color of the brush
     *
     * @param color the number of the color
     */
    public void setColor(int color) {
        mPaint.setColor(color);
    }

    /**
     * Sets the size of the brush
     *
     * @param size the width of the brush
     */
    public void setSize(int size) {
        mPaint.setStrokeWidth(size);
    }

    /**
     * Return the straight boolean
     *
     * @return straight boolean
     */
    public boolean isStraight() {
        return straight;
    }

    /**
     * Sets the straight boolean.
     *
     * @param straight the indicator if straightLine MenuItem is pressed
     */
    public void setStraight(boolean straight) {
        this.straight = straight;
    }

    /**
     * Sets the string loaded.
     *
     * @param loaded the url of the image which is opened
     */
    public void setLoaded(String loaded) {
        this.loaded = loaded;
    }
}
