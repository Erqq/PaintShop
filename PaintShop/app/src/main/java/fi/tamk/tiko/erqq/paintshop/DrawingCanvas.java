package fi.tamk.tiko.erqq.paintshop;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;

public class DrawingCanvas extends AppCompatActivity {
    DrawingView dv ;
    MenuItem tempitem;
    MenuItem tempItemSize;
    private boolean eraser=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle= getIntent().getExtras();


        dv = new DrawingView(this);
        dv.setDrawingCacheEnabled(true);
        dv.setColor(Color.RED);

        if (bundle !=null){
            System.out.println(bundle.get("loadedpicture"));
            if (bundle.containsKey("loadedpicture")){
                dv.setLoaded(bundle.get("loadedpicture").toString());
            }
        }
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
        tempitem=menu.findItem(R.id.red);
        tempitem.setCheckable(true);
        tempitem.setChecked(true);
        tempItemSize=menu.findItem(R.id.sizeMedium);
        tempItemSize.setCheckable(true);
        tempItemSize.setChecked(true);
        return true;
    }

    @Override
    public void onBackPressed() {


        final Dialog dialog= new Dialog(this);
        dialog.setTitle("Exit");
        dialog.setContentView(R.layout.exit_dialog);
        Button exit = (Button) dialog.findViewById(R.id.exit);
        Button saveExit= (Button) dialog.findViewById(R.id.exitSave);
        Button cancel = (Button) dialog.findViewById(R.id.cancelButton);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                DrawingCanvas.this.finish();
            }
        });
        saveExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                save(dv.context);

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            dialog.dismiss();
            }
        });
        dialog.show();

    }
    private  File getOutputMediaFile(){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/PaintShop");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator );
        return mediaFile;
    }
    public void save(final Context context){
        final AlertDialog.Builder editalert = new AlertDialog.Builder(DrawingCanvas.this);
        editalert.setTitle("Please Enter the name with which you want to Save");
        final EditText input = new EditText(DrawingCanvas.this);
        editalert.setView(input);
        editalert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String name= input.getText().toString();
                Bitmap bitmap = dv.getDrawingCache();

                File pictureFile = getOutputMediaFile();
                if (pictureFile == null) {
                    Log.d("TAG",
                            "Error creating media file, check storage permissions: ");// e.getMessage());
                    return;
                }


                File file = new File(pictureFile,name+".png");
                try
                {
                    if(file.exists())
                    {
                        file.delete();
                    }
                    FileOutputStream ostream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, ostream);
                     ostream.flush();
                    ostream.close();
                    dv.invalidate();
                    MediaScannerConnection.scanFile(context,new String[] { file.getAbsolutePath() }, null,new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.wtf("ExternalStorage", "Scanned " + path + ":");
                            Log.wtf("ExternalStorage", "-> uri=" + uri);
                        }
                    });
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }finally
                {

                    dv.setDrawingCacheEnabled(false);
                    DrawingCanvas.this.finish();
                }
            }
        });
        editalert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                    }
                });
        editalert.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        dv.mPaint.setXfermode(null);
        eraser=false;
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case (R.id.red):
                setCheck(item);
                dv.mPaint.setAlpha(0xFF);
                dv.mPaint.setColor(Color.RED);

                return true;
            case (R.id.green):
                setCheck(item);
                dv.mPaint.setAlpha(0xFF);
                dv.setColor(Color.GREEN);
                return true;
            case (R.id.blue):
               setCheck(item);
                dv.mPaint.setAlpha(0xFF);
                dv.setColor(Color.BLUE);
                return true;
            case (R.id.black):
                setCheck(item);
                dv.mPaint.setAlpha(0xFF);
                dv.setColor(Color.BLACK);
                return true;
            case (R.id.yellow):
                setCheck(item);
                dv.mPaint.setAlpha(0xFF);
                dv.setColor(Color.YELLOW);
                return true;
            case (R.id.gray):
                setCheck(item);
                dv.mPaint.setAlpha(0xFF);
                dv.setColor(Color.GRAY);
                return true;
            case (R.id.straightLine):


                dv.setStraigth(!dv.isStraigth());
                item.setCheckable(dv.isStraigth());
                item.setChecked(dv.isStraigth());
                return true;

            case (R.id.full):


                  setCheck(item);
                    dv.mPaint.setAlpha(0xFF);
                    dv.mPaint.setXfermode(new PorterDuffXfermode(
                            PorterDuff.Mode.SRC_ATOP));

                    dv.setColor(Color.WHITE);

                return true;
            case (R.id.light):

                setCheck(item);
                dv.mPaint.setAlpha(0xFF);
                dv.mPaint.setXfermode(new PorterDuffXfermode(
                        PorterDuff.Mode.SRC_ATOP));
                dv.setColor(Color.WHITE);
                dv.mPaint.setAlpha(0x90);


                return true;
            case (R.id.sizeSmall):
                setSizeCheck(item);
                dv.setSize(10);
                return true;
            case (R.id.sizeMedium):
                setSizeCheck(item);
                dv.setSize(20);
                return true;
            case (R.id.sizeBig):
                setSizeCheck(item);
                dv.setSize(30);
                return true;
        }
        return false;
    }
    public void setCheck(MenuItem item){
        if (tempitem!=null){

            tempitem.setCheckable(false);
        }
        tempitem= item;
        tempitem.setCheckable(true);
        tempitem.setChecked(true);
    }
    public void setSizeCheck(MenuItem item){
        if (tempItemSize!=null){

            tempItemSize.setCheckable(false);
        }
        tempItemSize= item;
        tempItemSize.setCheckable(true);
        tempItemSize.setChecked(true);
    }
}
