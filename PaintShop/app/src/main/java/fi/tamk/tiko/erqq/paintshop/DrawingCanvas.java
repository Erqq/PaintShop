package fi.tamk.tiko.erqq.paintshop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DrawingCanvas extends AppCompatActivity {
    DrawingView dv ;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dv = new DrawingView(this);
        dv.setDrawingCacheEnabled(true);
        dv.setColor(Color.RED);


        setContentView(dv);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                    }
                });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.mainmenu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        dv.mPaint.setXfermode(null);
        dv.mPaint.setAlpha(0xFF);
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case (R.id.red):
                dv.mPaint.setColor(Color.RED);

                return true;
            case (R.id.green):
                dv.setColor(Color.GREEN);
                return true;
            case (R.id.blue):
                dv.setColor(Color.BLUE);
                return true;
            case (R.id.straightLine):
                dv.setStraigth(!dv.isStraigth());
                return true;
            case (R.id.light):
                dv.mPaint.setXfermode(new PorterDuffXfermode(
                        PorterDuff.Mode.SRC_ATOP));
                dv.setColor(Color.parseColor("#FAFAFA"));
                dv.mPaint.setAlpha(0x90);
                return true;
            case (R.id.full):
                dv.mPaint.setXfermode(new PorterDuffXfermode(
                        PorterDuff.Mode.SRC_ATOP));
                dv.setColor(Color.parseColor("#FAFAFA"));
                return true;
            case (R.id.sizeSmall):
                dv.setSize(10);
                return true;
            case (R.id.sizeMedium):
                dv.setSize(20);
                return true;
            case (R.id.sizeBig):
                dv.setSize(30);
                return true;
        }
        return false;
    }

}
