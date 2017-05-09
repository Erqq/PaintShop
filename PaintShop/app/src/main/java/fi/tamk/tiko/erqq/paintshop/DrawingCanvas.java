package fi.tamk.tiko.erqq.paintshop;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;

/**
 * DrawingCanvas is the activity on which the user can draw with finger. It sets the DrawingView
 * as the contentView
 *
 * @author      Eerik Timonen
 * @version     1.0
 * @since       1.0
 */
public class DrawingCanvas extends AppCompatActivity {
    DrawingView dv;
    MenuItem tempItem;
    MenuItem tempItemSize;
    private boolean eraser = false;

    /**
     * Creates the DrawingView dv and sets it as the contentView. Gets the content of the bundle
     * if bundle is not null.
     *
     * @param savedInstanceState    contains possible saved states
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        dv = new DrawingView(this);
        dv.setDrawingCacheEnabled(true);
        dv.setColor(Color.BLACK);

        if (bundle != null) {
            System.out.println(bundle.get("loadedpicture"));
            if (bundle.containsKey("loadedpicture")) {
                dv.setLoaded(bundle.get("loadedpicture").toString());
            }
        }
        setContentView(dv);
    }

    /**
     * Creates the menu that has the color, brush size, eraser, straight line and save options.
     *
     * @param menu  the menu that contains the options.
     * @return      boolean true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        tempItem = menu.findItem(R.id.black);
        tempItem.setCheckable(true);
        tempItem.setChecked(true);
        tempItemSize = menu.findItem(R.id.sizeMedium);
        tempItemSize.setCheckable(true);
        tempItemSize.setChecked(true);
        return true;
    }

    /**
     * Shows the exit dialog on back button pressed. User can exit to main menu, save and exit and
     * cancel the dialog.
     *
     */
    @Override
    public void onBackPressed() {

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Exit");
        dialog.setContentView(R.layout.exit_dialog);
        Button exit = (Button) dialog.findViewById(R.id.exit);
        Button saveExit = (Button) dialog.findViewById(R.id.exitSave);
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

    /**
     * Makes the file location where the images are saved.
     *
     * @return      the file directory where the image is going to be saved.
     */
    private File getOutputMediaFile() {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.
                DIRECTORY_PICTURES).toString() + "/PaintShop");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator);
        return mediaFile;
    }

    /**
     * Shows the save dialog to user. Asks user the name of the file and saves the image with the
     * given name. Shows ok and cancel button.
     *
     * @param context   the context of the DrawingView
     */
    public void save(final Context context) {
        final AlertDialog.Builder editalert = new AlertDialog.Builder(DrawingCanvas.this);
        editalert.setTitle("Please Enter the name with which you want to Save the image");
        final EditText input = new EditText(DrawingCanvas.this);
        editalert.setView(input);
        editalert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String name = input.getText().toString();
                Bitmap bitmap = dv.getDrawingCache();
                File pictureFile = getOutputMediaFile();

                if (pictureFile == null) {
                    Log.d("TAG",
                            "Error creating media file, check storage permissions: ");
                    return;
                }

                File file = new File(pictureFile, name + ".png");
                try {
                    if (file.exists()) {
                        file.delete();
                    }
                    FileOutputStream ostream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, ostream);
                    ostream.flush();
                    ostream.close();
                    dv.invalidate();
                    MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()},
                            null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {

                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
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

    /**
     * Defines what happens when specific MenuItem is pressed.
     *
     * @param item  the menu item which is pressed
     * @return      boolean true when MenuItem is pressed
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        dv.mPaint.setXfermode(null);
        eraser = false;
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
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

            case (R.id.magenta):
                setCheck(item);
                dv.mPaint.setAlpha(0xFF);
                dv.mPaint.setColor(Color.MAGENTA);
                return true;

            case (R.id.cyan):
                setCheck(item);
                dv.mPaint.setAlpha(0xFF);
                dv.mPaint.setColor(Color.CYAN);
                return true;

            case (R.id.straightLine):
                dv.setStraight(!dv.isStraight());
                item.setCheckable(dv.isStraight());
                item.setChecked(dv.isStraight());
                return true;

            case (R.id.full):
                setCheck(item);
                dv.mPaint.setAlpha(0xFF);
                dv.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
                dv.setColor(Color.WHITE);
                return true;

            case (R.id.light):
                setCheck(item);
                dv.mPaint.setAlpha(0xFF);
                dv.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
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

            case (R.id.save):
                save(dv.context);
                return true;
        }
        return false;
    }

    /**
     * Sets the item which is pressed to checkable and checked. This method handles the color and
     * eraser items
     *
     * @param item  the MenuItem which is pressed
     */
    public void setCheck(MenuItem item) {
        if (tempItem != null) {

            tempItem.setCheckable(false);
        }
        tempItem = item;
        tempItem.setCheckable(true);
        tempItem.setChecked(true);
    }

    /**
     * Sets the item which is pressed to checkable and checked. This method handles the brush size
     * items.
     *
     * @param item  the MenuItem which is pressed
     */
    public void setSizeCheck(MenuItem item) {
        if (tempItemSize != null) {
            tempItemSize.setCheckable(false);
        }
        tempItemSize = item;
        tempItemSize.setCheckable(true);
        tempItemSize.setChecked(true);
    }
}
