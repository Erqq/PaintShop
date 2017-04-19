package fi.tamk.tiko.erqq.paintshop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class DrawingCanvas extends AppCompatActivity {
    DrawingView dv ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dv = new DrawingView(this);
        dv.mPaint.setColor(Color.RED);
        setContentView(dv);

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
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case (R.id.red):
                dv.mPaint.setColor(Color.RED);

                return true;
            case (R.id.green):
                dv.mPaint.setColor(Color.GREEN);
                return true;
            case (R.id.blue):
                dv.mPaint.setColor(Color.BLUE);
                return true;
            case (R.id.straightLine):
                dv.setStraigth(!dv.isStraigth());
                return true;
        }
        return false;
    }

}
