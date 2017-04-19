package fi.tamk.tiko.erqq.paintshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void newDrawing(View v){
        Intent intent= new Intent(this,DrawingCanvas.class);
        startActivity(intent);
    }
}
