package fi.tamk.tiko.erqq.paintshop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    final int PERMISSION_REQUEST_ID=0;
    private static final int SELECT_PICTURE = 1;
    private String Path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){
            String[] listofpermiossions={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this,listofpermiossions,PERMISSION_REQUEST_ID);

        }
            setContentView(R.layout.activity_main);

    }
    public void newDrawing(View v){

            Intent intent = new Intent(this, DrawingCanvas.class);
            startActivity(intent);

    }

    public void LoadDrawing(View v){
        Intent intent = new Intent();
        intent.setType("image/png");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                Path = selectedImageUri.toString();
                System.out.println(Path);
                Intent intent = new Intent(this, DrawingCanvas.class);
                intent.putExtra("loadedpicture",Path);

                startActivity(intent);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // permission was granted, yay! Do the
            // contacts-related task you need to do.
        } else {


            Toast.makeText(MainActivity.this, "Permission denied to save and load drawings from your External storage", Toast.LENGTH_LONG).show();
            MainActivity.this.finish();
        }
        return;
    }

}
